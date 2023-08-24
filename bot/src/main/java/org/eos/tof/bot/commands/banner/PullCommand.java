package org.eos.tof.bot.commands.banner;

import discord4j.core.event.domain.interaction.ChatInputAutoCompleteEvent;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.entity.Member;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.discordjson.json.ApplicationCommandOptionChoiceData;
import discord4j.rest.util.Color;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import lombok.AllArgsConstructor;
import org.eos.tof.bot.BannerService;
import org.eos.tof.bot.commands.SlashSubCommand;
import org.eos.tof.common.Banner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * Command for pulling on the banner of the member.
 *
 * @author Eos
 */
@AllArgsConstructor
@Component
public class PullCommand extends AbstractBannerSubCommand implements SlashSubCommand {

    private final BannerService service;
    private final CreateCommand createCommand;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return "banner pull";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Void> handle(final ChatInputInteractionEvent event,
                             final ApplicationCommandInteractionOption option) {
        long amount = getAmount(option);
        String name = getName(option, null);
        boolean isTheory = isTheory(option);

        Optional<Member> member = event.getInteraction().getMember();
        @SuppressWarnings({"java:S3655", "OptionalGetWithoutIsPresent"})
        long id = member.get().getUserData().id().asLong();
        if (name == null) {
            var banner = service.pull(id, amount);
            return bannerToReply(event, banner);
        }

        var banner = service.pull(id, name, isTheory, amount);
        return bannerToReply(event, banner);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Void> handleError(final ChatInputInteractionEvent event, final Throwable error) {
        return event.reply()
                .withEphemeral(true)
                .withContent("Sorry, an error occurred while pulling on the banner")
                .then();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Void> handle(final ChatInputAutoCompleteEvent event,
                             final ApplicationCommandInteractionOption option) {
        String focusedOption = event.getFocusedOption().getName();
        if (focusedOption.equals("amount")) {
            List<ApplicationCommandOptionChoiceData> suggestions = List.of(
                    ApplicationCommandOptionChoiceData.builder().name("Pull to A6").value("-1").build(),
                    ApplicationCommandOptionChoiceData.builder().name("80 pulls").value("80").build(),
                    ApplicationCommandOptionChoiceData.builder().name("10 pulls").value("10").build(),
                    ApplicationCommandOptionChoiceData.builder().name("1 pull").value("1").build()
            );

            return event.respondWithSuggestions(suggestions);
        }

        return createCommand.handle(event, option);
    }

    private Mono<Void> bannerToReply(final ChatInputInteractionEvent event, final Mono<Banner> banner) {
        return banner.flatMap(b -> {
            var spec = b.getSpec();
            var history = b.getHistory();
            var pity = b.getPity();
            var statistics = b.getStatistics();
            var tokens = b.getTokens();

            return event.reply().withEmbeds(EmbedCreateSpec.builder()
                            .color(Color.PINK)
                            .title(spec.getSimulacra() + " (" + spec.getWeapon() + ")")
                            .author("ToF Pulling Simulator", null, null)
                            .addField("", "", false)
                            .addField("SSR", statistics.getSSR().toString(), true)
                            .addField("SR", statistics.getSR().toString(), true)
                            .addField("Rare", statistics.getRare().toString(), true)
                            .addField("", "", false)
                            .addField("Normal", statistics.getNormal().toString(), true)
                            .addField(spec.getWeapon(), statistics.getWeaponBanner().toString(), true)
                            .addField("", "", true)
                            .addField("", "", false)
                            .addField("Pity", pity.getSSR().toString(), true)
                            .addField("Lost/Won", pity.getLost() + "/" + pity.getWon(), true)
                            .addField("Black gold", tokens.get().toString(), true)
                            .addField("", "", false)
                            .addField("Last", history.getLast().getName(), true)
                            .addField("Total pulls", Integer.toString(history.get().size()), true)
                            .addField("", "", true)
                            .addField("", "", false)
                            .timestamp(Instant.now())
                            .build())
                    .then();
        });
    }
}
