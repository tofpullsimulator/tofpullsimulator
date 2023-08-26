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

    private static final Set<String> OPTIONS = Set.of("Frosted Spear", "Combat Blade", "EM Blade", "Composite Bow");
    private static final String[] OPTIONS_LIST = OPTIONS.toArray(String[]::new);

    private final String name;

    /**
     * Create a new rare rarity item.
     *
     * @param rng Random generator to get a random rare item
     */
    public Rare(final RandomGenerator rng) {
        int n = rng.nextInt(OPTIONS.size());
        this.name = OPTIONS_LIST[n];
    }
}
