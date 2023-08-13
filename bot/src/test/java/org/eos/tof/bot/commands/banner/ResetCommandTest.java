package org.eos.tof.bot.commands.banner;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.spec.InteractionApplicationCommandCallbackReplyMono;
import org.eos.tof.bot.BannerService;
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

import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {ResetCommand.class, BannerService.class})
@ComponentScan(basePackages = {"org.eos.tof.common"})
@EnableAutoConfiguration
class ResetCommandTest {

    @Autowired
    private ResetCommand command;
    @Autowired
    private BannerService service;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private InteractionApplicationCommandCallbackReplyMono reply;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private ChatInputInteractionEvent interactionEvent;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private ApplicationCommandInteractionOption option;

    @BeforeEach
    void setUp() {
        when(reply.withEphemeral(anyBoolean())).thenReturn(reply);
        when(reply.withContent(anyString())).thenReturn(reply);
        when(reply.then()).thenReturn(Mono.empty());
        when(interactionEvent.reply()).thenReturn(reply);
    }

    @Test
    void shouldGetTheNameOfTheCommand() {
        Assertions.assertEquals("banner reset", command.getName());
    }

    @Test
    void shouldHandleInteractionEvent() {
        service.pull(0L, "Yu Lan", true, 10).block();
        command.handle(interactionEvent, option).block();

        var mono = service.get(0L);
        StepVerifier.create(mono)
                .verifyError(NullPointerException.class);
    }

    @Test
    void shouldHandleError() {
        var mono = command.handleError(interactionEvent, new Throwable("This is a test"));
        StepVerifier.create(mono).verifyComplete();
    }
}
