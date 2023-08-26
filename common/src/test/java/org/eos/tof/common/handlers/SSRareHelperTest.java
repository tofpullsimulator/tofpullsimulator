package org.eos.tof.common.handlers;

import java.util.random.RandomGenerator;

import org.eos.tof.common.Banner;
import org.eos.tof.common.BannerFactory;
import org.eos.tof.common.Config;
import org.eos.tof.common.counters.PityCounter;
import org.eos.tof.common.counters.StatisticsCounter;
import org.eos.tof.common.counters.TokenCounter;
import org.eos.tof.common.handlers.matrices.MatrixHandlers;
import org.eos.tof.common.handlers.weapons.WeaponHandlers;
import org.eos.tof.common.History;
import org.eos.tof.common.MatrixBanner;
import org.eos.tof.common.WeaponBanner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {
        BannerFactory.class,
        Config.class,
        History.class,
        MatrixHandlers.class,
        PityCounter.class,
        SSRareHelper.class,
        StatisticsCounter.class,
        TokenCounter.class,
        WeaponHandlers.class
})
class SSRareHelperTest {

    @Autowired
    private BannerFactory factory;
    @Autowired
    private SSRareHelper helper;

    @MockBean
    private RandomGenerator rng;

    private Banner banner;

    @BeforeEach
    void setUp() {
        when(rng.nextBoolean()).thenReturn(true);
        factory.setSpec(Banner.Spec.YULAN);
    }

    @AfterEach
    void tearDown() {
        banner.reset();
    }

    @CsvSource({
            "false, 1, 1, 0, 0, 0, 0",
            "true,  0, 0, 1, 1, 1, 0",
            "true,  0, 0, 1, 1, 1, 1",
            "true,  0, 0, 1, 1, 1, 2",
            "true,  0, 0, 1, 1, 1, 3",
    })
    @ParameterizedTest
    void shouldPullOnMatrices(final boolean isStandard, final int lost, final int lostRow, final int won,
                              final int wonRow, final int bannerWeapon, final int corner) {
        factory.setClazz(MatrixBanner.class);
        banner = factory.getObject();
        banner.statistics().set(StatisticsCounter.BRAIN_PIECES, 3);
        banner.statistics().set(StatisticsCounter.HANDS_PIECES, 3);
        banner.statistics().set(StatisticsCounter.HEAD_PIECES, 3);
        banner.statistics().set(StatisticsCounter.HEART_PIECES, 3);

        when(rng.nextBoolean()).thenReturn(isStandard);
        when(rng.nextInt(anyInt())).thenReturn(corner);
        helper.pullMatrix(banner);

        var pity = banner.pity();
        Assertions.assertEquals(lost, pity.getLost());
        Assertions.assertEquals(lostRow, pity.getLostInARow());
        Assertions.assertEquals(won, pity.getWon());
        Assertions.assertEquals(wonRow, pity.getWonInARow());
        Assertions.assertEquals(bannerWeapon, banner.statistics().getTotalMatrixPieces());
    }

    @Test
    void shouldAlwaysPullBannerWeaponWhenPullingOnMatrices() {
        factory.setClazz(MatrixBanner.class);
        factory.setTheory(true);
        banner = factory.getObject();

        banner.pity().set(PityCounter.LOST_IN_A_ROW, 2);
        when(rng.nextBoolean()).thenReturn(true);

        helper.pullMatrix(banner);

        var pity = banner.pity();
        Assertions.assertEquals(0, pity.getLost());
        Assertions.assertEquals(0, pity.getLostInARow());
        Assertions.assertEquals(1, pity.getWon());
        Assertions.assertEquals(1, pity.getWonInARow());
        Assertions.assertEquals(1, banner.statistics().getTotalMatrixPieces());
    }

    @Test
    void shouldAlwaysPullStandardWeaponWhenPullingOnMatrices() {
        factory.setClazz(MatrixBanner.class);
        factory.setTheory(true);
        banner = factory.getObject();

        banner.pity().set(PityCounter.WON_IN_A_ROW, 2);
        when(rng.nextBoolean()).thenReturn(true);

        helper.pullMatrix(banner);

        var pity = banner.pity();
        Assertions.assertEquals(1, pity.getLost());
        Assertions.assertEquals(1, pity.getLostInARow());
        Assertions.assertEquals(0, pity.getWon());
        Assertions.assertEquals(0, pity.getWonInARow());
        Assertions.assertEquals(0, banner.statistics().getTotalMatrixPieces());
    }

