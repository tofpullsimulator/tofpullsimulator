package org.eos.tof.common.handlers;

import java.util.random.RandomGenerator;

import org.eos.tof.common.Banner;
import org.eos.tof.common.BannerFactory;
import org.eos.tof.common.counters.PityCounter;
import org.eos.tof.common.counters.StatisticsCounter;
import org.eos.tof.common.counters.TokenCounter;
import org.eos.tof.common.History;
import org.eos.tof.common.items.SRare;
import org.eos.tof.common.items.SSRare;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.Mockito.when;

@SpringBootTest(classes = {
        BannerFactory.class,
        History.class,
        PityCounter.class,
        PityHandler.class,
        SSRareHelper.class,
        StatisticsCounter.class,
        TokenCounter.class
})
class PityHandlerTest {

    @MockBean
    private RandomGenerator rng;

    @Autowired
    private BannerFactory factory;
    @Autowired
    private PityHandler handler;

    private Banner banner;

    @BeforeEach
    void setUp() {
        when(rng.nextBoolean()).thenReturn(true);
        factory.setSpec(Banner.Spec.YULAN);
        banner = factory.getObject();
    }

    @AfterEach
    void tearDown() {
        banner.reset();
    }

    @Test
    void shouldPullSSRWhenHittingPity() {
        banner.getPity().set(PityCounter.SSR, 79);

        var result = handler.check(banner);
        Assertions.assertTrue(result);

        Assertions.assertEquals(0, banner.getPity().get(PityCounter.SSR));
        Assertions.assertEquals(1, banner.getPity().get(PityCounter.HIT));

        var last = banner.getHistory().getLast();
        Assertions.assertInstanceOf(SSRare.class, last);
    }

    @Test
    void shouldPullSRWhenGuarantee() {
        banner.getPity().set(PityCounter.SR, 9);

        var result = handler.check(banner);
        Assertions.assertTrue(result);
        Assertions.assertEquals(0, banner.getPity().get(PityCounter.SR));

        var last = banner.getHistory().getLast();
        Assertions.assertInstanceOf(SRare.class, last);
    }

    @Test
    void shouldNotPullAnyThing() {
        var result = handler.check(banner);
        Assertions.assertTrue(result);
        Assertions.assertTrue(banner.getHistory().get().isEmpty());
    }
}
