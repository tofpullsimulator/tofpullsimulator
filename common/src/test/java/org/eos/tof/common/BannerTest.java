package org.eos.tof.common;

import org.eos.tof.common.counters.PityCounter;
import org.eos.tof.common.counters.StatisticsCounter;
import org.eos.tof.common.counters.TokenCounter;
import org.eos.tof.common.handlers.Handler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.Mockito.verify;

@SpringBootTest(classes = {Config.class})
class BannerTest {

    @MockBean
    private History history;
    @MockBean
    private PityCounter pity;
    @MockBean
    private StatisticsCounter statistics;
    @MockBean
    private TokenCounter tokens;
    @MockBean
    private Handler weaponHandlers;

    @Spy
    private final Banner banner = new Banner(Banner.Spec.YULAN) {

        @Override
        public Banner pull(long amount) {
            return this;
        }
    };

    @BeforeEach
    void setUp() {
        banner.rate(Banner.RateMode.WEAPON_NORMAL);
        banner.theory(true);
        banner.history(history);
        banner.pity(pity);
        banner.statistics(statistics);
        banner.tokens(tokens);
        banner.handlers(weaponHandlers);
    }

    @Test
    void shouldReset() {
        banner.reset();

        verify(history).reset();
        verify(pity).reset();
        verify(statistics).reset();
        verify(tokens).reset();
    }

    @Test
    void shouldGetSpec() {
        Assertions.assertEquals(Banner.Spec.YULAN, banner.spec());
    }

    @Test
    void shouldGetRate() {
        Assertions.assertEquals(Banner.RateMode.WEAPON_NORMAL, banner.rate());
    }

    @Test
    void shouldGetTheory() {
        Assertions.assertTrue(banner.theory());
    }

    @Test
    void shouldGetHistory() {
        Assertions.assertEquals(history, banner.history());
    }

    @Test
    void shouldGetPity() {
        Assertions.assertEquals(pity, banner.pity());
    }

    @Test
    void shouldGetStatistics() {
        Assertions.assertEquals(statistics, banner.statistics());
    }

    @Test
    void shouldGetTokens() {
        Assertions.assertEquals(tokens, banner.tokens());
    }

    @Test
    void shouldGetHandlers() {
        Assertions.assertEquals(weaponHandlers, banner.handlers());
    }

    @Test
    void shouldGetABannerSpecFromAString() {
        var spec = Banner.Spec.from("Yu Lan");
        Assertions.assertEquals(Banner.Spec.YULAN, spec);
    }
}

