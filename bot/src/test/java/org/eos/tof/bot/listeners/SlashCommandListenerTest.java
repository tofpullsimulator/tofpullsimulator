package org.eos.tof.bot.listeners;

import discord4j.core.GatewayDiscordClient;
import org.springframework.boot.test.context.SpringBootTest;
import discord4j.core.event.domain.interaction.ChatInputAutoCompleteEvent;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.spec.InteractionApplicationCommandCallbackReplyMono;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {SlashCommandListener.class, MockCommands.class})
class SlashCommandListenerTest {

    @Autowired
    private SlashCommandListener listener;

    @SuppressWarnings("unused")
    @MockBean(answer = Answers.RETURNS_DEEP_STUBS)
    private GatewayDiscordClient client;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private InteractionApplicationCommandCallbackReplyMono reply;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private ChatInputInteractionEvent interactionEvent;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private ChatInputAutoCompleteEvent autoCompleteEvent;

    @BeforeEach
    void setUp() {
        when(reply.withEphemeral(anyBoolean())).thenReturn(reply);
        when(reply.withContent(anyString())).thenReturn(reply);
        when(reply.then()).thenReturn(Mono.empty());
        when(interactionEvent.reply()).thenReturn(reply);
    }

    @Test
    void shouldHandleInteractionEvent() {
        when(interactionEvent.getCommandName()).thenReturn("mock");
        var mono = listener.handle(interactionEvent);
        StepVerifier.create(mono).verifyComplete();
    }

    @Test
    void shouldFailInteractionEvent() {
        when(interactionEvent.getCommandName()).thenReturn("failing");
        var mono = listener.handle(interactionEvent);
        StepVerifier.create(mono).verifyError();
    }

    @Test
    void shouldHandleAutoCompleteEvent() {
        when(autoCompleteEvent.getCommandName()).thenReturn("mock");
        var mono = listener.handle(autoCompleteEvent);
        StepVerifier.create(mono).verifyComplete();
    }
}
