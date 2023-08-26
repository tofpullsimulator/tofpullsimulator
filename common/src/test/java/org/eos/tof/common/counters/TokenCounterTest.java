package org.eos.tof.common.counters;

import java.util.random.RandomGenerator;

import org.eos.tof.common.History;
import org.eos.tof.common.items.Normal;
import org.eos.tof.common.items.Rare;
import org.eos.tof.common.items.SRare;
import org.eos.tof.common.items.SSRare;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(classes = {History.class, TokenCounter.class})
class TokenCounterTest {

    @Autowired
    private History history;
    @Autowired
    private TokenCounter tokens;

    @MockBean
    private RandomGenerator rng;

    @AfterEach
    void tearDown() {
        history.reset();
        tokens.reset();
    }

    @Test
    void shouldGetValueOfMatrixTokenCounter() {
        tokens.set(TokenCounter.MATRIX_TOKENS, 1);
        Assertions.assertEquals(1, tokens.getMatrixTokens());
    }

    @Test
    void shouldGetValueOfBuyBrainPiecesCounter() {
        tokens.set(TokenCounter.BUY_BRAIN_PIECES, 1);
        Assertions.assertEquals(1, tokens.getBuyBrainPieces());
    }

    @Test
    void shouldGetValueOfBuyHandsPiecesCounter() {
        tokens.set(TokenCounter.BUY_HANDS_PIECES, 1);
        Assertions.assertEquals(1, tokens.getBuyHandsPieces());
    }

    @Test
    void shouldGetValueOfBuyHeadPiecesCounter() {
        tokens.set(TokenCounter.BUY_HEAD_PIECES, 1);
        Assertions.assertEquals(1, tokens.getBuyHeadPieces());
    }

    @Test
    void shouldGetValueOfBuyHeartPiecesCounter() {
        tokens.set(TokenCounter.BUY_HEART_PIECES, 1);
        Assertions.assertEquals(1, tokens.getBuyHeartPieces());
    }

    @Test
    void shouldGetValueOfWeaponTokenCounter() {
        tokens.set(TokenCounter.WEAPON_TOKENS, 1);
        Assertions.assertEquals(1, tokens.getWeaponTokens());
    }

    @Test
    void shouldThrowExceptionWhenGettingWithoutMetricName() {
        Assertions.assertThrows(UnsupportedOperationException.class,
                () -> tokens.increment());
    }

    @Test
    void shouldIncrementsMatrixTokens() {
        tokens.increment(TokenCounter.MATRIX_TOKENS);
        Assertions.assertEquals(1, tokens.getMatrixTokens());
    }

    @Test
    void shouldNotIncrementWhenSSRIsGiven() {
        history.add(new SSRare("weaponsName"));
        tokens.increment(TokenCounter.WEAPON_TOKENS);
        Assertions.assertEquals(0, tokens.getWeaponTokens());
    }

    @Test
    void shouldIncrementWhenSSRIsGiven() {
        history.add(new SSRare(rng));
        tokens.increment(TokenCounter.WEAPON_TOKENS);
        Assertions.assertEquals(10, tokens.getWeaponTokens());
    }

    @Test
    void shouldIncrementWhenSRIsGiven() {
        history.add(new SRare(rng));
        tokens.increment(TokenCounter.WEAPON_TOKENS);
        Assertions.assertEquals(2, tokens.getWeaponTokens());
    }

    @Test
    void shouldIncrementWhenRareIsGiven() {
        history.add(new Rare(rng));
        tokens.increment(TokenCounter.WEAPON_TOKENS);
        Assertions.assertEquals(1, tokens.getWeaponTokens());
    }

    @Test
    void shouldIncrementWhenNormalIsGiven() {
        history.add(new Normal());
        tokens.increment(TokenCounter.WEAPON_TOKENS);
        Assertions.assertEquals(1, tokens.getWeaponTokens());
    }

    @Test
    void shouldResetTheTokens() {
        tokens.set(TokenCounter.WEAPON_TOKENS, 10);
        Assertions.assertEquals(10, tokens.getWeaponTokens());

        tokens.reset();
        Assertions.assertEquals(0, tokens.getWeaponTokens());
    }
}
