package org.eos.tof.bot.commands;

import discord4j.core.event.domain.interaction.ChatInputAutoCompleteEvent;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import reactor.core.publisher.Mono;

/**
 * @author Eos
 */
public interface SlashSubCommand {

    /**
     * Get the name of the command.
     *
     * @return The name of the command.
     */
    String getName();

    /**
     * Handle an interaction event for the subcommand.
     *
     * @param event  The interaction event to be handled.
     * @param option The option of the subcommand.
     * @return The result of the handled event.
     */
    Mono<Void> handle(final ChatInputInteractionEvent event,
                      final ApplicationCommandInteractionOption option);

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
     * @param event  The autocomplete event to be handled.
     * @param option The option of the subcommand.
     * @return The result of the handled event.
     */
    Mono<Void> handle(final ChatInputAutoCompleteEvent event,
                      final ApplicationCommandInteractionOption option);
}
