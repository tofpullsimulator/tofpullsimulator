package org.eos.tof.bot.commands.banner;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.entity.Member;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.MessageCreateFields;
import discord4j.rest.util.Color;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.Optional;

import org.eos.tof.bot.BannerService;
import org.eos.tof.bot.commands.SlashSubCommand;
import org.eos.tof.common.Banner;
import org.eos.tof.common.MatrixBanner;
import org.eos.tof.common.MatrixDrawer;
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
            try {
                var spec = b.spec();

                Optional<Member> member = event.getInteraction().getMember();
                @SuppressWarnings({"java:S3655", "OptionalGetWithoutIsPresent"})
                long id = member.get().getUserData().id().asLong();
                String name = "banner-" + id + ".png";

                var drawer = new MatrixDrawer(b);
                drawer.draw();
                drawer.save(name, "png");
                var in = new FileInputStream(name);

                return event.reply()
                        .withFiles(MessageCreateFields.File.of(name, in))
                        .withEmbeds(EmbedCreateSpec.builder()
                                .color(Color.PINK)
                                .title(spec.getSimulacra() + " (" + spec.getMatrix() + ")")
                                .author("ToF Pulling Simulator", null, null)
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
