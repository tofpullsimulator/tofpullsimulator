package org.eos.tof.common;

import java.util.Random;
import java.util.random.RandomGenerator;

import org.eos.tof.common.handlers.Handler;
import org.eos.tof.common.handlers.PityHandler;
import org.eos.tof.common.handlers.PullHandler;
import org.eos.tof.common.handlers.TokenHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for the banner application.
 *
 * @author Eos
 */
@Configuration
public class Config {

    /**
     * A random generator used across various instances of the banner.
     *
     * @return A random generator instance.
     */
    @Bean
    public RandomGenerator rng() {
        return new Random();
    }

    /**
     * A chain of handlers for pulling on the banner.
     *
     * @param pityHandler  Handler for handling pity.
     * @param pullHandler  Handler for handling pulling.
     * @param tokenHandler Handler for handling post pull statistics.
     * @return The handler chain.
     */
    @Bean
    public Handler handlers(final PityHandler pityHandler, final PullHandler pullHandler,
                            final TokenHandler tokenHandler) {
        return Handler.link(pityHandler, pullHandler, tokenHandler);
    }
}
