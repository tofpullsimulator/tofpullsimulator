package org.eos.tof.common.counters;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {PityCounter.class})
class PityCounterTest {

    @Autowired
    private PityCounter pity;

    @AfterEach
    void tearDown() {
        pity.reset();
    }

    @Test
    void shouldGetValueOfSSRCounter() {
        pity.set(PityCounter.SSR, 1);
        Assertions.assertEquals(1, pity.getSSR());
    }

    @Test
    void shouldGetValueOfSRCounter() {
        pity.set(PityCounter.SR, 1);
        Assertions.assertEquals(1, pity.getSR());
    }

    @Test
    void shouldGetValueOfHitCounter() {
        pity.set(PityCounter.HIT, 1);
        Assertions.assertEquals(1, pity.getHit());
    }

    @Test
    void shouldGetValueOfLostCounter() {
        pity.set(PityCounter.LOST, 1);
        Assertions.assertEquals(1, pity.getLost());
    }

    @Test
    void shouldGetValueOfWonCounter() {
        pity.set(PityCounter.WON, 1);
        Assertions.assertEquals(1, pity.getWon());
    }

    @Test
    void shouldGetValueOfLostInARowCounter() {
        pity.set(PityCounter.LOST_IN_A_ROW, 1);
        Assertions.assertEquals(1, pity.getLostInARow());
    }

    @Test
    void shouldGetValueOfWonInARowCounter() {
        pity.set(PityCounter.WON_IN_A_ROW, 1);
        Assertions.assertEquals(1, pity.getWonInARow());
    }

    @CsvSource({
            "80,true",
            "79,false"
    })
    @ParameterizedTest
    void shouldBeSSRWeaponPity(final int amount, final boolean result) {
        pity.set(PityCounter.SSR, amount);
        Assertions.assertEquals(result, pity.isSsrWeaponPity());
    }

    @CsvSource({
            "40,true",
            "39,false"
    })
    @ParameterizedTest
    void shouldBeSSRMatrixPity(final int amount, final boolean result) {
        pity.set(PityCounter.SSR, amount);
        Assertions.assertEquals(result, pity.isSsrMatrixPity());
    }

    @CsvSource({
            "10,79,true",
            "10,80,true",
            "9,79,true",
            "9,80,false",
    })
    @ParameterizedTest
    void shouldBeSSRWeaponPity(final int srAmount, final int ssrAmount, final boolean result) {
        pity.set(PityCounter.SR, srAmount);
        pity.set(PityCounter.SSR, ssrAmount);
        Assertions.assertEquals(result, pity.isSrWeaponPity());
    }

    @CsvSource({
            "10,39,true",
            "10,40,true",
            "9,39,true",
            "9,40,false",
    })
    @ParameterizedTest
    void shouldBeSSRMatrixPity(final int srAmount, final int ssrAmount, final boolean result) {
        pity.set(PityCounter.SR, srAmount);
        pity.set(PityCounter.SSR, ssrAmount);
        Assertions.assertEquals(result, pity.isSrMatrixPity());
    }

    @Test
    void shouldIncrementBothSSRAndSR() {
        pity.increment();
        Assertions.assertEquals(1, pity.getSSR());
        Assertions.assertEquals(1, pity.getSR());
    }

    @Test
    void shouldResetTheCounters() {
        pity.set(PityCounter.LOST, 10);
        pity.set(PityCounter.WON, 10);

        pity.reset();
        Assertions.assertEquals(0, pity.getLost());
        Assertions.assertEquals(0, pity.getWon());
    }
}
