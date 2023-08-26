package org.eos.tof.common;

import org.eos.tof.common.counters.PityCounter;
import org.eos.tof.common.counters.StatisticsCounter;
import org.eos.tof.common.counters.TokenCounter;
import org.eos.tof.common.handlers.matrices.MatrixHandlers;
import org.eos.tof.common.handlers.SSRareHelper;
import org.eos.tof.common.handlers.weapons.WeaponHandlers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
class MatrixBannerTest {

    @Autowired
    private BannerFactory factory;

    private MatrixBanner banner;

    @BeforeEach
    void setUp() {
        factory.setSpec(Banner.Spec.YULAN);
        factory.setClazz(MatrixBanner.class);
        factory.setRate(Banner.RateMode.MATRIX_NORMAL);
        banner = (MatrixBanner) factory.getObject();
        banner.boxes(1);
    }

    @AfterEach
    void tearDown() {
        banner.reset();
    }

    @Test
    void shouldPullTen() {
        banner.pull(10);
        Assertions.assertEquals(10, banner.history().get().size());
    }

    @Test
    void shouldPullToMax() {
        banner.boxes(2);
        banner.pull(-1);
        Assertions.assertTrue(16 >= banner.statistics().getTotalMatrixPieces());
    }

    @Test
    void shouldThrowNPEWhenNoSpec() {
        factory.setSpec(null);
        var banner = factory.getObject();

        Assertions.assertThrows(NullPointerException.class, () -> banner.pull(1));
    }
}
