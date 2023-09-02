package org.eos.tof.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
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
 *     factory.setSpec(Banner.Spec.YULAN);
 *     factory.setRate(Banner.RateMode.WEAPON_NORMAL);
 *     factory.setClazz(WeaponBanner.class);
 *     factory.setTheory(true);
 *
 *     Banner banner = factory.getObject();
 *     banner.pull(10);
 * </pre>
 *
 * @author Eos
 * @see BannerFactory
 * @see MatrixBanner
 * @see WeaponBanner
 */
@RequiredArgsConstructor
public abstract class Banner {

    /**
     * The rates for the various item rarities to be used.
     *
     * @author Eos
     */
    @Getter
    public enum RateMode {
        /**
         * Normal rates for weapons.
         */
        WEAPON_NORMAL(0.75, 1, 91.40, 6.85),
        /**
         * Guarantee rates for weapons.
         */
        WEAPON_GUARANTEE(2, 12, 80, 6),
        /**
         * Guarantee rates for matrices.
         */
        MATRIX_NORMAL(1.7, 7.5, 90.8, -1),
        /**
         * Guarantee rates for matrices.
         */
        MATRIX_GUARANTEE(4.2, 18.5, 77.3, -1);

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
        LINGHAN("Overseer", "Ling Han", "Ling Han"),
        /**
         * Mimi spec.
         */
        FEISE("Zhongrui", "Fei Se", "Fei Se"),
        /**
         * Mimi spec.
         */
        MIMI("Meng Zhang", "Resounding Chant", "Mimi"),
        /**
         * Zeke spec.
         */
        ZEKE("Zhi Ming", "Lost af Heart", "Zeke"),
        /**
         * Yu Lan spec.
         */
        YULAN("Unity", "Sincerity", "Yu Lan"),
        /**
         * Liu Huo spec.
         */
        LIUHUO("Pine Comet", "Spring & Autumn Brush", "Liu Huo"),
        /**
         * Rubilia spec.
         */
        RUBILIA("Lost Art", "Seeker", "Rubilia"),
        /**
         * Gnonno spec.
         */
        GNONNO("Mini Hurricane", "Adventure Asteroid", "Gnonno"),
        /**
         * Fiona spec.
         */
        FIONA("Moonstar Bracelet", "Stimulus", "Fiona"),
        /**
         * Icarus spec.
         */
        ICARUS("Precious One", "With Frost", "Icarus"),
        /**
         * Lan spec.
         */
        LAN("Lingguang", "Firesstorm", "Lan"),
        /**
         * Fenrir spec.
         */
        FENRIR("Gleipnir", "Symphony of Life", "Fenrir"),
        /**
         * Umi spec.
         */
        UMI("Mobius", "Double Magic", "Umi"),
        /**
         * Alyss spec.
         */
        ALYSS("Unyielding Wing", "Spiritual Reconstruction", "Alyss"),
        /**
         * Annabella spec.
         */
        ANNABELLA("Clover Cross", "Bloody Anecdote", "Annabella"),
        /**
         * Tian Lang spec.
         */
        TIANLANG("Thunderbreaker", "Savage Wolf", "Tian Lang"),
        /**
         * Lyra spec.
         */
        LYRA("Vesper", "The Confession of Memory", "Lyra"),
        /**
         * Lin spec.
         */
        LIN("Shadoweave", "Night City Never Sleeps", "Lin"),
        /**
         * Saki Fuwa spec.
         */
        SAKIFUWA("Heartstream", "Super Flow", "Saki Fuwa"),
        /**
         * Ruby spec.
         */
        RUBY("Spark", "White Rabbit Dance", "Ruby"),
        /**
         * Cobalt-B spec.
         */
        COBALTB("Flaming Revolver", "Intuitive Tactic", "Cobalt-B"),
        /**
         * Claudia spec.
         */
        CLAUDIA("Guren Blade", "Multi-directional Strike", "Claudia"),
        /**
         * Frigg spec.
         */
        FRIGG("Balmung", "Iron Mind and Body", "Frigg"),
        /**
         * Nemesis spec.
         */
        NEMESIS("Venus", "Life Bridging", "Nemesis");

