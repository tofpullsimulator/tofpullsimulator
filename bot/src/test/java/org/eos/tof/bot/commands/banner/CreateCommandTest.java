package org.eos.tof.bot.commands.banner;

import discord4j.core.event.domain.interaction.ChatInputAutoCompleteEvent;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import discord4j.core.spec.InteractionApplicationCommandCallbackReplyMono;

import java.util.Optional;

import org.eos.tof.bot.BannerService;
import org.eos.tof.common.Banner;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {CreateCommand.class, BannerService.class})
@ComponentScan(basePackages = {"org.eos.tof.common"})
@EnableAutoConfiguration
class CreateCommandTest {

    @Autowired
    private CreateCommand command;
    @Autowired
    private BannerService service;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private InteractionApplicationCommandCallbackReplyMono reply;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private ChatInputInteractionEvent interactionEvent;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private ChatInputAutoCompleteEvent autoCompleteEvent;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private ApplicationCommandInteractionOption option;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private ApplicationCommandInteractionOptionValue value;

    @BeforeEach
    void setUp() {
        when(reply.withEphemeral(anyBoolean())).thenReturn(reply);
        when(reply.withContent(anyString())).thenReturn(reply);
        when(reply.then()).thenReturn(Mono.empty());
        when(option.getOption(anyString())).thenReturn(Optional.of(option));
        when(option.getValue()).thenReturn(Optional.of(value));
        when(interactionEvent.reply()).thenReturn(reply);
    }

    @Test
    void shouldGetTheNameOfTheCommand() {
        Assertions.assertEquals("banner create", command.getName());
    }

    @Test
    void shouldHandleInteractionEvent() {
        command.handle(interactionEvent, option).block();
        var mono = service.get(0L);
        StepVerifier.create(mono)
                .assertNext(it -> Assertions.assertEquals(Banner.Spec.ZEKE, it.getSpec()))
                .verifyComplete();
    }

    @Test
    void shouldHandleError() {
        var mono = command.handleError(interactionEvent, new Throwable("This is a test"));
        StepVerifier.create(mono).verifyComplete();
    }

    @Test
    void shouldHandleAutoCompleteEvent() {
        var focusedOption = mock(ApplicationCommandInteractionOption.class);
        when(focusedOption.getName()).thenReturn("name");
        when(autoCompleteEvent.getFocusedOption()).thenReturn(focusedOption);
        when(autoCompleteEvent.respondWithSuggestions(any())).thenReturn(Mono.empty());

        var mono = command.handle(autoCompleteEvent);
        StepVerifier.create(mono).verifyComplete();
    }

    @Test
    void shouldSkipAutoCompleteEvent() {
        var focusedOption = mock(ApplicationCommandInteractionOption.class);
        when(focusedOption.getName()).thenReturn("invalid");
        when(autoCompleteEvent.getFocusedOption()).thenReturn(focusedOption);

        var mono = command.handle(autoCompleteEvent);
        StepVerifier.create(mono).verifyComplete();
    }
}
