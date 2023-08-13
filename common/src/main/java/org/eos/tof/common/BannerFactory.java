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
 *     factory.setRate(Banner.RateMode.NORMAL);
 *     factory.setTheory(true);
 *
 *     Banner banner = factory.getObject();
 *     banner.pull(10);
 * </pre>
 *
 * @author Eos
 * @see Banner
 */
@RequiredArgsConstructor
@Setter
@Component
public class BannerFactory implements FactoryBean<Banner> {

    private Banner.Spec spec;
    private Banner.RateMode rate = Banner.RateMode.NORMAL;
    private boolean isTheory = true;
    private final History history;
    private final PityCounter pity;
    private final StatisticsCounter statistics;
    private final TokenCounter tokens;
    private final Handler handlers;

    @NonNull
    @Override
    public Banner getObject() {
        Banner banner = new Banner(spec);
        banner.setRate(rate);
        banner.setTheory(isTheory);
        banner.setHistory(history);
        banner.setPity(pity);
        banner.setStatistics(statistics);
        banner.setTokens(tokens);
        banner.setHandlers(handlers);

        return banner;
    }

    @Override
    public Class<?> getObjectType() {
        return Banner.class;
    }
}
