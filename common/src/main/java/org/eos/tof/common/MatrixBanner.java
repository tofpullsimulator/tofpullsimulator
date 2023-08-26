package org.eos.tof.common;

import java.util.Objects;

import lombok.extern.log4j.Log4j2;
import org.eos.tof.common.items.Matrix;

/**
 * Object representing the matrix banner to be pulled on.
 * <p>
 * Example;
 * <pre>
 *     BannerFactory factory = new BannerFactor();
 *     factory.setSpec(Banner.Spec.YULAN);
 *     factory.setRate(Banner.RateMode.MATRIX_NORMAL);
 *     factory.setClass(MatrixBanner.class);
 *     factory.setTheory(true);
 *
 *     Banner banner = factory.getObject();
 *     banner.pull(10);
 * </pre>
 *
 * @author Eos
 * @see Banner
 * @see BannerFactory
 */
@Log4j2
public class MatrixBanner extends Banner {

    private Matrix.Pieces blocked = null;
    private int boxes = 0;

    /**
     * Create a new matrix banner instance.
     *
     * @param spec The spec of the matrix banner instance.
     */
    public MatrixBanner(final Banner.Spec spec) {
        super(spec);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Banner pull(final long amount) {
        Objects.requireNonNull(spec(), "No spec found!");

        if (amount <= 0) {
            while (statistics().getTotalMatrixPieces() < 16) {
                pull();
            }

            return this;
        }

        for (int i = 0; i < amount; i++) {
            pull();
        }

        return this;
    }

    private void pull() {
        handlers().check(this);
    }

    /**
     * Get the current blocked matrix piece.
     *
     * @return The current blocked matrix piece.
     */
    public Matrix.Pieces blocked() {
        return blocked;
    }

    /**
     * Set the current blocked matrix piece.
     *
     * @param blocked The matrix piece which needs to be blocked.
     */
    public void blocked(final Matrix.Pieces blocked) {
        this.blocked = blocked;
    }

    /**
     * Get the amount of matrix boxes to be used on the banner.
     *
     * @return The amount of matrix boxes to be used on the banner.
     */
    public int boxes() {
        return boxes;
    }

    /**
     * Set the amount of matrix boxes to be used on the banner.
     *
     * @param boxes the amount of matrix boxes to be used on the banner.
     */
    public void boxes(final int boxes) {
        this.boxes = boxes;
    }
}
