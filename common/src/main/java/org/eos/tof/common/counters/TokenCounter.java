package org.eos.tof.common.counters;

import java.util.concurrent.atomic.AtomicInteger;

import lombok.AllArgsConstructor;
import org.eos.tof.common.History;
import org.eos.tof.common.items.SRare;
import org.eos.tof.common.items.SSRare;
import org.springframework.stereotype.Component;

/**
 * Counter object for handling tokens (black gold).
 *
 * @author Eos
 */
@AllArgsConstructor
@Component(value = "tokens")
public class TokenCounter implements Counter {

    private final AtomicInteger store = new AtomicInteger(0);
    private final History history;

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer get(final String metricName) {
        return store.get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void set(final String metricName, final int newValue) {
        store.set(newValue);
    }

    /**
     * Increments the token counter based on the last item in the history stack.
     *
     * @param metricName The metric name of the counter.
     * @see History
     */
    @Override
    public void increment(final String metricName) {
        var last = history.getLast();
        int amount = 1;
        if (last instanceof SRare) {
            amount = 2;
        } else if (last instanceof SSRare item) {
            amount = item.isStandard() ? 10 : 0;
        }

        set(store.get() + amount);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reset(final String metricName) {
        store.set(0);
    }
}
