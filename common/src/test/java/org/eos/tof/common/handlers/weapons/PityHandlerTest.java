package org.eos.tof.common.handlers.weapons;

import java.util.random.RandomGenerator;

import org.eos.tof.common.Banner;
import org.eos.tof.common.BannerFactory;
import org.eos.tof.common.counters.PityCounter;
import org.eos.tof.common.counters.StatisticsCounter;
import org.eos.tof.common.counters.TokenCounter;
import org.eos.tof.common.handlers.matrices.MatrixHandlers;
import org.eos.tof.common.handlers.SSRareHelper;
import org.eos.tof.common.History;
import org.eos.tof.common.items.SRare;
import org.eos.tof.common.items.SSRare;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.Mockito.when;

@SpringBootTest(classes = {
        BannerFactory.class,
        History.class,
        MatrixHandlers.class,
        PityCounter.class,
        PityHandler.class,
        SSRareHelper.class,
        StatisticsCounter.class,
        TokenCounter.class,
        WeaponHandlers.class
})
class PityHandlerTest {

    @Autowired
    private BannerFactory factory;
    @Autowired
    @Qualifier(value = "weaponPityHandler")
    private PityHandler handler;

    @MockBean
    private RandomGenerator rng;

    private Banner banner;

    @BeforeEach
    void setUp() {
        when(rng.nextBoolean()).thenReturn(true);
        factory.setSpec(Banner.Spec.YULAN);
        banner = factory.getObject();
        handler.setNext(null);
    }

    @AfterEach
    void tearDown() {
        banner.reset();
    }

    @Test
    void shouldPullSSRWhenHittingPity() {
        banner.pity().set(PityCounter.SSR, 79);

        var result = handler.check(banner);
        Assertions.assertTrue(result);

        Assertions.assertEquals(0, banner.pity().getSSR());
        Assertions.assertEquals(1, banner.pity().getHit());

        var last = banner.history().getLast();
        Assertions.assertInstanceOf(SSRare.class, last);
    }

    @Test
    void shouldPullSRWhenGuarantee() {
        banner.pity().set(PityCounter.SR, 9);

        var result = handler.check(banner);
        Assertions.assertTrue(result);
        Assertions.assertEquals(0, banner.pity().getSR());

        var last = banner.history().getLast();
        Assertions.assertInstanceOf(SRare.class, last);
    }

    @Test
    void shouldNotPullAnyThing() {
        var result = handler.check(banner);
        Assertions.assertTrue(result);
        Assertions.assertTrue(banner.history().get().isEmpty());
    }
}
