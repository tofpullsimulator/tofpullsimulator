package org.eos.tof.common.handlers.matrices;

import java.util.random.RandomGenerator;

import org.eos.tof.common.Banner;
import org.eos.tof.common.BannerFactory;
import org.eos.tof.common.counters.PityCounter;
import org.eos.tof.common.counters.StatisticsCounter;
import org.eos.tof.common.counters.TokenCounter;
import org.eos.tof.common.handlers.SSRareHelper;
import org.eos.tof.common.handlers.weapons.WeaponHandlers;
import org.eos.tof.common.History;
import org.eos.tof.common.items.Matrix;
import org.eos.tof.common.items.Rare;
import org.eos.tof.common.items.SRare;
import org.eos.tof.common.MatrixBanner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.Mockito.when;

@SpringBootTest(classes = {
        BannerFactory.class,
        History.class,
        MatrixHandlers.class,
        MatrixHandlers.class,
        PityCounter.class,
        PullHandler.class,
        SSRareHelper.class,
        StatisticsCounter.class,
        TokenCounter.class,
        WeaponHandlers.class
})
class PullHandlerTest {

    @Autowired
    private BannerFactory factory;
    @Autowired
    @Qualifier(value = "matrixPullHandler")
    private PullHandler handler;

    @MockBean
    private RandomGenerator rng;

    private Banner banner;

    @BeforeEach
    void setUp() {
        factory.setSpec(Banner.Spec.YULAN);
        factory.setClazz(MatrixBanner.class);
        handler.setNext(null);
    }

    @AfterEach
    void tearDown() {
        banner.reset();
    }

    @ValueSource(strings = {PityCounter.SSR, PityCounter.SR})
    @ParameterizedTest
    void shouldSkipIfPityOrGuaranteeIsHit(final String metricName) {
        banner = factory.getObject();
        banner.pity().set(PityCounter.SSR, 1);
        banner.pity().set(PityCounter.SR, 1);
        banner.pity().set(metricName, 0);

        var result = handler.check(banner);
        Assertions.assertTrue(result);
        Assertions.assertTrue(banner.history().get().isEmpty());
    }

    @EnumSource(value = Banner.RateMode.class, names = {"MATRIX_GUARANTEE", "MATRIX_NORMAL"})
    @ParameterizedTest
    void shouldPullAnSSRare(final Banner.RateMode rate) {
        factory.setRate(rate);
        banner = factory.getObject();

        banner.pity().increment();
        when(rng.nextDouble(100)).thenReturn(banner.rate().getSsr());

        var result = handler.check(banner);
        Assertions.assertTrue(result);

        var last = banner.history().getLast();
        Assertions.assertInstanceOf(Matrix.class, last);
    }

    @EnumSource(value = Banner.RateMode.class, names = {"MATRIX_GUARANTEE", "MATRIX_NORMAL"})
    @ParameterizedTest
    void shouldPullAnSRare(final Banner.RateMode rate) {
        factory.setRate(rate);
        banner = factory.getObject();

        banner.pity().increment();
        when(rng.nextDouble(100)).thenReturn(banner.rate().getSr());

        var result = handler.check(banner);
        Assertions.assertTrue(result);

        var last = banner.history().getLast();
        Assertions.assertInstanceOf(SRare.class, last);
    }

    @EnumSource(value = Banner.RateMode.class, names = {"MATRIX_GUARANTEE", "MATRIX_NORMAL"})
    @ParameterizedTest
    void shouldPullAnRare(final Banner.RateMode rate) {
        factory.setRate(rate);
        banner = factory.getObject();

        banner.pity().increment();
        when(rng.nextDouble(100)).thenReturn(banner.rate().getR());

        var result = handler.check(banner);
        Assertions.assertTrue(result);

        var last = banner.history().getLast();
        Assertions.assertInstanceOf(Rare.class, last);
    }

    @EnumSource(value = Banner.RateMode.class, names = {"MATRIX_GUARANTEE", "MATRIX_NORMAL"})
    @ParameterizedTest
    void shouldPullNothing(final Banner.RateMode rate) {
        factory.setRate(rate);
        banner = factory.getObject();

        banner.pity().increment();
        when(rng.nextDouble(100)).thenReturn(101.0);

        var result = handler.check(banner);
        Assertions.assertFalse(result);
        Assertions.assertTrue(banner.history().get().isEmpty());
    }
}
