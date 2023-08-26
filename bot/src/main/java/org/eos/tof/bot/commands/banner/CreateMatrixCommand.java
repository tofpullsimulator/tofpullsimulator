package org.eos.tof.bot.commands.banner;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.entity.Member;
import org.eos.tof.bot.BannerService;
import org.eos.tof.bot.commands.SlashSubCommand;
import org.eos.tof.common.Banner;
import org.eos.tof.common.MatrixBanner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * Command for creating a new matrix banner for the member.
 *
 * @author Eos
 */
@Component
public class CreateMatrixCommand extends CreateWeaponCommand implements SlashSubCommand {

    /**
     * Create a new command to create a matrix banner.
     *
     * @param service The banner service to create matrix banners with.
     */
    public CreateMatrixCommand(final BannerService service) {
        super(service);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return "banner create-matrix";
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

        return service.get(id, name, isTheory, true, MatrixBanner.class).flatMap(b -> {
            var spec = b.spec();
            return event.reply()
                    .withEphemeral(true)
                    .withContent("Created matrix banner '" + spec.getSimulacra() + " (" + spec.getMatrix() + ")'")
                    .then();
        });
    }
}
