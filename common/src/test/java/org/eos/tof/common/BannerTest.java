package org.eos.tof.common;

import org.eos.tof.common.counters.PityCounter;
import org.eos.tof.common.counters.StatisticsCounter;
import org.eos.tof.common.counters.TokenCounter;
import org.eos.tof.common.handlers.PityHandler;
import org.eos.tof.common.handlers.PullHandler;
import org.eos.tof.common.handlers.SSRareHelper;
import org.eos.tof.common.handlers.TokenHandler;
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
        PityCounter.class,
        PityHandler.class,
        PullHandler.class,
        SSRareHelper.class,
        StatisticsCounter.class,
        TokenCounter.class,
        TokenHandler.class,
})
class BannerTest {

    @Autowired
    private BannerFactory factory;

    private Banner banner;

    @BeforeEach
    void setUp() {
        factory.setSpec(Banner.Spec.YULAN);
        banner = factory.getObject();
    }

    @AfterEach
    void tearDown() {
        banner.reset();
    }

    @Test
    void shouldPullTen() {
        banner.pull(10);
        Assertions.assertEquals(10, banner.getHistory().get().size());
    }

    @Test
    void shouldPullToMax() {
        banner.pull(-1);
        Assertions.assertEquals(7, banner.getStatistics().get(StatisticsCounter.BANNER_WEAPON));
    }

    @Test
    void shouldThrowNPEWhenNoSpec() {
        factory.setSpec(null);
        var banner = factory.getObject();

        Assertions.assertThrows(NullPointerException.class, () -> banner.pull(1));
    }

    @Test
    void shouldGetABannerSpecFromAString() {
        var spec = Banner.Spec.from("Yu Lan");
        Assertions.assertEquals(Banner.Spec.YULAN, spec);
    }
}
