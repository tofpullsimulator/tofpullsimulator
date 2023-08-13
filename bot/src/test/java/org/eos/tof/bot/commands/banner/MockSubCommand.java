package org.eos.tof.bot.commands.banner;

import discord4j.core.event.domain.interaction.ChatInputAutoCompleteEvent;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import org.eos.tof.bot.commands.SlashSubCommand;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class MockSubCommand implements SlashSubCommand {

    @Override
    public String getName() {
        return "banner mock";
    }

    @Override
    public Mono<Void> handle(final ChatInputInteractionEvent event,
                             final ApplicationCommandInteractionOption option) {
        return Mono.just("mock").then();
    }

    @Override
    public Mono<Void> handle(final ChatInputAutoCompleteEvent event) {
        return Mono.just("mock").then();
    }

    @Override
    public Mono<Void> handleError(ChatInputInteractionEvent event, Throwable error) {
        return Mono.just("mock").then();
    }
}
