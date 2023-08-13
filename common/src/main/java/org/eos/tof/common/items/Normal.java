package org.eos.tof.common.items;

import lombok.Getter;

/**
 * A normal rarity item.
 *
 * @author Eos
 */
@Getter
public class Normal implements Item {

    private final String name;

    /**
     * Create a new normal rarity item.
     */
    public Normal() {
        this.name = "Weapon Battery III";
    }
}
