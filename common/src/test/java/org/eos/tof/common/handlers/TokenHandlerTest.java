package org.eos.tof.common.handlers;

import org.eos.tof.common.Banner;
import org.eos.tof.common.BannerFactory;
import org.eos.tof.common.counters.PityCounter;
import org.eos.tof.common.counters.StatisticsCounter;
import org.eos.tof.common.counters.TokenCounter;
import org.eos.tof.common.History;
import org.eos.tof.common.items.Normal;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {
        BannerFactory.class,
        History.class,
        PityCounter.class,
        StatisticsCounter.class,
        TokenCounter.class,
        TokenHandler.class
})
class TokenHandlerTest {

    @Autowired
    private BannerFactory factory;
    @Autowired
    private TokenHandler handler;

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
    void shouldIncrementStatisticsAndTokens() {
        banner.getHistory().add(new Normal());

        var result = handler.check(banner);
        Assertions.assertTrue(result);

        Assertions.assertEquals(1, banner.getStatistics().get(StatisticsCounter.N));
        Assertions.assertEquals(1, banner.getTokens().get());
    }

    @Test
    void shouldResetTokensWhenSSRCanBeBought() {
        banner.getHistory().add(new Normal());
        banner.getTokens().set(119);

        var result = handler.check(banner);
        Assertions.assertTrue(result);

        Assertions.assertEquals(1, banner.getStatistics().get(StatisticsCounter.N));
        Assertions.assertEquals(0, banner.getTokens().get());
    }
}
