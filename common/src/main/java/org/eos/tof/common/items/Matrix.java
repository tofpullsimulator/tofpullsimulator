package org.eos.tof.common.items;

import lombok.Getter;

import java.util.Set;
import java.util.random.RandomGenerator;

/**
 * A matrix item.
 *
 * @author Eos
 */
@Getter
public class Matrix implements SSRItem {

    /**
     * Available corner pieces for matrices.
     *
     * @author Eos
     */
    public enum Pieces {
        /**
         * The brain piece.
         */
        BRAIN,
        /**
         * The hands piece.
         */
        HANDS,
        /**
         * The head piece.
         */
        HEAD,
        /**
         * The heart piece.
         */
        HEART;

        /**
         * Return the human-readable name of the matrix piece.
         *
         * @return The human-readable name of the matrix piece.
         */
        @Override
        public String toString() {
            return this.name().substring(0, 1).toUpperCase() + this.name().substring(1).toLowerCase();
        }
    }

    private static final Set<String> OPTIONS = Set.of(
            "Pure White Guardian",
            "Eye of Wind",
            "Proof of Trust",
            "Advocate of Violence",
            "Poem of Departure",
            "Incredible Connection",
            "Rational Liberation",
            "Slightly Awakened",
            "Fanatical Pursuit"
    );
    /**
     * A list op standard SSR options.
     */
    protected static final String[] OPTIONS_LIST = OPTIONS.toArray(String[]::new);

    private final String name;
    private final Pieces position;

    /**
     * Create a new matrix item.
     *
     * @param rng Random generator to get a random matrix item.
     */
    public Matrix(final RandomGenerator rng) {
        int n = rng.nextInt(OPTIONS.size());
        this.name = OPTIONS_LIST[n];
        int m = rng.nextInt(Pieces.values().length);
        this.position = Pieces.values()[m];
    }

    /**
     * Create a new matrix item.
     *
     * @param name    The name of the matrix item.
     * @param rng     Random generator to get a random matrix item.
     * @param blocked The piece which is blocked.
     */
    public Matrix(final String name, final RandomGenerator rng, final Pieces blocked) {
        this.name = name;

        if (blocked == null) {
            int m = rng.nextInt(Pieces.values().length);
            this.position = Pieces.values()[m];
        } else {
            Pieces p = null;
            while (p == null || p == blocked) {
                int m = rng.nextInt(Pieces.values().length);
                p = Pieces.values()[m];
            }
            this.position = p;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isStandard() {
        return OPTIONS.contains(this.name);
    }
}
