package org.eos.tof.common;

import java.util.ArrayList;
import java.util.List;

import lombok.NoArgsConstructor;
import org.eos.tof.common.items.Item;
import org.springframework.stereotype.Component;

/**
 * History containing a stack of pulled items.
 *
 * @author Eos
 */
@NoArgsConstructor
@Component(value = "history")
public class History {

    private final List<Item> pulled = new ArrayList<>();

    /**
     * Add an item to the history stack.
     *
     * @param item The item to add to the history stack.
     */
    public void add(final Item item) {
        pulled.add(item);
    }

    /**
     * Get the current history stack.
     *
     * @return A list of item representing the history stack.
     */
    public List<Item> get() {
        return pulled;
    }

    /**
     * Get the last pushed item from the history stack.
     *
     * @return The last pushed item or null if the history stack is empty
     */
    public Item getLast() {
        if (pulled.isEmpty()) {
            return null;
        }

        return pulled.get(pulled.size() - 1);
    }

    /**
     * Resets the history stack.
     */
    public void reset() {
        pulled.clear();
    }
}
