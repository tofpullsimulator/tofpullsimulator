package org.eos.tof.bot.commands.banner;

import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;

import lombok.extern.log4j.Log4j2;

/**
 * Class with helper function for the banner subcommands.
 *
 * @author Eos
 */
@Log4j2
public abstract class AbstractBannerSubCommand {

    /**
     * Get the value of the name option.
     *
     * @param option   The subcommand option.
     * @param fallBack The fallback value if non was set by the user.
     * @return The name of banner to be requested.
     */
    String getName(final ApplicationCommandInteractionOption option, final String fallBack) {
        return option.getOption("name")
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asString)
                .orElse(fallBack);
    }

    /**
     * Get the value of the theory option.
     *
     * @param option The subcommand option.
     * @return Whenever to use theory mode for the banner.
     */
    boolean isTheory(final ApplicationCommandInteractionOption option) {
        return option.getOption("theory")
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asBoolean)
                .orElse(true);
    }

    /**
     * Get the value of the amount option.
     *
     * @param option The subcommand option.
     * @return The amount of pulls to be done on the banner.
     */
    long getAmount(final ApplicationCommandInteractionOption option) {
        return option.getOption("amount")
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asLong)
                .orElse(-1L);
    }
}
