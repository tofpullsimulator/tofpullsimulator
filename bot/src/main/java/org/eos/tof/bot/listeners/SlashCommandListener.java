package org.eos.tof.bot.listeners;

import discord4j.core.event.domain.interaction.ChatInputAutoCompleteEvent;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.GatewayDiscordClient;

import java.util.List;

import lombok.extern.log4j.Log4j2;
import org.eos.tof.bot.commands.SlashCommand;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Listener object for {@link SlashCommand}s.
 *
 * @author Eos
 */
@Log4j2
@Component
public class SlashCommandListener {

    private final List<SlashCommand> commands;

    /**
     * Create a new {@link SlashCommandListener} instance.
     *
     * @param client   The discord client.
     * @param commands The list of commands to listen for.
     */
    public SlashCommandListener(final GatewayDiscordClient client, final List<SlashCommand> commands) {
        this.commands = commands;
        client.on(ChatInputInteractionEvent.class, this::handle).subscribe();
        client.on(ChatInputAutoCompleteEvent.class, this::handle).subscribe();
    }

    /**
     * Handle an interaction event.
     *
     * @param event The interaction event to be handled.
     * @return The response of the handled command, or nothing if there is no command for the event.
     */
    Mono<Void> handle(final ChatInputInteractionEvent event) {
        return Flux.fromIterable(commands)
                .filter(command -> command.getName().equals(event.getCommandName()))
                .next()
                .flatMap(command -> command.handle(event));
    }

    /**
     * Handle an autocomplete event.
     *
     * @param event The autocomplete event to be handled.
     * @return The response of the handled command, or nothing if there is no command for the event.
     */
    Mono<Void> handle(final ChatInputAutoCompleteEvent event) {
        return Flux.fromIterable(commands)
                .filter(command -> command.getName().equals(event.getCommandName()))
                .next()
                .flatMap(command -> command.handle(event));
    }
}
