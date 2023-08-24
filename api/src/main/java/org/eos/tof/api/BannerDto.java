package org.eos.tof.api;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import org.eos.tof.common.Banner;
import org.eos.tof.common.counters.PityCounter;
import org.eos.tof.common.counters.StatisticsCounter;
import org.eos.tof.common.items.Item;

/**
 * Record for holding the various properties of the {@link Banner}.
 *
 * @param spec       The spec of the Banner.
 * @param mode       The rate mode of the Banner.
 * @param isTheory   Whenever the banner is using the theory.
 * @param history    The history of the Banner.
 * @param pity       The pity of the Banner.
 * @param statistics The statistics of the Banner.
 * @param tokens     The token counter of the Banner.
 * @author Eos
 */
@Builder
public record BannerDto(Banner.Spec spec,
                        Banner.RateMode mode,
                        @JsonProperty("theory") boolean isTheory,
                        HistoryDto history,
                        PityDto pity,
                        StatisticsDto statistics,
                        int tokens) {

    /**
     * Converts a {@link Banner} to a {@link BannerDto}.
     *
     * @param banner The banner to be converted.
     * @return The converted DTO object.
     */
    public static BannerDto toDto(final Banner banner) {
        var history = banner.getHistory();
        var pity = banner.getPity();
        var statistics = banner.getStatistics();

        return BannerDto.builder()
                .spec(banner.getSpec())
                .mode(banner.getRate())
                .isTheory(banner.isTheory())
                .history(HistoryDto.builder()
                        .items(history.get())
                        .last(history.getLast())
                        .totalPulls(history.get().size())
                        .build())
                .pity(PityDto.builder()
                        .ssRare(pity.getSSR())
                        .sRare(pity.getSR())
                        .hit(pity.getHit())
                        .lost(pity.getLost())
                        .won(pity.getWon())
                        .lostInARow(pity.getLostInARow())
                        .wonInARow(pity.getWonInARow())
                        .build())
                .statistics(StatisticsDto.builder()
                        .ssRare(statistics.getSSR())
                        .sRare(statistics.getSR())
                        .rare(statistics.getRare())
                        .normal(statistics.getNormal())
                        .bannerWeapon(statistics.getWeaponBanner())
                        .build())
                .tokens(banner.getTokens().get())
                .build();
    }

    /**
     * Record for holding the history stack.
     *
     * @param items      The history stack itself.
     * @param last       The last item from the history stack.
     * @param totalPulls The amount of total pulls in the history stack.
     * @author Eos
     */
    @Builder
    private record HistoryDto(Collection<Item> items, Item last, int totalPulls) {

    }

    /**
     * Record for holding the metrics of the {@link PityCounter}.
     *
     * @param ssRare     The pity for super-super rare items.
     * @param sRare      The pity for super rare items.
     * @param hit        The amount of times hard pity has been hit.
     * @param lost       The amount of 50/50 lost.
     * @param won        The amount of 50/50 won.
     * @param lostInARow The amount of times lost in a row.
     * @param wonInARow  The amount of times won in a row.
     * @author Eos
     */
    @Builder
    private record PityDto(int ssRare, int sRare, int hit, int lost, int won, int lostInARow, int wonInARow) {

    }

    /**
     * Record for holding the metrics of the {@link StatisticsCounter}.
     *
     * @param ssRare       The amount of super-super rare item gotten.
     * @param sRare        The amount of super rare item gotten.
     * @param rare         The amount of rare items gotten.
     * @param normal       The amount of normal items gotten.
     * @param bannerWeapon The amount of limited weapons gotten.
     * @author Eos
     */
    @Builder
    private record StatisticsDto(int ssRare, int sRare, int rare, int normal, int bannerWeapon) {

    }
}
