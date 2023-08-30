package org.eos.tof.common.items;

import java.util.random.RandomGenerator;
import java.util.Set;

import lombok.Getter;
import lombok.ToString;

/**
 * A super rare rarity item.
 *
 * @author Eos
 */
@Getter
@ToString
public class SRare implements Item {

    private static final Set<String> MATRIX_OPTIONS = Set.of(
            "Apophis",
            "Bai Ling",
            "Barbarossa",
            "Echo",
            "Ene",
            "Frost Bot",
            "Hilda",
            "Pepper",
            "Robarg"
    );
    private static final String[] MATRIX_OPTIONS_LIST = MATRIX_OPTIONS.toArray(String[]::new);
    private static final Set<String> WEAPON_OPTIONS = Set.of(
            "Staff of Scars",
            "The Terminator",
            "Pummeler",
            "Nightingale's Feather",
            "Thunderous Halberd"
    );
    private static final String[] WEAPON_OPTIONS_LIST = WEAPON_OPTIONS.toArray(String[]::new);

    private final String name;

    /**
     * Create a new super rare rarity item.
     *
     * @param rng Random generator to get a random super rare item.
     */
    public SRare(final RandomGenerator rng) {
        this(rng, false);
    }

    /**
     * Create a new super rare rarity item.
     *
     * @param rng      Random generator to get a random super rare item.
     * @param isMatrix Whenever the item is a matrix or not.
     */
    public SRare(final RandomGenerator rng, final boolean isMatrix) {
        if (isMatrix) {
            int n = rng.nextInt(MATRIX_OPTIONS.size());
            this.name = MATRIX_OPTIONS_LIST[n];
        } else {
            int n = rng.nextInt(WEAPON_OPTIONS.size());
            this.name = WEAPON_OPTIONS_LIST[n];
        }
    }
}
