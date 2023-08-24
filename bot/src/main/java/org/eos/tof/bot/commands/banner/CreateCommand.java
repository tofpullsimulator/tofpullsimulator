package org.eos.tof.bot.commands.banner;

import discord4j.core.event.domain.interaction.ChatInputAutoCompleteEvent;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.entity.Member;
import discord4j.discordjson.json.ApplicationCommandOptionChoiceData;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.AllArgsConstructor;
import org.eos.tof.bot.BannerService;
import org.eos.tof.bot.commands.SlashSubCommand;
import org.eos.tof.common.Banner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * Command for creating a new banner for the member.
 *
 * @author Eos
 */
@AllArgsConstructor
@Component
public class CreateCommand extends AbstractBannerSubCommand implements SlashSubCommand {

    private final BannerService service;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return "banner create";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Void> handle(final ChatInputInteractionEvent event,
                             final ApplicationCommandInteractionOption option) {
        String name = getName(option, Banner.Spec.ZEKE.name());
        boolean isTheory = isTheory(option);

        Optional<Member> member = event.getInteraction().getMember();
        @SuppressWarnings({"java:S3655", "OptionalGetWithoutIsPresent"})
        long id = member.get().getUserData().id().asLong();

        return service.get(id, name, isTheory, true).flatMap(b -> {
            var spec = b.getSpec();
            return event.reply()
                    .withEphemeral(true)
                    .withContent("Created banner '" + spec.getSimulacra() + " (" + spec.getWeapon() + ")'")
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
                .withContent("Sorry, an error occurred while creating the banner")
                .then();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Void> handle(final ChatInputAutoCompleteEvent event,
                             final ApplicationCommandInteractionOption option) {
        String focusedOption = event.getFocusedOption().getName();
        if (focusedOption.equals("name")) {
            List<ApplicationCommandOptionChoiceData> suggestions = new ArrayList<>();
            for (Banner.Spec spec : Banner.Spec.values()) {
                var choice = ApplicationCommandOptionChoiceData.builder()
                        .name(spec.getSimulacra() + " (" + spec.getWeapon() + ")")
                        .value(spec.getSimulacra())
                        .build();
                suggestions.add(choice);
            }

            return event.respondWithSuggestions(suggestions);
        }

        return Mono.empty();
    }
}
