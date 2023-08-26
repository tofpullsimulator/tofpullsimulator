package org.eos.tof.common.handlers.weapons;

import lombok.AllArgsConstructor;
import org.eos.tof.common.Banner;
import org.eos.tof.common.counters.StatisticsCounter;
import org.eos.tof.common.counters.TokenCounter;
import org.eos.tof.common.handlers.Handler;
import org.springframework.stereotype.Component;

/**
 * Handler for processing the banner after pulling on weapons.
 *
 * @author Eos
 */
@AllArgsConstructor
@Component("weaponTokenHandler")
public class TokenHandler extends Handler {

    /**
     * Handler for the tokens and statistics. It is responsible for the following:
     * <ul>
     *   <li>Increments the token (flame gold) counter. Including subtracting the tokens if a banner weapon can be bought.</li>
     *   <li>Increments the statistics counter.</li>
     * </ul>
     *
     * @param banner The banner to execute the current handler on.
     * @return Whenever the handler has executed successfully.
     */
    @Override
    public boolean check(final Banner banner) {
        var statistics = banner.statistics();
        var tokens = banner.tokens();

        statistics.increment();
        tokens.increment(TokenCounter.WEAPON_TOKENS);

        int t = tokens.get(TokenCounter.WEAPON_TOKENS);
        if (t >= 120) {
            tokens.set(TokenCounter.WEAPON_TOKENS, t - 120);
            statistics.increment(StatisticsCounter.BANNER_WEAPON);
        }

        return checkNext(banner);
    }
}
