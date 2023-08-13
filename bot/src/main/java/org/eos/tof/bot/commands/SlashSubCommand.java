package org.eos.tof.bot.commands;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import reactor.core.publisher.Mono;

/**
 * @author Eos
 */
public interface SlashSubCommand extends SlashCommand {

    /**
     * Handle an interaction event for the subcommand.
     *
     * @param event  The interaction event to be handled.
     * @param option The option of the subcommand.
     * @return The result of the handled event.
     */
    default Mono<Void> handle(final ChatInputInteractionEvent event,
                              final ApplicationCommandInteractionOption option) {
        return handle(event);
    }
}
