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

import static org.mockito.Mockito.mock;

@SpringBootTest(classes = {History.class, StatisticsCounter.class})
class StatisticsCounterTest {

    @Autowired
    private History history;
    @Autowired
    private StatisticsCounter statistics;

    @AfterEach
    void tearDown() {
        history.reset();
        statistics.reset();
    }

    @Test
    void shouldGetValueOfSSRCounter() {
        statistics.set(StatisticsCounter.SSR, 1);
        Assertions.assertEquals(1, statistics.getSSR());
    }

    @Test
    void shouldGetValueOfSRCounter() {
        statistics.set(StatisticsCounter.SR, 1);
        Assertions.assertEquals(1, statistics.getSR());
    }

    @Test
    void shouldGetValueOfRareCounter() {
        statistics.set(StatisticsCounter.R, 1);
        Assertions.assertEquals(1, statistics.getRare());
    }

    @Test
    void shouldGetValueOfNormalCounter() {
        statistics.set(StatisticsCounter.N, 1);
        Assertions.assertEquals(1, statistics.getNormal());
    }

    @Test
    void shouldGetValueOfBannerWeaponCounter() {
        statistics.set(StatisticsCounter.BANNER_WEAPON, 1);
        Assertions.assertEquals(1, statistics.getWeaponBanner());
    }

    @Test
    void shouldThrowExceptionWhenGettingWithoutMetricName() {
        Assertions.assertThrows(UnsupportedOperationException.class,
                () -> statistics.get());
    }

    @ValueSource(classes = {Normal.class, Rare.class, SRare.class, SSRare.class})
    @ParameterizedTest
    void shouldIncrementBothSSRAndSR(final Class<?> clazz) throws Exception {
        Constructor<?> ctor = clazz.getConstructor();
        Item object = (Item) ctor.newInstance();
        history.add(object);

        statistics.increment();
        Assertions.assertEquals(1, statistics.get(clazz.getSimpleName()));
    }

    @Test
    void shouldNotIncrementWhenInvalidHistory() {
        Item object = mock(Item.class);
        history.add(object);

        statistics.increment();
        Assertions.assertEquals(0, statistics.get(StatisticsCounter.SSR));
        Assertions.assertEquals(0, statistics.get(StatisticsCounter.SR));
        Assertions.assertEquals(0, statistics.get(StatisticsCounter.R));
        Assertions.assertEquals(0, statistics.get(StatisticsCounter.N));
    }

    @Test
    void shouldResetTheCounters() {
        statistics.set(StatisticsCounter.SR, 10);
        statistics.set(StatisticsCounter.SSR, 10);

        statistics.reset();
        Assertions.assertEquals(0, statistics.get(StatisticsCounter.SR));
        Assertions.assertEquals(0, statistics.get(StatisticsCounter.SSR));
    }
}
