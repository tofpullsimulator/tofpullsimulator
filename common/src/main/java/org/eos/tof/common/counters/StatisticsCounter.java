package org.eos.tof.common.counters;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.Map;

import lombok.AllArgsConstructor;
import org.eos.tof.common.History;
import org.eos.tof.common.items.Normal;
import org.eos.tof.common.items.Rare;
import org.eos.tof.common.items.SRare;
import org.eos.tof.common.items.SSRare;
import org.springframework.stereotype.Component;

/**
 * Counter object for handling statistics of the banner.
 *
 * @author Eos
 */
@AllArgsConstructor
@Component(value = "statistics")
public class StatisticsCounter implements Counter {

    /**
     * Metric name for SSR gotten in the banner.
     */
    public static final String SSR = SSRare.class.getSimpleName();
    /**
     * Metric name for SR gotten in the banner.
     */
    public static final String SR = SRare.class.getSimpleName();
    /**
     * Metric name for rare gotten in the banner.
     */
    public static final String R = Rare.class.getSimpleName();
    /**
     * Metric name for normal items gotten in the banner.
     */
    public static final String N = Normal.class.getSimpleName();
    /**
     * Metric name for limited weapons gotten in the banner.
     */
    public static final String BANNER_WEAPON = "banner_weapon";

    private final Map<String, AtomicInteger> store = Map.ofEntries(
            Map.entry(SSR, new AtomicInteger(0)),
            Map.entry(SR, new AtomicInteger(0)),
            Map.entry(R, new AtomicInteger(0)),
            Map.entry(N, new AtomicInteger(0)),
            Map.entry(BANNER_WEAPON, new AtomicInteger(0))
    );
    private final History history;

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
     * Get the value of {@link #R} from the counter.
     *
     * @return The value of {@link #R}.
     */
    public Integer getRare() {
        return get(R);
    }

    /**
     * Get the value of {@link #N} from the counter.
     *
     * @return The value of {@link #N}.
     */
    public Integer getNormal() {
        return get(N);
    }

    /**
     * Get the value of {@link #BANNER_WEAPON} from the counter.
     *
     * @return The value of {@link #BANNER_WEAPON}.
     */
    public Integer getWeaponBanner() {
        return get(BANNER_WEAPON);
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
     * Increments the different rarity counters based on the last item in the history stack.
     *
     * @see History
     */
    @Override
    public void increment() {
        var last = history.getLast();
        if (last instanceof Normal) {
            increment(N);
        } else if (last instanceof Rare) {
            increment(R);
        } else if (last instanceof SRare) {
            increment(SR);
        } else if (last instanceof SSRare) {
            increment(SSR);
        }
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
