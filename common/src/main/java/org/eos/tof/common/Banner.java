package org.eos.tof.common;

import java.util.Objects;

import lombok.AccessLevel;
import lombok.extern.log4j.Log4j2;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.eos.tof.common.counters.PityCounter;
import org.eos.tof.common.counters.StatisticsCounter;
import org.eos.tof.common.counters.TokenCounter;
import org.eos.tof.common.handlers.Handler;

/**
 * Object representing the banner to be pulled on.
 * <p>
 * Example;
 * <pre>
 *     BannerFactory factory = new BannerFactor();
 *     factory.setRate(Banner.RateMode.NORMAL);
 *     factory.setTheory(true);
 *
 *     Banner banner = factory.getObject();
 *     banner.pull(10);
 * </pre>
 *
 * @author Eos
 * @see BannerFactory
 */
@Log4j2
@RequiredArgsConstructor
@Getter
@Setter
public class Banner {

    /**
     * The rates for the various item rarities to be used.
     *
     * @author Eos
     */
    @Getter
    public enum RateMode {
        /**
         * Normal rates.
         */
        NORMAL(0.75, 1, 91.40, 6.85),
        /**
         * Guarantee rates.
         */
        GUARANTEE(2, 12, 80, 6);

        private final double ssr;
        private final double sr;
        private final double r;
        private final double n;

        RateMode(final double ssr, final double sr, final double r, final double n) {
            this.ssr = ssr;
            this.sr = sr;
            this.r = r;
            this.n = n;
        }
    }

    /**
     * The specifications of the banner, In other words the banners people can pull on.
     *
     * @author Eos
     */
    @Getter
    public enum Spec {
        /**
         * Mimi spec.
         */
        MIMI("Meng Zhang", "Mimi"),
        /**
         * Zeke spec.
         */
        ZEKE("Zhi Ming", "Zeke"),
        /**
         * Yu Lan spec.
         */
        YULAN("Unity", "Yu Lan"),
        /**
         * Liu Huo spec.
         */
        LIUHUO("Pine Comet", "Liu Huo"),
        /**
         * Rubilia spec.
         */
        RUBILIA("Lost Art", "RUBILIA"),
        /**
         * Gnonno spec.
         */
        GNONNO("Mini Hurricane", "GNONNO"),
        /**
         * Fiona spec.
         */
        FIONA("Moonstar Bracelet", "Fiona"),
        /**
         * Icarus spec.
         */
        ICARUS("Precious One", "Icarus"),
        /**
         * Lan spec.
         */
        LAN("Lingguang", "LAN"),
        /**
         * Fenrir spec.
         */
        FENRIR("Gleipnir", "Fenrir"),
        /**
         * Umi spec.
         */
        UMI("Mobius", "Umi"),
        /**
         * Alyss spec.
         */
        ALYSS("Unyielding Wing", "Alyss"),
        /**
         * Annabella spec.
         */
        ANNABELLA("Clover Cross", "Annabella"),
        /**
         * Tian Lang spec.
         */
        TIANLANG("Thunderbreaker", "Tian Lang"),
        /**
         * Lyra spec.
         */
        LYRA("Vesper", "Lyra"),
        /**
         * Lin spec.
         */
        LIN("Shadoweave", "Lin");

        private final String weapon;
        private final String simulacra;

        Spec(final String weapon, final String simulacra) {
            this.weapon = weapon;
            this.simulacra = simulacra;
        }
    }

    private final Spec spec;
    private RateMode rate = RateMode.NORMAL;
    private boolean isTheory = true;
    private History history;
    private PityCounter pity;
    private StatisticsCounter statistics;
    private TokenCounter tokens;
    @Getter(AccessLevel.NONE)
    private Handler handlers;

    /**
     * Pull on the banner.
     *
     * @param amount The amount can be any positive number. if the amount is negative it will pull until there are 7
     *               banner weapons (A6).
     * @return The same instance of the banner.
     * @throws NullPointerException if the @see #spec is null.
     */
    public Banner pull(final long amount) {
        Objects.requireNonNull(this.spec, "No spec found!");

        if (amount <= 0) {
            while (statistics.get(StatisticsCounter.BANNER_WEAPON) != 7) {
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
        handlers.check(this);
    }

    /**
     * Resets the banner, including the pity, history, statistics and tokens.
     */
    public void reset() {
        history.reset();
        pity.reset();
        statistics.reset();
        tokens.reset();
    }
}
