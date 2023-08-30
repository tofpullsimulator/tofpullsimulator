package org.eos.tof.bot.commands.banner;

import discord4j.core.event.domain.interaction.ChatInputAutoCompleteEvent;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.InteractionApplicationCommandCallbackReplyMono;
import discord4j.core.spec.MessageCreateFields;

import java.util.Optional;

import org.eos.tof.bot.BannerService;
import org.eos.tof.common.Banner;
import org.eos.tof.common.MatrixBanner;
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

@SpringBootTest(classes = {PullMatrixCommand.class, CreateMatrixCommand.class, BannerService.class})
@ComponentScan(basePackages = {"org.eos.tof.common"})
@EnableAutoConfiguration
class PullMatrixCommandTest {

    @Autowired
    private PullMatrixCommand command;
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
    private ApplicationCommandInteractionOption amount;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private ApplicationCommandInteractionOptionValue amountValue;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private ApplicationCommandInteractionOption name;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private ApplicationCommandInteractionOptionValue nameValue;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private ApplicationCommandInteractionOption theory;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private ApplicationCommandInteractionOptionValue theoryValue;

    @BeforeEach
    void setUp() {
        service.reset(0L).block();

        when(reply.withEphemeral(anyBoolean())).thenReturn(reply);
        when(reply.withContent(anyString())).thenReturn(reply);
        when(reply.withFiles(any(MessageCreateFields.File.class))).thenReturn(reply);
        when(reply.withEmbeds(any(EmbedCreateSpec.class))).thenReturn(reply);
        when(reply.then()).thenReturn(Mono.empty());
        when(option.getOption("amount")).thenReturn(Optional.of(amount));
        when(option.getOption("name")).thenReturn(Optional.of(name));
        when(option.getOption("theory")).thenReturn(Optional.of(theory));
        when(interactionEvent.reply()).thenReturn(reply);
    }

    @Test
    void shouldGetTheNameOfTheCommand() {
        Assertions.assertEquals("banner pull-matrix", command.getName());
    }

    @Test
    void shouldHandleInteractionEvent() {
        when(amountValue.asLong()).thenReturn(1L);
        when(amount.getValue()).thenReturn(Optional.of(amountValue));
        when(nameValue.asString()).thenReturn("Yu Lan");
        when(name.getValue()).thenReturn(Optional.of(nameValue));
        when(theoryValue.asBoolean()).thenReturn(false);
        when(theory.getValue()).thenReturn(Optional.of(theoryValue));

        command.handle(interactionEvent, option).block();

        var mono = service.get(0L);
        StepVerifier.create(mono)
                .assertNext(it -> {
                    Assertions.assertEquals(Banner.Spec.YULAN, it.spec());
                    Assertions.assertEquals(1, it.pity().getSSR());
                })
                .verifyComplete();
    }

    @Test
    void shouldHandleInteractionEventAlreadyExistingBanner() {
        service.pull(0L, "Yu Lan", false, 10, MatrixBanner.class).block();

        when(amountValue.asLong()).thenReturn(1L);
        when(amount.getValue()).thenReturn(Optional.of(amountValue));
        when(nameValue.asString()).thenReturn(null);
        when(name.getValue()).thenReturn(Optional.of(nameValue));
        when(theoryValue.asBoolean()).thenReturn(false);
        when(theory.getValue()).thenReturn(Optional.of(theoryValue));

        command.handle(interactionEvent, option).block();

        var mono = service.get(0L);
        StepVerifier.create(mono)
                .assertNext(it -> {
                    Assertions.assertEquals(Banner.Spec.YULAN, it.spec());
                    Assertions.assertEquals(11, it.pity().getSSR());
                })
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
        when(focusedOption.getName()).thenReturn("amount");
        when(autoCompleteEvent.getFocusedOption()).thenReturn(focusedOption);
        when(autoCompleteEvent.respondWithSuggestions(any())).thenReturn(Mono.empty());

        var mono = command.handle(autoCompleteEvent, option);
        StepVerifier.create(mono).verifyComplete();
    }

    @Test
    void shouldSkipAutoCompleteEvent() {
        var focusedOption = mock(ApplicationCommandInteractionOption.class);
        when(focusedOption.getName()).thenReturn("invalid");
        when(autoCompleteEvent.getFocusedOption()).thenReturn(focusedOption);

        var mono = command.handle(autoCompleteEvent, option);
        StepVerifier.create(mono).verifyComplete();
    }
}
