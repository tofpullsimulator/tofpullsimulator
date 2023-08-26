package org.eos.tof.common.items;

/**
 * A super-super rare rarity item interface.
 *
 * @author Eos
 */
public interface SSRItem extends Item {

    /**
     * Checks if the current item is a standard weapon or not.
     *
     * @return Standard if the standard pool contains the current weapons' name.
     */
    boolean isStandard();
}
