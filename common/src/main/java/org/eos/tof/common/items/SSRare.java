package org.eos.tof.common.items;

import java.util.Random;
import java.util.Set;
import java.util.random.RandomGenerator;

import lombok.Getter;

/**
 * A super-super rare rarity item.
 *
 * @author Eos
 */
@Getter
public class SSRare implements Item {

    private static final Set<String> OPTIONS = Set.of(
            "Rosy Edge",
            "Icewind Arrow",
            "Absolute Zero",
            "Scythe of the Crow",
            "Chakram of the Seas",
            "Negating Cube",
            "Dual EM Stars",
            "Molten Shield V2",
            "Thunderblades"
    );
    private static final String[] OPTIONS_LIST = OPTIONS.toArray(String[]::new);

    private final String name;

    /**
     * Create a new super-super rare rarity item.
     */
    public SSRare() {
        this(new Random());
    }

    /**
     * Create a new super-super rare rarity item.
     *
     * @param rng Random generator to get a random super-super rare item
     */
    public SSRare(final RandomGenerator rng) {
        int n = rng.nextInt(OPTIONS.size());
        this.name = OPTIONS_LIST[n];
    }

    /**
     * Create a new super-super rare rarity item.
     *
     * @param name The name of the super-super rare item.
     */
    public SSRare(final String name) {
        this.name = name;
    }

    /**
     * Checks if the current item is a standard weapon or not.
     *
     * @return Standard if the standard pool contains the current weapons' name.
     */
    public boolean isStandard() {
        return OPTIONS.contains(this.name);
    }
}
