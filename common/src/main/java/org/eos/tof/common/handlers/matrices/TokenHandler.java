package org.eos.tof.common.handlers.matrices;

import org.eos.tof.common.Banner;
import org.eos.tof.common.MatrixBanner;
import org.eos.tof.common.counters.StatisticsCounter;
import org.eos.tof.common.counters.TokenCounter;
import org.eos.tof.common.handlers.Handler;
import org.springframework.stereotype.Component;

/**
 * Handler for processing the banner after pulling on matrices.
 *
 * @author Eos
 */
@Component("matrixTokenHandler")
public class TokenHandler extends Handler {

    /**
     * Handler for the tokens and statistics. It is responsible for the following:
     * <ul>
     *   <li>Increments the token (overclocking chips) counter.</li>
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
        tokens.increment(TokenCounter.MATRIX_TOKENS);

        int t = tokens.get(TokenCounter.MATRIX_TOKENS);
        int canBuy = t / 80 + ((MatrixBanner) banner).boxes();

        canBuy = consumeTokens(tokens, TokenCounter.BUY_BRAIN_PIECES, statistics.getBrainPieces(), canBuy);
        canBuy = consumeTokens(tokens, TokenCounter.BUY_HANDS_PIECES, statistics.getHandsPieces(), canBuy);
        canBuy = consumeTokens(tokens, TokenCounter.BUY_HEAD_PIECES, statistics.getHeadPieces(), canBuy);
        consumeTokens(tokens, TokenCounter.BUY_HEART_PIECES, statistics.getHeartPieces(), canBuy);

        int brainPieces = statistics.getBrainPieces() + tokens.getBuyBrainPieces();
        int handsPieces = statistics.getHandsPieces() + tokens.getBuyHandsPieces();
        int headPieces = statistics.getHeadPieces() + tokens.getBuyHeadPieces();
        int heartPieces = statistics.getHeartPieces() + tokens.getBuyHeartPieces();

        if (brainPieces >= 4 && handsPieces >= 4 && headPieces >= 4 && heartPieces >= 4) {
            int totalPieces = brainPieces + handsPieces + headPieces + heartPieces;
            statistics.set(StatisticsCounter.TOTAL_MATRIX_PIECES, totalPieces);
        }

        return checkNext(banner);
    }

    private int consumeTokens(final TokenCounter tokens, final String metricName, final int pieces, final int canBuy) {
        if (pieces < 4 && canBuy > 0) {
            int needed = 4 - pieces;
            if (canBuy >= needed) {
                int remaining = canBuy - needed;
                tokens.set(metricName, canBuy - remaining);

                return remaining;
            } else {
                tokens.set(metricName, canBuy);
                return 0;
            }
        } else if (pieces >= 4) {
            tokens.set(metricName, 0);
        }

        return canBuy;
    }
}

