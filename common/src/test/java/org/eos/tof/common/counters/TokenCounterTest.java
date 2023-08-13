package org.eos.tof.common.counters;

import java.lang.reflect.Constructor;

import org.eos.tof.common.History;
import org.eos.tof.common.items.Item;
import org.eos.tof.common.items.Normal;
import org.eos.tof.common.items.Rare;
import org.eos.tof.common.items.SRare;
import org.eos.tof.common.items.SSRare;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {History.class, TokenCounter.class})
class TokenCounterTest {

    @Autowired
    private History history;
    @Autowired
    private TokenCounter tokens;

    @AfterEach
    void tearDown() {
        history.reset();
        tokens.reset();
    }

    @Test
    void shouldNotIncrementWhenSSRIsGiven() {
        history.add(new SSRare("weaponsName"));
        tokens.increment();
        Assertions.assertEquals(0, tokens.get());
    }

    @Test
    void shouldIncrementWhenSSRIsGiven() {
        history.add(new SSRare());
        tokens.increment();
        Assertions.assertEquals(10, tokens.get());
    }

    @Test
    void shouldIncrementWhenSRIsGiven() {
        history.add(new SRare());
        tokens.increment();
        Assertions.assertEquals(2, tokens.get());
    }

    @ValueSource(classes = {Rare.class, Normal.class})
    @ParameterizedTest
    void shouldIncrementWhenOtherIsGiven(final Class<?> clazz) throws Exception {
        Constructor<?> ctor = clazz.getConstructor();
        Item object = (Item) ctor.newInstance();
        history.add(object);

        tokens.increment();
        Assertions.assertEquals(1, tokens.get());
    }

    @Test
    void shouldResetTheTokens() {
        tokens.set(10);
        Assertions.assertEquals(10, tokens.get());

        tokens.reset();
        Assertions.assertEquals(0, tokens.get());
    }
}
