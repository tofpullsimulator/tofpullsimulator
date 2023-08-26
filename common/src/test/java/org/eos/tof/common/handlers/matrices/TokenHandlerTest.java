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
import org.eos.tof.common.items.SRare;
import org.eos.tof.common.MatrixBanner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(classes = {
        BannerFactory.class,
        History.class,
        MatrixHandlers.class,
        MatrixHandlers.class,
        PityCounter.class,
        SSRareHelper.class,
        StatisticsCounter.class,
        TokenCounter.class,
        TokenHandler.class,
        WeaponHandlers.class
})
class TokenHandlerTest {

    @Autowired
    private BannerFactory factory;
    @Autowired
    @Qualifier(value = "matrixTokenHandler")
    private TokenHandler handler;

    @MockBean
    private RandomGenerator rng;

    private Banner banner;

    @BeforeEach
    void setUp() {
        factory.setSpec(Banner.Spec.YULAN);
        factory.setClazz(MatrixBanner.class);
        banner = factory.getObject();
    }

    @AfterEach
    void tearDown() {
        banner.reset();
        handler.setNext(null);
    }

    @Test
    void shouldIncrementStatisticsAndTokens() {
        banner.history().add(new SRare(rng));

        var result = handler.check(banner);
        Assertions.assertTrue(result);

        Assertions.assertEquals(1, banner.statistics().getSR());
        Assertions.assertEquals(1, banner.tokens().getMatrixTokens());
    }

    @Test
    void shouldSetTotalMatrixPiecesWhenDoneWithBanner() {
        banner.history().add(new Matrix(rng));
        banner.statistics().set(StatisticsCounter.BRAIN_PIECES, 4);
        banner.statistics().set(StatisticsCounter.HANDS_PIECES, 4);
        banner.statistics().set(StatisticsCounter.HEAD_PIECES, 4);
        banner.statistics().set(StatisticsCounter.HEART_PIECES, 4);

        var result = handler.check(banner);
        Assertions.assertTrue(result);

        Assertions.assertEquals(16, banner.statistics().getTotalMatrixPieces());
        Assertions.assertEquals(1, banner.tokens().getMatrixTokens());
    }

    @ValueSource(ints = {2, 3})
    @ParameterizedTest
    void shouldBuyPieces(final int heartPieces) {
        ((MatrixBanner) banner).boxes(0);
        banner.history().add(new SRare(rng));
        banner.statistics().set(StatisticsCounter.BRAIN_PIECES, 4);
        banner.statistics().set(StatisticsCounter.HANDS_PIECES, 4);
        banner.statistics().set(StatisticsCounter.HEAD_PIECES, 4);
        banner.statistics().set(StatisticsCounter.HEART_PIECES, heartPieces);
        banner.tokens().set(TokenCounter.MATRIX_TOKENS, 79);

        var result = handler.check(banner);
        Assertions.assertTrue(result);

        Assertions.assertEquals(80, banner.tokens().getMatrixTokens());
        Assertions.assertEquals(1, banner.tokens().get(TokenCounter.BUY_HEART_PIECES));
    }
}
