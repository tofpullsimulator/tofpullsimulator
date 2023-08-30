package org.eos.tof.common.counters;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.AllArgsConstructor;
import org.eos.tof.common.History;
import org.eos.tof.common.items.SRare;
import org.eos.tof.common.items.SSRare;
import org.springframework.stereotype.Component;

/**
 * Counter object for handling tokens (flame gold).
 *
 * @author Eos
 */
@AllArgsConstructor
@Component(value = "tokens")
public class TokenCounter implements Counter {

    /**
     * Metric name for matrix tokens in the banner.
     */
    public static final String MATRIX_TOKENS = "matrix_tokens";
    /**
     * Metric name for the amount of brain pieces to buy with tokens in the banner.
     */
    public static final String BUY_BRAIN_PIECES = "buy_brain_pieces";
    /**
     * Metric name for the amount of hands pieces to buy with tokens in the banner.
     */
    public static final String BUY_HANDS_PIECES = "buy_hands_pieces";
    /**
     * Metric name for the amount of head pieces to buy with tokens in the banner.
     */
    public static final String BUY_HEAD_PIECES = "buy_head_pieces";
    /**
     * Metric name for the amount of heart pieces to buy with tokens in the banner.
     */
    public static final String BUY_HEART_PIECES = "buy_heart_pieces";
    /**
     * Metric name for weapon tokens in the banner.
     */
    public static final String WEAPON_TOKENS = "weapon_tokens";

    private final Map<String, AtomicInteger> store = Map.ofEntries(
            Map.entry(MATRIX_TOKENS, new AtomicInteger(0)),
            Map.entry(BUY_BRAIN_PIECES, new AtomicInteger(0)),
            Map.entry(BUY_HANDS_PIECES, new AtomicInteger(0)),
            Map.entry(BUY_HEAD_PIECES, new AtomicInteger(0)),
            Map.entry(BUY_HEART_PIECES, new AtomicInteger(0)),
            Map.entry(WEAPON_TOKENS, new AtomicInteger(0))
    );
    private final History history;

    /**
     * Get the value of {@link #MATRIX_TOKENS} from the counter.
     *
     * @return The value of {@link #MATRIX_TOKENS}.
     */
    public Integer getMatrixTokens() {
        return get(MATRIX_TOKENS);
    }

    /**
     * Get the value of {@link #BUY_BRAIN_PIECES} from the counter.
     *
     * @return The value of {@link #BUY_BRAIN_PIECES}.
     */
    public Integer getBuyBrainPieces() {
        return get(BUY_BRAIN_PIECES);
    }

    /**
     * Get the value of {@link #BUY_HANDS_PIECES} from the counter.
     *
     * @return The value of {@link #BUY_HANDS_PIECES}.
     */
    public Integer getBuyHandsPieces() {
        return get(BUY_HANDS_PIECES);
    }

    /**
     * Get the value of {@link #BUY_HEAD_PIECES} from the counter.
     *
     * @return The value of {@link #BUY_HEAD_PIECES}.
     */
    public Integer getBuyHeadPieces() {
        return get(BUY_HEAD_PIECES);
    }

    /**
     * Get the value of {@link #BUY_HEART_PIECES} from the counter.
     *
     * @return The value of {@link #BUY_HEART_PIECES}.
     */
    public Integer getBuyHeartPieces() {
        return get(BUY_HEART_PIECES);
    }

    /**
     * Get the value of {@link #WEAPON_TOKENS} from the counter.
     *
     * @return The value of {@link #WEAPON_TOKENS}.
     */
    public Integer getWeaponTokens() {
        return get(WEAPON_TOKENS);
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
    public void increment() {
        throw new UnsupportedOperationException();
    }

    /**
     * Increments the token counter based on the last item in the history stack.
     *
     * @param metricName The metric name of the counter.
     * @see History
     */
    @Override
    public void increment(final String metricName) {
        if (Objects.equals(metricName, MATRIX_TOKENS)) {
            set(MATRIX_TOKENS, get(MATRIX_TOKENS) + 1);
            return;
        }

        var last = history.getLast();
        int amount = 1;
        if (last instanceof SRare) {
            amount = 2;
        } else if (last instanceof SSRare item) {
            amount = item.isStandard() ? 10 : 0;
        }

        set(WEAPON_TOKENS, get(WEAPON_TOKENS) + amount);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reset(String metricName) {
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
