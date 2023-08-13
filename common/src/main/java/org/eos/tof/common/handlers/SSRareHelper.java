package org.eos.tof.common.handlers;

import java.util.random.RandomGenerator;

import lombok.AllArgsConstructor;
import org.eos.tof.common.Banner;
import org.eos.tof.common.counters.PityCounter;
import org.eos.tof.common.counters.StatisticsCounter;
import org.eos.tof.common.items.SSRare;
import org.springframework.stereotype.Component;

/**
 * Helper object for when pulling an SSR weapon.
 *
 * @author Eos
 */
@AllArgsConstructor
@Component
public class SSRareHelper {

    private final RandomGenerator rng;

    /**
     * Pulls an SSR weapon for the banner.
     *
     * @param banner The banner to execute the current handler on.
     */
    public void pull(final Banner banner) {
        var pity = banner.getPity();

        var result = new SSRare();
        int lostInARow = pity.get(PityCounter.LOST_IN_A_ROW);
        int wonInARow = pity.get(PityCounter.WON_IN_A_ROW);
        if (banner.isTheory() && lostInARow >= 2) {
            result = new SSRare(banner.getSpec().getWeapon());
        } else if (banner.isTheory() && wonInARow >= 2) {
            result = new SSRare();
        } else if (rng.nextBoolean()) {
            result = new SSRare(banner.getSpec().getWeapon());
        }

        banner.getHistory().add(result);
        if (result.isStandard()) {
            pity.increment(PityCounter.LOST);
            pity.increment(PityCounter.LOST_IN_A_ROW);
            pity.reset(PityCounter.WON_IN_A_ROW);
            return;
        }

        pity.increment(PityCounter.WON);
        pity.increment(PityCounter.WON_IN_A_ROW);
        pity.reset(PityCounter.LOST_IN_A_ROW);
        banner.getStatistics().increment(StatisticsCounter.BANNER_WEAPON);
    }
}
