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

    private static final Set<String> OPTIONS = Set.of(
            "Staff of Scars",
            "The Terminator",
            "Pummeler",
            "Nightngale's Feather",
            "Thunderous Halberd"
    );
    private static final String[] OPTIONS_LIST = OPTIONS.toArray(String[]::new);

    private final String name;

    /**
     * Create a new super rare rarity item.
     *
     * @param rng Random generator to get a random super rare item
     */
    public SRare(final RandomGenerator rng) {
        int n = rng.nextInt(OPTIONS.size());
        this.name = OPTIONS_LIST[n];
    }
}
