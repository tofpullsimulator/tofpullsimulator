package org.eos.tof.common.handlers.matrices;

import java.util.random.RandomGenerator;

import lombok.AllArgsConstructor;
import org.eos.tof.common.Banner;
import org.eos.tof.common.counters.PityCounter;
import org.eos.tof.common.handlers.Handler;
import org.eos.tof.common.handlers.SSRareHelper;
import org.eos.tof.common.items.SRare;
import org.springframework.stereotype.Component;

/**
 * Handler for processing the banner before pulling on matrices.
 *
 * @author Eos
 */
@AllArgsConstructor
@Component("matrixPityHandler")
public class PityHandler extends Handler {

    private final RandomGenerator rng;
    private final SSRareHelper helper;

    /**
     * Handler for the pity. It is responsible for the following:
     * <ul>
     *   <li>Increments the pity counter.</li>
     *   <li>Pull a guaranteed SR every 10 pulls.</li>
     *   <li>Pull a guaranteed SSR every 80 pulls. The SSR is either from the standard pool or a limited weapon.
     *       If the banner is using the theory you cannot lose or win 3 times in a row.</li>
     * </ul>
     *
     * @param banner The banner to execute the current handler on.
     * @return Whenever the handler has executed successfully.
     */
    @Override
    public boolean check(final Banner banner) {
        var pity = banner.pity();
        pity.increment();

        if (pity.isSsrMatrixPity()) {
            pity.reset(PityCounter.SSR);
            pity.increment(PityCounter.HIT);
            helper.pullMatrix(banner);
        } else if (pity.isSrMatrixPity()) {
            pity.reset(PityCounter.SR);
            banner.history().add(new SRare(rng));
        }

        return checkNext(banner);
    }
}
