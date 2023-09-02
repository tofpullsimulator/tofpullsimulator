package org.eos.tof.common;

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
        MatrixHandlers.class,
        SSRareHelper.class,
        WeaponHandlers.class
})
class WeaponBannerTest {

    @Autowired
    private BannerFactory factory;

    private WeaponBanner banner;

    @BeforeEach
    void setUp() {
        factory.setSpec(Banner.Spec.YULAN);
        factory.setClazz(WeaponBanner.class);
        factory.setRate(Banner.RateMode.WEAPON_NORMAL);
        banner = (WeaponBanner) factory.getObject();
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
        banner.pull(-1);
        Assertions.assertTrue(7 >= banner.statistics().getWeaponBanner());
    }

    @Test
    void shouldThrowNPEWhenNoSpec() {
        factory.setSpec(null);
        var banner = factory.getObject();

        Assertions.assertThrows(NullPointerException.class, () -> banner.pull(1));
    }
}
