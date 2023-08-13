package org.eos.tof.common.handlers;

import lombok.AllArgsConstructor;
import org.eos.tof.common.Banner;
import org.eos.tof.common.counters.StatisticsCounter;
import org.springframework.stereotype.Component;

/**
 * Handler for processing the banner after pulling.
 *
 * @author Eos
 */
@AllArgsConstructor
@Component
public class TokenHandler extends Handler {

    /**
     * Handler for the tokens and statistics. It is responsible for the following:
     * <ul>
     *   <li>Increments the token (black gold) counter. Including subtracting the tokens if a banner weapon can be bought.</li>
     *   <li>Increments the statistics counter.</li>
     * </ul>
     *
     * @param banner The banner to execute the current handler on.
     * @return Whenever the handler has executed successfully.
     */
    @Override
    public boolean check(final Banner banner) {
        var statistics = banner.getStatistics();
        var tokens = banner.getTokens();

        statistics.increment();
        tokens.increment();

        int t = tokens.get();
        if (t >= 120) {
            tokens.set(t - 120);
            statistics.increment(StatisticsCounter.BANNER_WEAPON);
        }

        return checkNext(banner);
    }
}
