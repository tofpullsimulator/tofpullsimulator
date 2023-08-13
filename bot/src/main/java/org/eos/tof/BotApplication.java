package org.eos.tof;

import lombok.Generated;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;

/**
 * Main for the bot.
 *
 * @author Eos
 */
@Generated
@SpringBootApplication
public class BotApplication {

    /**
     * Start the bot.
     *
     * @param args The arguments passed to the bot.
     */
    public static void main(final String[] args) {
        SpringApplication.run(BotApplication.class, args);
    }
}
