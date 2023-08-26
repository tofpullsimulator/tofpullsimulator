package org.eos.tof.common;

import java.util.Objects;

import lombok.extern.log4j.Log4j2;

/**
 * Object representing the weapon banner to be pulled on.
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
 * @see Banner
 * @see BannerFactory
 */
@Log4j2
public class WeaponBanner extends Banner {

    /**
     * Create a new weapon banner instance.
     *
     * @param spec The spec of the weapon banner instance.
     */
    public WeaponBanner(final Banner.Spec spec) {
        super(spec);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Banner pull(final long amount) {
        Objects.requireNonNull(spec(), "No spec found!");

        if (amount <= 0) {
            while (statistics().getWeaponBanner() < 7) {
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
}
