package org.eos.tof.common;

import java.util.random.RandomGenerator;
import java.util.Random;

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
}
