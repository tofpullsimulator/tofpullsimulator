package org.eos.tof.common.handlers.weapons;

import org.eos.tof.common.handlers.Handler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Handler for processing the banner after pulling on weapons.
 *
 * @author Eos
 */
@Configuration(value = "weapons")
@ComponentScan("org.eos.tof.common.handlers.weapons")
public class WeaponHandlers {

    /**
     * A chain of handlers for weapons for pulling on the banner.
     *
     * @param weaponPityHandler  Handler for handling pity on weapons.
     * @param weaponPullHandler  Handler for handling pulling on weapons.
     * @param weaponTokenHandler Handler for handling post pull statistics on weapons.
     * @return The handler chain.
     */
    @Bean
    public Handler weaponHandlers(final PityHandler weaponPityHandler,
                                  final PullHandler weaponPullHandler,
                                  final TokenHandler weaponTokenHandler) {
        return Handler.link(weaponPityHandler, weaponPullHandler, weaponTokenHandler);
    }
}
