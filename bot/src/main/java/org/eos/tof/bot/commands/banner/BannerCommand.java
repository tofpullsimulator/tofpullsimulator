package org.eos.tof.bot.commands.banner;

import discord4j.core.event.domain.interaction.ChatInputAutoCompleteEvent;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandOption;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.eos.tof.bot.commands.SlashCommand;
import org.eos.tof.bot.commands.SlashSubCommand;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Parent command for handling banner commands.
 *
 * @author Eos
 */
@AllArgsConstructor
@Log4j2
@Component
public class BannerCommand implements SlashCommand {

    private final List<SlashSubCommand> subCommands;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return "banner";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Void> handle(final ChatInputInteractionEvent event) {
        return Flux.fromIterable(event.getOptions())
                .filter(option -> option.getType().equals(ApplicationCommandOption.Type.SUB_COMMAND))
                .next()
                .flatMap(option -> handle(event, option));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Void> handleError(final ChatInputInteractionEvent event, final Throwable error) {
        return event.reply()
                .withEphemeral(true)
                .withContent("Sorry, an error occurred")
                .then();
    }

    private Mono<Void> handle(final ChatInputInteractionEvent event,
                              final ApplicationCommandInteractionOption option) {
        return Flux.fromIterable(subCommands)
                .filter(subCommand -> subCommand.getName().equals(getName() + " " + option.getName()))
                .next()
                .flatMap(subCommand -> subCommand.handle(event, option)
                        .doOnError(e -> log.error("An error happened, {}", e.getMessage()))
                        .onErrorResume(e -> subCommand.handleError(event, e)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Void> handle(final ChatInputAutoCompleteEvent event) {
        return Flux.fromIterable(event.getOptions())
                .filter(option -> option.getType().equals(ApplicationCommandOption.Type.SUB_COMMAND))
                .next()
                .flatMap(option -> handle(event, option));
    }

    private Mono<Void> handle(final ChatInputAutoCompleteEvent event,
                              final ApplicationCommandInteractionOption option) {
        return Flux.fromIterable(subCommands)
                .filter(subCommand -> subCommand.getName().equals(getName() + " " + option.getName()))
                .next()
                .flatMap(subCommand -> subCommand.handle(event));
    }
}
