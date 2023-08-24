package org.eos.tof.common.handlers;

import java.util.random.RandomGenerator;

import org.eos.tof.common.Banner;
import org.eos.tof.common.BannerFactory;
import org.eos.tof.common.counters.PityCounter;
import org.eos.tof.common.counters.StatisticsCounter;
import org.eos.tof.common.counters.TokenCounter;
import org.eos.tof.common.History;
import org.eos.tof.common.items.Normal;
import org.eos.tof.common.items.Rare;
import org.eos.tof.common.items.SRare;
import org.eos.tof.common.items.SSRare;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.Mockito.when;

@SpringBootTest(classes = {
        BannerFactory.class,
        History.class,
        PityCounter.class,
        PullHandler.class,
        SSRareHelper.class,
        StatisticsCounter.class,
        TokenCounter.class
})
class PullHandlerTest {

    @MockBean
    private RandomGenerator rng;

    @Autowired
    private BannerFactory factory;
    @Autowired
    private PullHandler handler;

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

    @ValueSource(strings = {PityCounter.SSR, PityCounter.SR})
    @ParameterizedTest
    void shouldSkipIfPityOrGuaranteeIsHit(final String metricName) {
        banner.getPity().set(PityCounter.SSR, 1);
        banner.getPity().set(PityCounter.SR, 1);
        banner.getPity().set(metricName, 0);

        var result = handler.check(banner);
        Assertions.assertTrue(result);
        Assertions.assertTrue(banner.getHistory().get().isEmpty());
    }

    @EnumSource(Banner.RateMode.class)
    @ParameterizedTest
    void shouldPullAnSSRare(final Banner.RateMode rate) {
        banner.setRate(rate);
        banner.getPity().increment();
        when(rng.nextDouble(100)).thenReturn(banner.getRate().getSsr());

        var result = handler.check(banner);
        Assertions.assertTrue(result);

        var last = banner.getHistory().getLast();
        Assertions.assertInstanceOf(SSRare.class, last);
    }

    @EnumSource(Banner.RateMode.class)
    @ParameterizedTest
    void shouldPullAnSRare(final Banner.RateMode rate) {
        banner.setRate(rate);
        banner.getPity().increment();
        when(rng.nextDouble(100)).thenReturn(banner.getRate().getSr());

        var result = handler.check(banner);
        Assertions.assertTrue(result);

        var last = banner.getHistory().getLast();
        Assertions.assertInstanceOf(SRare.class, last);
    }

    @EnumSource(Banner.RateMode.class)
    @ParameterizedTest
    void shouldPullAnRare(final Banner.RateMode rate) {
        banner.setRate(rate);
        banner.getPity().increment();
        when(rng.nextDouble(100)).thenReturn(banner.getRate().getR());

        var result = handler.check(banner);
        Assertions.assertTrue(result);

        var last = banner.getHistory().getLast();
        Assertions.assertInstanceOf(Rare.class, last);
    }

    @EnumSource(Banner.RateMode.class)
    @ParameterizedTest
    void shouldPullANormal(final Banner.RateMode rate) {
        banner.setRate(rate);
        banner.getPity().increment();
        when(rng.nextDouble(100)).thenReturn(banner.getRate().getN());

        var result = handler.check(banner);
        Assertions.assertTrue(result);

        var last = banner.getHistory().getLast();
        Assertions.assertInstanceOf(Normal.class, last);
    }

    @EnumSource(Banner.RateMode.class)
    @ParameterizedTest
    void shouldPullNothing(final Banner.RateMode rate) {
        banner.setRate(rate);
        banner.getPity().increment();
        when(rng.nextDouble(100)).thenReturn(101.0);

        var result = handler.check(banner);
        Assertions.assertFalse(result);
        Assertions.assertTrue(banner.getHistory().get().isEmpty());
    }
}
