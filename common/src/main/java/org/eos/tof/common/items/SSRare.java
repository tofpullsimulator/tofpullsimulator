package org.eos.tof.common.items;

import java.util.random.RandomGenerator;
import java.util.Set;

import lombok.Getter;

/**
 * A super-super rare rarity item.
 *
 * @author Eos
 */
@Getter
public class SSRare implements SSRItem {

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
    /**
     * A list op standard SSR options.
     */
    protected static final String[] OPTIONS_LIST = OPTIONS.toArray(String[]::new);

    private final String name;

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
     * {@inheritDoc}
     */
    @Override
    public boolean isStandard() {
        return OPTIONS.contains(this.name);
    }
}
