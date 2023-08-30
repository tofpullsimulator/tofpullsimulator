package org.eos.tof.common.items;

import java.util.random.RandomGenerator;
import java.util.Set;

import lombok.Getter;

/**
 * A rare rarity item.
 *
 * @author Eos
 */
@Getter
public class Rare implements Item {

    private static final Set<String> MATRIX_OPTIONS = Set.of(
            "Functional Dash",
            "Obstacle Removal",
            "Plunder",
            "Self-explosive",
            "Standard Operation",
            "Wind Blade"
    );
    private static final String[] MATRIX_OPTIONS_LIST = MATRIX_OPTIONS.toArray(String[]::new);
    private static final Set<String> WEAPON_OPTIONS = Set.of(
            "Frosted Spear",
            "Combat Blade",
            "EM Blade",
            "Composite Bow"
    );
    private static final String[] WEAPON_OPTIONS_LIST = WEAPON_OPTIONS.toArray(String[]::new);

    private final String name;

    /**
     * Create a new rare rarity item.
     *
     * @param rng      Random generator to get a random rare item
     */
    public Rare(final RandomGenerator rng) {
        this(rng, false);
    }

    /**
     * Create a new rare rarity item.
     *
     * @param rng      Random generator to get a random rare item
     * @param isMatrix Whenever the item is a matrix or not.
     */
    public Rare(final RandomGenerator rng, final boolean isMatrix) {
        if (isMatrix) {
            int n = rng.nextInt(MATRIX_OPTIONS.size());
            this.name = MATRIX_OPTIONS_LIST[n];
        } else {
            int n = rng.nextInt(WEAPON_OPTIONS.size());
            this.name = WEAPON_OPTIONS_LIST[n];
        }
    }
}
