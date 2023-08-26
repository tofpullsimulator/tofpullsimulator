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
     * Get the value of {@link #SSR} from the counter.
     *
     * @return The value of {@link #SSR}.
     */
    public Integer getSSR() {
        return get(SSR);
    }

    /**
     * Get the value of {@link #SR} from the counter.
     *
     * @return The value of {@link #SR}.
     */
    public Integer getSR() {
        return get(SR);
    }

    /**
     * Get the value of {@link #HIT} from the counter.
     *
     * @return The value of {@link #HIT}.
     */
    public Integer getHit() {
        return get(HIT);
    }

    /**
     * Get the value of {@link #LOST} from the counter.
     *
     * @return The value of {@link #LOST}.
     */
    public Integer getLost() {
        return get(LOST);
    }

    /**
     * Get the value of {@link #WON} from the counter.
     *
     * @return The value of {@link #WON}.
     */
    public Integer getWon() {
        return get(WON);
    }

    /**
     * Get the value of {@link #LOST_IN_A_ROW} from the counter.
     *
     * @return The value of {@link #LOST_IN_A_ROW}.
     */
    public Integer getLostInARow() {
        return get(LOST_IN_A_ROW);
    }

    /**
     * Get the value of {@link #WON_IN_A_ROW} from the counter.
     *
     * @return The value of {@link #WON_IN_A_ROW}.
     */
    public Integer getWonInARow() {
        return get(WON_IN_A_ROW);
    }

    /**
     * Check if the pity is to hit for SSR rarity for a weapon banner.
     *
     * @return True if the {@link #SSR} counter is 80.
     */
    public boolean isSsrWeaponPity() {
        return get(SSR) == 80;
    }

    /**
     * Check if the pity is to hit for SSR rarity for a matrix banner.
     *
     * @return True if the {@link #SSR} counter is 40.
     */
    public boolean isSsrMatrixPity() {
        return get(SSR) == 40;
    }

    /**
     * Check if the pity is to hit for SR rarity for a weapon banner.
     *
     * @return True if the {@link #SR} counter is 10 or {@link #SSR} counter is 79.
     */
    public boolean isSrWeaponPity() {
        return getSR() == 10 || getSSR() == 79;
    }

    /**
     * Check if the pity is to hit for SR rarity for a matrix banner.
     *
     * @return True if the {@link #SR} counter is 10 or {@link #SSR} counter is 39.
     */
    public boolean isSrMatrixPity() {
        return getSR() == 10 || getSSR() == 39;
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
