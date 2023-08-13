package org.eos.tof.common.handlers;

import java.util.random.RandomGenerator;

import lombok.AllArgsConstructor;
import org.eos.tof.common.Banner;
import org.eos.tof.common.counters.PityCounter;
import org.eos.tof.common.items.Normal;
import org.eos.tof.common.items.Rare;
import org.eos.tof.common.items.SRare;
import org.springframework.stereotype.Component;

/**
 * Handler for processing the banner during pulling.
 *
 * @author Eos
 */
@AllArgsConstructor
@Component
public class PullHandler extends Handler {

    private final RandomGenerator rng;
    private final SSRareHelper helper;

    /**
     * Handler for the pulling. It is responsible for the following:
     * <ul>
     *   <li>Pulling a random weapon of a random rarity.</li>
     * </ul>
     *
     * @param banner The banner to execute the current handler on.
     * @return Whenever the handler has executed successfully.
     */
    @Override
    public boolean check(final Banner banner) {
        var pity = banner.getPity();
        if (pity.get(PityCounter.SSR) == 0 || pity.get(PityCounter.SR) == 0) {
            return checkNext(banner);
        }

        var rate = banner.getRate();
        if (rate == Banner.RateMode.NORMAL) {
            return normal(banner);
        }

        return guarantee(banner);
    }

    private boolean normal(final Banner banner) {
        @SuppressWarnings("DuplicatedCode")
        var mode = banner.getRate();
        var history = banner.getHistory();

        double n = rng.nextDouble(100);
        n -= mode.getSsr();
        if (n <= 0) {
            helper.pull(banner);
            return checkNext(banner);
        }

        n -= mode.getSr();
        if (n <= 0) {
            history.add(new SRare());
            return checkNext(banner);
        }

        n -= mode.getN();
        if (n <= 0) {
            history.add(new Normal());
            return checkNext(banner);
        }

        n -= mode.getR();
        if (n <= 0) {
            history.add(new Rare());
            return checkNext(banner);
        }

        return false;
    }

    private boolean guarantee(final Banner banner) {
        @SuppressWarnings("DuplicatedCode")
        var mode = banner.getRate();
        var history = banner.getHistory();

        double n = rng.nextDouble(100);
        n -= mode.getSsr();
        if (n <= 0) {
            helper.pull(banner);
            return checkNext(banner);
        }

        n -= mode.getN();
        if (n <= 0) {
            history.add(new Normal());
            return checkNext(banner);
        }

        n -= mode.getSr();
        if (n <= 0) {
            history.add(new SRare());
            return checkNext(banner);
        }

        n -= mode.getR();
        if (n <= 0) {
            history.add(new Rare());
            return checkNext(banner);
        }

        return false;
    }
}

