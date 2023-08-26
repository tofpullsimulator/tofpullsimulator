package org.eos.tof.common;

import java.util.stream.Stream;

import org.eos.tof.common.counters.PityCounter;
import org.eos.tof.common.counters.StatisticsCounter;
import org.eos.tof.common.counters.TokenCounter;
import org.eos.tof.common.handlers.matrices.MatrixHandlers;
import org.eos.tof.common.handlers.SSRareHelper;
import org.eos.tof.common.handlers.weapons.WeaponHandlers;
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
        MatrixHandlers.class,
        PityCounter.class,
        SSRareHelper.class,
        StatisticsCounter.class,
        TokenCounter.class,
        WeaponHandlers.class
})
class BannerFactoryTest {

    @Autowired
    private BannerFactory factory;

    @MethodSource("arguments")
    @ParameterizedTest
    void shouldCreateBanners(final Class<? extends Banner> clazz, final Banner.RateMode rate, final boolean isTheory) {
        factory.setSpec(Banner.Spec.YULAN);
        factory.setClazz(clazz);
        factory.setRate(rate);
        factory.setTheory(isTheory);

        var banner = factory.getObject();

        Assertions.assertInstanceOf(clazz, banner);
        Assertions.assertEquals(Banner.Spec.YULAN, banner.spec());
        Assertions.assertEquals(rate, banner.rate());
        Assertions.assertEquals(isTheory, banner.theory());
    }

    private static Stream<Arguments> arguments() {
        return Stream.of(
                Arguments.of(WeaponBanner.class, Banner.RateMode.WEAPON_NORMAL, true),
                Arguments.of(WeaponBanner.class, Banner.RateMode.WEAPON_NORMAL, false),
                Arguments.of(WeaponBanner.class, Banner.RateMode.WEAPON_GUARANTEE, true),
                Arguments.of(WeaponBanner.class, Banner.RateMode.WEAPON_GUARANTEE, false),
                Arguments.of(MatrixBanner.class, Banner.RateMode.WEAPON_NORMAL, true),
                Arguments.of(MatrixBanner.class, Banner.RateMode.WEAPON_NORMAL, false),
                Arguments.of(MatrixBanner.class, Banner.RateMode.WEAPON_GUARANTEE, true),
                Arguments.of(MatrixBanner.class, Banner.RateMode.WEAPON_GUARANTEE, false)
        );
    }
}
