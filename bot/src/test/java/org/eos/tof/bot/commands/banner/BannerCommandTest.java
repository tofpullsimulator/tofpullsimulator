package org.eos.tof.bot.commands.banner;

import discord4j.core.event.domain.interaction.ChatInputAutoCompleteEvent;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandOption;
import discord4j.core.spec.InteractionApplicationCommandCallbackReplyMono;

import java.util.Collections;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {BannerCommand.class, MockSubCommands.class})
class BannerCommandTest {

    @Autowired
    private BannerCommand command;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private InteractionApplicationCommandCallbackReplyMono reply;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private ChatInputInteractionEvent interactionEvent;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private ChatInputAutoCompleteEvent autoCompleteEvent;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private ApplicationCommandInteractionOption option;

    @BeforeEach
    void setUp() {
        when(reply.withEphemeral(anyBoolean())).thenReturn(reply);
        when(reply.withContent(anyString())).thenReturn(reply);
        when(reply.then()).thenReturn(Mono.empty());
        when(option.getType()).thenReturn(ApplicationCommandOption.Type.SUB_COMMAND);
        when(autoCompleteEvent.getOptions()).thenReturn(Collections.singletonList(option));
        when(interactionEvent.getOptions()).thenReturn(Collections.singletonList(option));
        when(interactionEvent.reply()).thenReturn(reply);
    }

    @Test
    void shouldGetTheNameOfTheCommand() {
        Assertions.assertEquals("banner", command.getName());
    }

    @Test
    void shouldHandleInteractionEvent() {
        when(option.getName()).thenReturn("mock");
        var mono = command.handle(interactionEvent);
        StepVerifier.create(mono).verifyComplete();
    }

    @Test
    void shouldFailInteractionEvent() {
        when(option.getName()).thenReturn("failing");
        var mono = command.handle(interactionEvent);
        StepVerifier.create(mono).verifyComplete();
    }

    @Test
    void shouldHandleError() {
        var mono = command.handleError(interactionEvent, new Throwable("This is a test"));
        StepVerifier.create(mono).verifyComplete();
    }

    @Test
    void shouldHandleAutoCompleteEvent() {
        when(option.getName()).thenReturn("mock");
        var mono = command.handle(autoCompleteEvent);
        StepVerifier.create(mono).verifyComplete();
    }
}
