package org.eos.tof.common.handlers;

import lombok.AllArgsConstructor;
import org.eos.tof.common.Banner;
import org.eos.tof.common.counters.PityCounter;
import org.eos.tof.common.items.SRare;
import org.springframework.stereotype.Component;

/**
 * Handler for processing the banner before pulling.
 *
 * @author Eos
 */
@AllArgsConstructor
@Component
public class PityHandler extends Handler {

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
        var pity = banner.getPity();
        pity.increment();

        if (pity.isSsrPity()) {
            pity.reset(PityCounter.SSR);
            pity.increment(PityCounter.HIT);
            helper.pull(banner);
        } else if (pity.isSrPity()) {
            pity.reset(PityCounter.SR);
            banner.getHistory().add(new SRare());
        }

        return checkNext(banner);
    }
}