    @Test
    void shouldIgnoreTheTheoryWhenPullingOnMatrices() {
        factory.setClazz(MatrixBanner.class);
        factory.setTheory(false);
        banner = factory.getObject();

        banner.pity().set(PityCounter.LOST_IN_A_ROW, 2);
        when(rng.nextBoolean()).thenReturn(true);

        helper.pullMatrix(banner);

        var pity = banner.pity();
        Assertions.assertEquals(0, pity.getLost());
        Assertions.assertEquals(0, pity.getLostInARow());
        Assertions.assertEquals(1, pity.getWon());
        Assertions.assertEquals(1, pity.getWonInARow());
        Assertions.assertEquals(1, banner.statistics().getTotalMatrixPieces());
    }

    @CsvSource({"false, 1, 1, 0, 0, 0", "true,  0, 0, 1, 1, 1"})
    @ParameterizedTest
    void shouldPullOnWeapons(final boolean isStandard, final int lost, final int lostRow, final int won,
                             final int wonRow, final int bannerWeapon) {
        factory.setClazz(WeaponBanner.class);
        banner = factory.getObject();

        when(rng.nextBoolean()).thenReturn(isStandard);
        helper.pullWeapon(banner);

        var pity = banner.pity();
        Assertions.assertEquals(lost, pity.getLost());
        Assertions.assertEquals(lostRow, pity.getLostInARow());
        Assertions.assertEquals(won, pity.getWon());
        Assertions.assertEquals(wonRow, pity.getWonInARow());
        Assertions.assertEquals(bannerWeapon, banner.statistics().getWeaponBanner());
    }

    @Test
    void shouldAlwaysPullBannerWeaponWhenPullingOnWeapons() {
        factory.setClazz(WeaponBanner.class);
        factory.setTheory(true);
        banner = factory.getObject();

        banner.pity().set(PityCounter.LOST_IN_A_ROW, 2);
        when(rng.nextBoolean()).thenReturn(true);

        helper.pullWeapon(banner);

        var pity = banner.pity();
        Assertions.assertEquals(0, pity.getLost());
        Assertions.assertEquals(0, pity.getLostInARow());
        Assertions.assertEquals(1, pity.getWon());
        Assertions.assertEquals(1, pity.getWonInARow());
        Assertions.assertEquals(1, banner.statistics().getWeaponBanner());
    }

    @Test
    void shouldAlwaysPullStandardWeaponWhenPullingOnWeapons() {
        factory.setClazz(WeaponBanner.class);
        factory.setTheory(true);
        banner = factory.getObject();

        banner.pity().set(PityCounter.WON_IN_A_ROW, 2);
        when(rng.nextBoolean()).thenReturn(true);

        helper.pullWeapon(banner);

        var pity = banner.pity();
        Assertions.assertEquals(1, pity.getLost());
        Assertions.assertEquals(1, pity.getLostInARow());
        Assertions.assertEquals(0, pity.getWon());
        Assertions.assertEquals(0, pity.getWonInARow());
        Assertions.assertEquals(0, banner.statistics().getWeaponBanner());
    }

    @Test
    void shouldIgnoreTheTheoryWhenPullingOnWeapons() {
        factory.setClazz(WeaponBanner.class);
        factory.setTheory(false);
        banner = factory.getObject();

        banner.pity().set(PityCounter.LOST_IN_A_ROW, 2);
        when(rng.nextBoolean()).thenReturn(true);

        helper.pullWeapon(banner);

        var pity = banner.pity();
        Assertions.assertEquals(0, pity.getLost());
        Assertions.assertEquals(0, pity.getLostInARow());
        Assertions.assertEquals(1, pity.getWon());
        Assertions.assertEquals(1, pity.getWonInARow());
        Assertions.assertEquals(1, banner.statistics().getWeaponBanner());
    }
}
