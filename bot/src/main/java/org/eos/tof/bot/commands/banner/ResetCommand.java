package org.eos.tof.bot.commands.banner;

import discord4j.core.event.domain.interaction.ChatInputAutoCompleteEvent;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.entity.Member;

import java.util.Optional;

import lombok.AllArgsConstructor;
import org.eos.tof.bot.BannerService;
import org.eos.tof.bot.commands.SlashSubCommand;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * Command for resetting the banner of the member.
 *
 * @author Eos
 */
@AllArgsConstructor
@Component
public class ResetCommand extends AbstractBannerSubCommand implements SlashSubCommand {

    private final BannerService service;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return "banner reset";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Void> handle(final ChatInputInteractionEvent event,
                             final ApplicationCommandInteractionOption option) {
        Optional<Member> member = event.getInteraction().getMember();
        @SuppressWarnings({"java:S3655", "OptionalGetWithoutIsPresent"})
        long id = member.get().getUserData().id().asLong();

        return service.reset(id).flatMap(b -> {
            var spec = b.getSpec();
            return event.reply()
                    .withEphemeral(true)
                    .withContent("Reset banner '" + spec.getSimulacra() + " (" + spec.getWeapon() + ")'")
                    .then();
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Void> handleError(final ChatInputInteractionEvent event, final Throwable error) {
        return event.reply()
                .withEphemeral(true)
                .withContent("Sorry, an error occurred while resetting the banner")
                .then();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Void> handle(final ChatInputAutoCompleteEvent event,
                             final ApplicationCommandInteractionOption option) {
        return Mono.empty();
    }
}
