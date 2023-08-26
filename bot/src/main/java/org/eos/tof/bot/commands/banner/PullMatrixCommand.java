package org.eos.tof.bot.commands.banner;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.entity.Member;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Color;

import java.time.Instant;
import java.util.Optional;

import org.eos.tof.bot.BannerService;
import org.eos.tof.bot.commands.SlashSubCommand;
import org.eos.tof.common.Banner;
import org.eos.tof.common.MatrixBanner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * Command for pulling on the matrix banner of the member.
 *
 * @author Eos
 */
@Component
public class PullMatrixCommand extends PullWeaponCommand implements SlashSubCommand {

    /**
     * Create a new command to pull on a matrix banner.
     *
     * @param service             The banner service to pull on matrix banners with.
     * @param createWeaponCommand The command to create new weapon banners with.
     */
    public PullMatrixCommand(final BannerService service, final CreateWeaponCommand createWeaponCommand) {
        super(service, createWeaponCommand);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return "banner pull-matrix";
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

        var banner = service.pull(id, name, isTheory, amount, MatrixBanner.class);
        return bannerToReply(event, banner);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Mono<Void> bannerToReply(final ChatInputInteractionEvent event, final Mono<Banner> banner) {
        return banner.flatMap(b -> {
            var spec = b.spec();
            var history = b.history();
            var pity = b.pity();
            var statistics = b.statistics();
            var tokens = b.tokens();
            var blocked = ((MatrixBanner) b).blocked();

            return event.reply().withEmbeds(EmbedCreateSpec.builder()
                            .color(Color.PINK)
                            .title(spec.getSimulacra() + " (" + spec.getWeapon() + ")")
                            .author("ToF Pulling Simulator", null, null)
                            .addField("SSR", statistics.getSSR().toString(), true)
                            .addField("SR", statistics.getSR().toString(), true)
                            .addField("Rare", statistics.getRare().toString(), true)
                            .addField("Pity", pity.getSSR().toString(), true)
                            .addField("Lost/Won", pity.getLost() + "/" + pity.getWon(), true)
                            .addField("Overclocking chips", tokens.getMatrixTokens().toString(), true)
                            .addField(spec.getMatrix(), statistics.getTotalMatrixPieces().toString(), true)
                            .addField("Last", history.getLast().getName(), true)
                            .addField("Total pulls", Integer.toString(history.get().size()), true)
                            .addField("Brain pieces", statistics.getBrainPieces().toString(), true)
                            .addField("Hands pieces", statistics.getHandsPieces().toString(), true)
                            .addField("Head pieces", statistics.getHeadPieces().toString(), true)
                            .addField("Heart pieces", statistics.getHeartPieces().toString(), true)
                            .addField("Boxes", Integer.toString(((MatrixBanner) b).boxes()), true)
                            .addField("Blocking", blocked == null ? "None" : blocked.toString(), true)
                            .addField("", "", true)
                            .timestamp(Instant.now())
                            .build())
                    .then();
        });
    }
}
