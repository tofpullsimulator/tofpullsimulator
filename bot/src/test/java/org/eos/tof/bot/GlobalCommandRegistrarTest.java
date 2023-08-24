package org.eos.tof.bot;

import discord4j.core.GatewayDiscordClient;
import discord4j.rest.RestClient;
import discord4j.rest.service.ApplicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("SpringBootApplicationProperties")
@SpringBootTest(
        classes = {GlobalCommandRegistrar.class, BotConfiguration.class},
        properties = {"discord.guildId=-1"}
)
class GlobalCommandRegistrarTest {

    @MockBean(answer = Answers.RETURNS_DEEP_STUBS)
    private GatewayDiscordClient client;
    @MockBean(answer = Answers.RETURNS_DEEP_STUBS)
    private RestClient restClient;
    @MockBean
    private ApplicationService applicationService;

    @Autowired
    private GlobalCommandRegistrar registrar;

    @BeforeEach
    void setUp() {
        when(client.getRestClient()).thenReturn(restClient);
        when(restClient.getApplicationService()).thenReturn(applicationService);
        when(restClient.getApplicationId()).thenReturn(Mono.just(1L));
    }

    @Test
    void shouldRegistrarGlobalCommandsDefault() throws IOException {
        when(applicationService.bulkOverwriteGlobalApplicationCommand(anyLong(), anyList()))
                .thenReturn(Flux.empty());
        registrar.run(null);
        verify(applicationService).bulkOverwriteGlobalApplicationCommand(eq(1L), anyList());
    }

    @Test
    void shouldRegistrarGlobalCommands() throws IOException {
        when(applicationService.bulkOverwriteGlobalApplicationCommand(anyLong(), anyList()))
                .thenReturn(Flux.empty());
        registrar.run(null, -1L);
        verify(applicationService).bulkOverwriteGlobalApplicationCommand(eq(1L), anyList());
    }

    @Test
    void shouldRegistrarGuildCommands() throws IOException {
        when(applicationService.bulkOverwriteGuildApplicationCommand(anyLong(), anyLong(), anyList()))
                .thenReturn(Flux.empty());
        registrar.run(null, 1L);
        verify(applicationService).bulkOverwriteGuildApplicationCommand(eq(1L), eq(1L), anyList());
    }
}
