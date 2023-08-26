package org.eos.tof.common;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.eos.tof.common.counters.PityCounter;
import org.eos.tof.common.counters.StatisticsCounter;
import org.eos.tof.common.counters.TokenCounter;
import org.eos.tof.common.handlers.Handler;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

/**
 * Factory for the {@link Banner} object.
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
 * @see MatrixBanner
 * @see WeaponBanner
 */
@RequiredArgsConstructor
@Setter
@Component
public class BannerFactory implements FactoryBean<Banner> {

    private Banner.Spec spec;
    private Class<? extends Banner> clazz = WeaponBanner.class;
    private Banner.RateMode rate;
    private boolean isTheory = true;
    private final History history;
    private final PityCounter pity;
    private final StatisticsCounter statistics;
    private final TokenCounter tokens;
    private final Handler matrixHandlers;
    private final Handler weaponHandlers;

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public Banner getObject() {
        Banner banner;
        if (clazz.isAssignableFrom(MatrixBanner.class)) {
            banner = new MatrixBanner(spec);
            banner.handlers(matrixHandlers);
        } else {
            banner = new WeaponBanner(spec);
            banner.handlers(weaponHandlers);
        }

        banner.rate(rate);
        banner.theory(isTheory);
        banner.history(history);
        banner.pity(pity);
        banner.statistics(statistics);
        banner.tokens(tokens);

        return banner;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<?> getObjectType() {
        return Banner.class;
    }
}
