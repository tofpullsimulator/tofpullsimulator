package org.eos.tof.common.handlers;

import java.util.random.RandomGenerator;

import org.eos.tof.common.Banner;
import org.eos.tof.common.BannerFactory;
import org.eos.tof.common.counters.PityCounter;
import org.eos.tof.common.counters.StatisticsCounter;
import org.eos.tof.common.counters.TokenCounter;
import org.eos.tof.common.History;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.Mockito.when;

@SpringBootTest(classes = {
        BannerFactory.class,
        History.class,
        PityCounter.class,
        PityHandler.class,
        SSRareHelper.class,
        StatisticsCounter.class,
        TokenCounter.class
})
class SSRareHelperTest {

    @MockBean
    private RandomGenerator rng;

    @Autowired
    private BannerFactory factory;
    @Autowired
    private SSRareHelper helper;

    private Banner banner;

    @BeforeEach
    void setUp() {
        when(rng.nextBoolean()).thenReturn(true);
        factory.setSpec(Banner.Spec.YULAN);
        banner = factory.getObject();
    }

    @AfterEach
    void tearDown() {
        banner.reset();
    }

    @CsvSource({
            "false, 1, 1, 0, 0, 0",
            "true,  0, 0, 1, 1, 1"
    })
    @ParameterizedTest
    void shouldPull(final boolean isStandard, final int lost, final int lostRow, final int won, final int wonRow,
                final int bannerWeapon) {
        when(rng.nextBoolean()).thenReturn(isStandard);
        helper.pull(banner);

        var pity = banner.getPity();
        Assertions.assertEquals(lost, pity.get(PityCounter.LOST));
        Assertions.assertEquals(lostRow, pity.get(PityCounter.LOST_IN_A_ROW));
        Assertions.assertEquals(won, pity.get(PityCounter.WON));
        Assertions.assertEquals(wonRow, pity.get(PityCounter.WON_IN_A_ROW));
        Assertions.assertEquals(bannerWeapon, banner.getStatistics().get(StatisticsCounter.BANNER_WEAPON));
    }

    @Test
    void shouldAlwaysPullBannerWeapon() {
        banner.setTheory(true);
        banner.getPity().set(PityCounter.LOST_IN_A_ROW, 2);
        when(rng.nextBoolean()).thenReturn(true);

        helper.pull(banner);

        var pity = banner.getPity();
        Assertions.assertEquals(0, pity.get(PityCounter.LOST));
        Assertions.assertEquals(0, pity.get(PityCounter.LOST_IN_A_ROW));
        Assertions.assertEquals(1, pity.get(PityCounter.WON));
        Assertions.assertEquals(1, pity.get(PityCounter.WON_IN_A_ROW));
        Assertions.assertEquals(1, banner.getStatistics().get(StatisticsCounter.BANNER_WEAPON));
    }

    @Test
    void shouldAlwaysPullStandardWeapon() {
        banner.setTheory(true);
        banner.getPity().set(PityCounter.WON_IN_A_ROW, 2);
        when(rng.nextBoolean()).thenReturn(true);

        helper.pull(banner);

        var pity = banner.getPity();
        Assertions.assertEquals(1, pity.get(PityCounter.LOST));
        Assertions.assertEquals(1, pity.get(PityCounter.LOST_IN_A_ROW));
        Assertions.assertEquals(0, pity.get(PityCounter.WON));
        Assertions.assertEquals(0, pity.get(PityCounter.WON_IN_A_ROW));
        Assertions.assertEquals(0, banner.getStatistics().get(StatisticsCounter.BANNER_WEAPON));
    }

    @Test
    void shouldIgnoreTheTheory() {
        banner.setTheory(false);
        banner.getPity().set(PityCounter.LOST_IN_A_ROW, 2);
        when(rng.nextBoolean()).thenReturn(true);

        helper.pull(banner);

        var pity = banner.getPity();
        Assertions.assertEquals(0, pity.get(PityCounter.LOST));
        Assertions.assertEquals(0, pity.get(PityCounter.LOST_IN_A_ROW));
        Assertions.assertEquals(1, pity.get(PityCounter.WON));
        Assertions.assertEquals(1, pity.get(PityCounter.WON_IN_A_ROW));
        Assertions.assertEquals(1, banner.getStatistics().get(StatisticsCounter.BANNER_WEAPON));
    }
}
