package org.eos.tof.bot.listeners;

import discord4j.core.event.domain.interaction.ChatInputAutoCompleteEvent;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import org.eos.tof.bot.commands.SlashCommand;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class MockCommands {

    @Component
    public static class MockCommand implements SlashCommand {

        @Override
        public String getName() {
            return "mock";
        }

        @Override
        public Mono<Void> handle(final ChatInputInteractionEvent event) {
            return Mono.just("mock").then();
        }

        @Override
        public Mono<Void> handleError(final ChatInputInteractionEvent event, final Throwable error) {
            return Mono.just("mock").then();
        }

        @Override
        public Mono<Void> handle(final ChatInputAutoCompleteEvent event) {
            return Mono.just("mock").then();
        }
    }

    @Component
    public static class MockFailingSubCommand implements SlashCommand {

        @Override
        public String getName() {
            return "failing";
        }

        @Override
        public Mono<Void> handle(final ChatInputInteractionEvent event) {
            return Mono.error(new Exception("Failing handling interaction event")).then();
        }

        @Override
        public Mono<Void> handle(final ChatInputAutoCompleteEvent event) {
            return Mono.error(new Exception("Failing handling autocomplete event")).then();
        }

        @Override
        public Mono<Void> handleError(final ChatInputInteractionEvent event, final Throwable error) {
            return Mono.error(new Exception("Failing handling error")).then();
        }
    }
}
