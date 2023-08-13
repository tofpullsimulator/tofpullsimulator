package org.eos.tof.common;

import java.util.stream.Stream;

import org.eos.tof.common.counters.PityCounter;
import org.eos.tof.common.counters.StatisticsCounter;
import org.eos.tof.common.counters.TokenCounter;
import org.eos.tof.common.handlers.PityHandler;
import org.eos.tof.common.handlers.PullHandler;
import org.eos.tof.common.handlers.SSRareHelper;
import org.eos.tof.common.handlers.TokenHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
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
class BannerFactoryTest {

    @Autowired
    private BannerFactory factory;

    @MethodSource("arguments")
    @ParameterizedTest
    void shouldCreateBanners(final Banner.RateMode rate, final boolean isTheory) {
        factory.setSpec(Banner.Spec.YULAN);
        factory.setRate(rate);
        factory.setTheory(isTheory);

        var banner = factory.getObject();

        Assertions.assertInstanceOf(Banner.class, banner);
        Assertions.assertEquals(Banner.Spec.YULAN, banner.getSpec());
        Assertions.assertEquals(rate, banner.getRate());
        Assertions.assertEquals(isTheory, banner.isTheory());
    }

    private static Stream<Arguments> arguments() {
        return Stream.of(
                Arguments.of(Banner.RateMode.NORMAL, true),
                Arguments.of(Banner.RateMode.NORMAL, false),
                Arguments.of(Banner.RateMode.GUARANTEE, true),
                Arguments.of(Banner.RateMode.GUARANTEE, false)
        );
    }
}
