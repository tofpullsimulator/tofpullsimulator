package org.eos.tof.bot.commands.banner;

import discord4j.core.event.domain.interaction.ChatInputAutoCompleteEvent;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.entity.Member;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.MessageCreateFields;
import discord4j.discordjson.json.ApplicationCommandOptionChoiceData;
import discord4j.rest.util.Color;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import lombok.AllArgsConstructor;
import org.eos.tof.bot.BannerService;
import org.eos.tof.bot.commands.SlashSubCommand;
import org.eos.tof.common.Banner;
import org.eos.tof.common.WeaponDrawer;
import org.eos.tof.common.WeaponBanner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * Command for pulling on the weapon banner of the member.
 *
 * @author Eos
 */
@AllArgsConstructor
@Component
public class PullWeaponCommand extends AbstractBannerSubCommand implements SlashSubCommand {

    /**
     * The banner service to pull on matrix banners with.
     */
    protected final BannerService service;
    /**
     * The command to create new weapon banners with.
     */
    protected final CreateWeaponCommand createWeaponCommand;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return "banner pull-weapon";
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("DuplicatedCode")
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

        var banner = service.pull(id, name, isTheory, amount, WeaponBanner.class);
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
                    ApplicationCommandOptionChoiceData.builder().name("Pull to max").value("-1").build(),
                    ApplicationCommandOptionChoiceData.builder().name("80 pulls").value("80").build(),
                    ApplicationCommandOptionChoiceData.builder().name("40 pulls").value("40").build(),
                    ApplicationCommandOptionChoiceData.builder().name("10 pulls").value("10").build(),
                    ApplicationCommandOptionChoiceData.builder().name("1 pull").value("1").build()
            );

            return event.respondWithSuggestions(suggestions);
        }

        return createWeaponCommand.handle(event, option);
    }

    /**
     * Convert the banner to a chat reply.
     *
     * @param event  The chat interaction event to be handled.
     * @param banner The banner to convert.
     * @return A reply with information from the banner.
     */
    protected Mono<Void> bannerToReply(final ChatInputInteractionEvent event, final Mono<Banner> banner) {
        return banner.flatMap(b -> {
            try {
                var spec = b.spec();

                Optional<Member> member = event.getInteraction().getMember();
                @SuppressWarnings({"java:S3655", "OptionalGetWithoutIsPresent"})
                long id = member.get().getUserData().id().asLong();
                String name = "banner-" + id + ".png";

                var drawer = new WeaponDrawer(b);
                drawer.draw();
                drawer.save(name, "png");
                var in = new FileInputStream(name);

                return event.reply()
                        .withFiles(MessageCreateFields.File.of(name, in))
                        .withEmbeds(EmbedCreateSpec.builder()
                                .color(Color.PINK)
                                .title(spec.getSimulacra() + " (" + spec.getWeapon() + ")")
                                .image("attachment://" + name)
                                .timestamp(Instant.now())
                                .build())
                        .then();
            } catch (IOException e) {
                return Mono.error(e);
            }
        });
    }
}
