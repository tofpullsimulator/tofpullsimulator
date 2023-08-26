package org.eos.tof.common.handlers.weapons;

import java.util.random.RandomGenerator;

import lombok.AllArgsConstructor;
import org.eos.tof.common.Banner;
import org.eos.tof.common.counters.PityCounter;
import org.eos.tof.common.handlers.Handler;
import org.eos.tof.common.handlers.SSRareHelper;
import org.eos.tof.common.items.Normal;
import org.eos.tof.common.items.Rare;
import org.eos.tof.common.items.SRare;
import org.springframework.stereotype.Component;

/**
 * Handler for processing the banner during pulling on weapons.
 *
 * @author Eos
 */
@AllArgsConstructor
@Component("weaponPullHandler")
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
        var pity = banner.pity();
        if (pity.get(PityCounter.SSR) == 0 || pity.get(PityCounter.SR) == 0) {
            return checkNext(banner);
        }

        var rate = banner.rate();
        if (rate == Banner.RateMode.WEAPON_NORMAL) {
            return normal(banner);
        }

        return guarantee(banner);
    }

    private boolean normal(final Banner banner) {
        @SuppressWarnings("DuplicatedCode")
        var mode = banner.rate();
        var history = banner.history();

        double n = rng.nextDouble(100);
        n -= mode.getSsr();
        if (n <= 0) {
            helper.pullWeapon(banner);
            return checkNext(banner);
        }

        n -= mode.getSr();
        if (n <= 0) {
            history.add(new SRare(rng));
            return checkNext(banner);
        }

        n -= mode.getN();
        if (n <= 0) {
            history.add(new Normal());
            return checkNext(banner);
        }

        n -= mode.getR();
        if (n <= 0) {
            history.add(new Rare(rng));
            return checkNext(banner);
        }

        return false;
    }

    @SuppressWarnings("DuplicatedCode")
    private boolean guarantee(final Banner banner) {
        var mode = banner.rate();
        var history = banner.history();

        double n = rng.nextDouble(100);
        n -= mode.getSsr();
        if (n <= 0) {
            helper.pullWeapon(banner);
            return checkNext(banner);
        }

        n -= mode.getN();
        if (n <= 0) {
            history.add(new Normal());
            return checkNext(banner);
        }

        n -= mode.getSr();
        if (n <= 0) {
            history.add(new SRare(rng));
            return checkNext(banner);
        }

        n -= mode.getR();
        if (n <= 0) {
            history.add(new Rare(rng));
            return checkNext(banner);
        }

        return false;
    }
}
