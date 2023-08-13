package org.eos.tof.common.counters;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.Map;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Counter object for handling the pity of the banner.
 *
 * @author Eos
 */
@NoArgsConstructor
@Component(value = "pity")
public class PityCounter implements Counter {

    /**
     * Metric name for SSR pity.
     */
    public static final String SSR = "ssr";
    /**
     * Metric name for SR pity.
     */
    public static final String SR = "sr";
    /**
     * Metric name for hard pity hit.
     */
    public static final String HIT = "hit";
    /**
     * Metric name for pity won in total.
     */
    public static final String LOST = "lost";
    /**
     * Metric name for pity won in total.
     */
    public static final String WON = "won";
    /**
     * Metric name for pity lost in a row.
     */
    public static final String LOST_IN_A_ROW = "lost_in_a_row";
    /**
     * Metric name for pity won in a row.
     */
    public static final String WON_IN_A_ROW = "won_in_a_row";

    private final Map<String, AtomicInteger> store = Map.ofEntries(
            Map.entry(SR, new AtomicInteger(0)),
            Map.entry(SSR, new AtomicInteger(0)),
            Map.entry(HIT, new AtomicInteger(0)),
            Map.entry(LOST, new AtomicInteger(0)),
            Map.entry(WON, new AtomicInteger(0)),
            Map.entry(LOST_IN_A_ROW, new AtomicInteger(0)),
            Map.entry(WON_IN_A_ROW, new AtomicInteger(0))
    );

    /**
     * Check if the pity is to hit for SSR rarity.
     *
     * @return True if the {@see PityCounter.SSR} is 80.
     */
    public boolean isSsrPity() {
        return get(SSR) == 80;
    }

    /**
     * Check if the pity is to hit for SR rarity.
     *
     * @return True if the {@see PityCounter.SR} is 10 or {@see PityCounter.SSR} is 79.
     */
    public boolean isSrPity() {
        return get(SR) == 10 || store.get(SSR).get() == 79;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer get() {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer get(final String metricName) {
        return store.get(metricName).get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void set(final String metricName, final int newValue) {
        store.get(metricName).set(newValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void increment(final String metricName) {
        store.get(metricName).incrementAndGet();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void increment() {
        increment(SR);
        increment(SSR);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reset(final String metricName) {
        store.get(metricName).set(0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reset() {
        for (String key : store.keySet()) {
            reset(key);
        }
    }
}