        private final String weapon;
        private final String matrix;
        private final String simulacra;

        Spec(final String weapon, final String matrix, final String simulacra) {
            this.weapon = weapon;
            this.matrix = matrix;
            this.simulacra = simulacra;
        }

        /**
         * Get a banner spec from a given name.
         *
         * @param name The name of the banner spec to get.
         * @return The fetched banner spec.
         */
        public static Spec from(final String name) {
            return valueOf(name.toUpperCase().replace(" ", ""));
        }
    }

    private final Spec spec;
    private RateMode rate = RateMode.MATRIX_NORMAL;
    private boolean isTheory = true;
    private History history;
    private PityCounter pity;
    private StatisticsCounter statistics;
    private TokenCounter tokens;
    private Handler handlers;

    /**
     * Pull on the banner.
     *
     * @param amount The amount can be any positive number. If the amount is negative it will pull until the banner is
     *               maxed out.
     * @return The same instance of the banner.
     * @throws NullPointerException if the spec of the banner is null.
     */
    public abstract Banner pull(final long amount);

    /**
     * Resets the banner, including the pity, history, statistics and tokens.
     */
    public void reset() {
        history.reset();
        pity.reset();
        statistics.reset();
        tokens.reset();
    }

    /**
     * Get the specification for the banner.
     *
     * @return The specification for the banner.
     */
    public Banner.Spec spec() {
        return spec;
    }

    /**
     * Get the rates for the banner.
     *
     * @return The rates for the banner.
     */
    public Banner.RateMode rate() {
        return rate;
    }

    /**
     * Set the rates for the banner.
     *
     * @param rate The rates for the banner.
     */
    protected void rate(final Banner.RateMode rate) {
        this.rate = rate;
    }

    /**
     * Whenever the banner confirms to the theory.
     *
     * @return Whenever the banner confirms to the theory.
     */
    public boolean theory() {
        return isTheory;
    }

    /**
     * Set whenever the banner should confirm to the theory.
     *
     * @param isTheory Whenever the banner should confirm to the theory.
     */
    protected void theory(final boolean isTheory) {
        this.isTheory = isTheory;
    }

    /**
     * Get the history for the banner.
     *
     * @return The history for the banner.
     */
    public History history() {
        return history;
    }

    /**
     * Set the history for the banner.
     *
     * @param history The history for the banner.
     */
    protected void history(final History history) {
        this.history = history;
    }

    /**
     * Get the pity for the banner.
     *
     * @return The pity for the banner.
     */
    public PityCounter pity() {
        return pity;
    }

    /**
     * Set the pity for the banner.
     *
     * @param pity The pity for the banner.
     */
    protected void pity(final PityCounter pity) {
        this.pity = pity;
    }

    /**
     * Get the statistics for the banner.
     *
     * @return The statistics for the banner.
     */
    public StatisticsCounter statistics() {
        return statistics;
    }

    /**
     * Set the statistics for the banner.
     *
     * @param statistics The statistics for the banner.
     */
    protected void statistics(final StatisticsCounter statistics) {
        this.statistics = statistics;
    }

    /**
     * Get the tokens for the banner.
     *
     * @return The tokens for the banner.
     */
    public TokenCounter tokens() {
        return tokens;
    }

    /**
     * Set the tokens for the banner.
     *
     * @param tokens The tokens for the banner.
     */
    protected void tokens(final TokenCounter tokens) {
        this.tokens = tokens;
    }

    /**
     * Get the handlers for the banner.
     *
     * @return The handlers for the banner.
     */
    protected Handler handlers() {
        return handlers;
    }

    /**
     * Set the handlers for the banner.
     *
     * @param handlers The handlers for the banner.
     */
    protected void handlers(final Handler handlers) {
        this.handlers = handlers;
    }
}
