package org.eos.tof.common.handlers.matrices;

import java.util.random.RandomGenerator;

import lombok.AllArgsConstructor;
import org.eos.tof.common.Banner;
import org.eos.tof.common.counters.PityCounter;
import org.eos.tof.common.handlers.Handler;
import org.eos.tof.common.handlers.SSRareHelper;
import org.eos.tof.common.items.Rare;
import org.eos.tof.common.items.SRare;
import org.springframework.stereotype.Component;

/**
 * Handler for processing the banner during pulling on matrices.
 *
 * @author Eos
 */
@AllArgsConstructor
@Component("matrixPullHandler")
public class PullHandler extends Handler {

    private final RandomGenerator rng;
    private final SSRareHelper helper;

    /**
     * Handler for the pulling. It is responsible for the following:
     * <ul>
     *   <li>Pulling a random matrix of a random rarity.</li>
     * </ul>
     *
     * @param banner The banner to execute the current handler on.
     * @return Whenever the handler has executed successfully.
     */
    @SuppressWarnings("DuplicatedCode")
    @Override
    public boolean check(final Banner banner) {
        var pity = banner.pity();
        if (pity.get(PityCounter.SSR) == 0 || pity.get(PityCounter.SR) == 0) {
            return checkNext(banner);
        }

        var mode = banner.rate();
        var history = banner.history();

        double n = rng.nextDouble(100);
        n -= mode.getSsr();
        if (n <= 0) {
            helper.pullMatrix(banner);
            return checkNext(banner);
        }

        n -= mode.getSr();
        if (n <= 0) {
            history.add(new SRare(rng, true));
            return checkNext(banner);
        }

        n -= mode.getR();
        if (n <= 0) {
            history.add(new Rare(rng, true));
            return checkNext(banner);
        }

        return false;
    }
}

