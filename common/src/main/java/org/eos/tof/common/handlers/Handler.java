package org.eos.tof.common.handlers;

import lombok.NoArgsConstructor;
import org.eos.tof.common.Banner;

/**
 * @author Eos
 */
@NoArgsConstructor
public abstract class Handler {

    private Handler next;

    /**
     * Link a chain for handlers together.
     *
     * @param first The first handler to be executed.
     * @param chain The rest of the chain to be added sequentially.
     * @return The first handler with the configured chain.
     */
    public static Handler link(final Handler first, final Handler... chain) {
        Handler head = first;
        for (Handler nextInChain : chain) {
            head.next = nextInChain;
            head = nextInChain;
        }

        return first;
    }

    /**
     * Executes the current handler.
     *
     * @param banner The banner to execute the current handler on.
     * @return Whenever the handler has executed successfully.
     */
    public abstract boolean check(final Banner banner);

    /**
     * Executes the next handler in the chain if possible, otherwise return `true`.
     *
     * @param banner The banner to execute the current handler on.
     * @return Whenever the handler has executed successfully.
     */
    protected boolean checkNext(final Banner banner) {
        if (next == null) {
            return true;
        }

        return next.check(banner);
    }
}

