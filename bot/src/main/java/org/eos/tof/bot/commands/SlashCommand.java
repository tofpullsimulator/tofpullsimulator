package org.eos.tof.bot.commands;

import discord4j.core.event.domain.interaction.ChatInputAutoCompleteEvent;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import reactor.core.publisher.Mono;

/**
 * @author Eos
 */
public interface SlashCommand {

    /**
     * Get the name of the command.
     *
     * @return The name of the command.
     */
    String getName();

    /**
     * Handle an interaction event.
     *
     * @param event The interaction event to be handled.
     * @return The result of the handled event.
     */
    Mono<Void> handle(final ChatInputInteractionEvent event);

    /**
     * Handle the errors of the command.
     *
     * @param event The interaction event to be handled.
     * @param error The error to be handled.
     * @return The result of the handled event.
     */
    Mono<Void> handleError(final ChatInputInteractionEvent event, Throwable error);

    /**
     * Handle an autocomplete event.
     *
     * @param event The autocomplete event to be handled.
     * @return The result of the handled event.
     */
    Mono<Void> handle(final ChatInputAutoCompleteEvent event);
}
