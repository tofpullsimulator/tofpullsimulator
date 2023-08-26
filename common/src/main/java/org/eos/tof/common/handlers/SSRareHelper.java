package org.eos.tof.common.handlers;

import java.util.random.RandomGenerator;

import lombok.AllArgsConstructor;
import org.eos.tof.common.Banner;
import org.eos.tof.common.counters.PityCounter;
import org.eos.tof.common.counters.StatisticsCounter;
import org.eos.tof.common.items.Matrix;
import org.eos.tof.common.items.SSRare;
import org.eos.tof.common.items.SSRItem;
import org.eos.tof.common.MatrixBanner;
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
     * Pulls an SSR matrix for the banner.
     *
     * @param banner The banner to execute the current handler on.
     */
    public void pullMatrix(final Banner banner) {
        var pity = banner.pity();

        var result = new Matrix(rng);
        int lostInARow = pity.getLostInARow();
        int wonInARow = pity.getWonInARow();
        var blocked = ((MatrixBanner) banner).blocked();
        if (banner.theory() && lostInARow >= 2) {
            result = new Matrix(banner.spec().getMatrix(), rng, blocked);
        } else if (banner.theory() && wonInARow >= 2) {
            result = new Matrix(rng);
        } else if (rng.nextBoolean()) {
            result = new Matrix(banner.spec().getMatrix(), rng, blocked);
        }

        postPull(banner, result);
    }

    /**
     * Pulls an SSR weapon for the banner.
     *
     * @param banner The banner to execute the current handler on.
     */
    public void pullWeapon(final Banner banner) {
        var pity = banner.pity();

        var result = new SSRare(rng);
        int lostInARow = pity.getLostInARow();
        int wonInARow = pity.getWonInARow();
        if (banner.theory() && lostInARow >= 2) {
            result = new SSRare(banner.spec().getWeapon());
        } else if (banner.theory() && wonInARow >= 2) {
            result = new SSRare(rng);
        } else if (rng.nextBoolean()) {
            result = new SSRare(banner.spec().getWeapon());
        }

        postPull(banner, result);
    }

    private void postPull(final Banner banner, final SSRItem result) {
        var pity = banner.pity();
        banner.history().add(result);
        if (result.isStandard()) {
            pity.increment(PityCounter.LOST);
            pity.increment(PityCounter.LOST_IN_A_ROW);
            pity.reset(PityCounter.WON_IN_A_ROW);
            return;
        }

        pity.increment(PityCounter.WON);
        pity.increment(PityCounter.WON_IN_A_ROW);
        pity.reset(PityCounter.LOST_IN_A_ROW);

        var last = banner.history().getLast();
        if (last instanceof Matrix matrix) {
            banner.statistics().increment(StatisticsCounter.TOTAL_MATRIX_PIECES);
            switch (matrix.getPosition()) {
                case BRAIN -> banner.statistics().increment(StatisticsCounter.BRAIN_PIECES);
                case HANDS -> banner.statistics().increment(StatisticsCounter.HANDS_PIECES);
                case HEAD -> banner.statistics().increment(StatisticsCounter.HEAD_PIECES);
                case HEART -> banner.statistics().increment(StatisticsCounter.HEART_PIECES);
            }
            blockMatrices(banner);

            return;
        }

        banner.statistics().increment(StatisticsCounter.BANNER_WEAPON);
    }

    private void blockMatrices(final Banner banner) {
        var statistics = banner.statistics();
        if (statistics.getBrainPieces() >= 4) {
            ((MatrixBanner) banner).blocked(Matrix.Pieces.BRAIN);
        } else if (statistics.getHandsPieces() >= 4) {
            ((MatrixBanner) banner).blocked(Matrix.Pieces.HANDS);
        } else if (statistics.getHeadPieces() >= 4) {
            ((MatrixBanner) banner).blocked(Matrix.Pieces.HEAD);
        } else if (statistics.getHeartPieces() >= 4) {
            ((MatrixBanner) banner).blocked(Matrix.Pieces.HEART);
        }
    }
}
