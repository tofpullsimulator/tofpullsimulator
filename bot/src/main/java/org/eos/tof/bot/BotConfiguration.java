package org.eos.tof.bot;

import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.rest.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * General configuration for the bot.
 *
 * @author Eos
 */
@Configuration
public class BotConfiguration {

    @Value("${discord.token}")
    private String token;

    @Value("${discord.guildId:-1L}")
    private long guildId;

    /**
     * Bean for a configured discord client.
     *
     * @return The configured discord client.
     */
    @Bean
    public GatewayDiscordClient client() {
        return DiscordClientBuilder.create(token)
                .build()
                .login()
                .block();
    }

    /**
     * Bean for the rest client of the discord client.
     *
     * @param client The discord client, {@link BotConfiguration#client()}.
     * @return The reset client of the discord client.
     */
    @Bean
    public RestClient restClient(final GatewayDiscordClient client) {
        return client.getRestClient();
    }

    /**
     * The id of the guild where the commands are registered on.
     *
     * @return The id of a guild.
     */
    @Bean
    public long getGuildId() {
        return guildId;
    }
}
