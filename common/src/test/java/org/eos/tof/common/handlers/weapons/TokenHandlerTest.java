package org.eos.tof.common.handlers.weapons;

import java.util.random.RandomGenerator;

import org.eos.tof.common.Banner;
import org.eos.tof.common.BannerFactory;
import org.eos.tof.common.counters.TokenCounter;
import org.eos.tof.common.handlers.matrices.MatrixHandlers;
import org.eos.tof.common.handlers.SSRareHelper;
import org.eos.tof.common.items.Rare;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(classes = {
        BannerFactory.class,
        MatrixHandlers.class,
        SSRareHelper.class,
        TokenHandler.class,
        WeaponHandlers.class
})
class TokenHandlerTest {

    @Autowired
    private BannerFactory factory;
    @Autowired
    @Qualifier(value = "weaponTokenHandler")
    private TokenHandler handler;

    @MockBean
    private RandomGenerator rng;

    private Banner banner;

    @BeforeEach
    void setUp() {
        factory.setSpec(Banner.Spec.YULAN);
        banner = factory.getObject();
        handler.setNext(null);
    }

    @AfterEach
    void tearDown() {
        banner.reset();
    }

    @Test
    void shouldIncrementStatisticsAndTokens() {
        banner.history().add(new Rare(rng));

        var result = handler.check(banner);
        Assertions.assertTrue(result);

        Assertions.assertEquals(1, banner.statistics().getRare());
        Assertions.assertEquals(1, banner.tokens().getWeaponTokens());
    }

    @Test
    void shouldResetTokensWhenSSRCanBeBought() {
        banner.history().add(new Rare(rng));
        banner.tokens().set(TokenCounter.WEAPON_TOKENS, 119);

        var result = handler.check(banner);
        Assertions.assertTrue(result);

        Assertions.assertEquals(1, banner.statistics().getRare());
        Assertions.assertEquals(0, banner.tokens().getWeaponTokens());
    }
}
