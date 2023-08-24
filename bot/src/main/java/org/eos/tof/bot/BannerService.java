package org.eos.tof.bot;

import java.util.Objects;
import java.util.concurrent.Callable;

import lombok.AllArgsConstructor;
import org.eos.tof.common.Banner;
import org.eos.tof.common.BannerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Service for creating, pulling and resetting the banner. Banners are kept in cache where the key is the id of the
 * user.
 *
 * @author Eos
 */
@AllArgsConstructor
@Service
@EnableCaching
@CacheConfig(cacheNames = "banners")
public class BannerService {

    private final BannerFactory factory;
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private final CacheManager cacheManager;

    /**
     * Get the current banner of the member.
     *
     * @param member The id of the member to get the banner for.
     * @return The current banner of the user.
     * @throws NullPointerException If no banner was found in the cache.
     */
    public Mono<Banner> get(final long member) {
        return get(member, false);
    }

    /**
     * Get the current banner of the member.
     *
     * @param member The id of the member to get the banner for.
     * @param evict  Evict the banner from the cache if it was present.
     * @return The current banner of the user.
     * @throws NullPointerException If no banner was found in the cache.
     */
    public Mono<Banner> get(final long member, final boolean evict) {
        Cache cache = getCache();
        var cached = cache.get(member, Banner.class);
        if (cached != null && evict) {
            cache.evict(member);
            cached.reset();
        }

        return Mono.justOrEmpty(cached);
    }

    /**
     * Get the current banner of the member.
     *
     * @param member The id of the member to get the banner for.
     * @param name   The name of the banner to get. If the member already has a banner in the cache, but the name of the
     *               cached banner is different from the given on it will create a new banner.
     * @return The current banner of the user.
     * @throws NullPointerException If no banner was found in the cache.
     */
    public Mono<Banner> get(final long member, final String name) {
        return get(member, name, true, false);
    }

    /**
     * Get the current banner of the member.
     *
     * @param member The id of the member to get the banner for.
     * @param name   The name of the banner to get. If the member already has a banner in the cache, but the name of
     *               the cached banner is different from the given on it will create a new banner.
     * @param theory Use the theory mode for banner.
     * @return The current banner of the user.
     * @throws NullPointerException If no banner was found in the cache.
     */
    public Mono<Banner> get(final long member, final String name, final boolean theory) {
        return get(member, name, theory, false);
    }

    /**
     * Get the current banner of the member.
     *
     * @param member The id of the member to get the banner for.
     * @param name   The name of the banner to get. If the member already has a banner in the cache, but the name of
     *               the cached banner is different from the given on it will create a new banner.
     * @param theory Use the theory mode for banner.
     * @param evict  Evict the banner from the cache if it was present.
     * @return The current banner of the user.
     * @throws NullPointerException If no banner was found in the cache.
     */
    public Mono<Banner> get(final long member, final String name, final boolean theory, final boolean evict) {
        Cache cache = getCache();
        Banner cached = cache.get(member, getter(name, theory));

        boolean shouldEvict = cached != null && (cached.getSpec() != Banner.Spec.from(name) || evict);
        if (shouldEvict) {
            cache.evict(member);
            cached.reset();
        }

        var result = cache.get(member, getter(name, theory));
        return Mono.justOrEmpty(result);
    }

    /**
     * Pull on the banner a certain amount of times.
     *
     * @param member The id of the member to get the banner for.
     * @param amount The amount of pulls to be done on the banner.
     * @return The current banner of the user.
     * @throws NullPointerException If no banner was found in the cache.
     */
    public Mono<Banner> pull(final long member, final long amount) {
        return get(member).map(banner -> banner.pull(amount));
    }

    /**
     * Pull on the banner a certain amount of times.
     *
     * @param member The id of the member to get the banner for.
     * @param name   The name of the banner to get. If the member already has a banner in the cache, but the name of
     *               the cached banner is different from the given on it will create a new banner.
     * @param theory Use the theory mode for banner.
     * @param amount The amount of pulls to be done on the banner.
     * @return The current banner of the user.
     * @throws NullPointerException If no banner was found in the cache.
     */
    public Mono<Banner> pull(final long member, final String name, final boolean theory, final long amount) {
        return get(member, name, theory, false)
                .map(banner -> banner.pull(amount));
    }

    /**
     * Reset the current banner of the member.
     *
     * @param member The id of the member to get the banner for.
     * @return The current banner of the user.
     * @throws NullPointerException If no banner was found in the cache.
     */
    public Mono<Banner> reset(final long member) {
        return get(member, true);
    }

    /**
     * Callable getter for creating the banner via the {@link BannerFactory}.
     *
     * @param name   The name of the banner to get. If the member already has a banner in the cache, but the name of
     *               the cached banner is different from the given on it will create a new banner.
     * @param theory Use the theory mode for banner.
     * @return A newly created banner.
     */
    private Callable<Banner> getter(final String name, final boolean theory) {
        return () -> {
            try {
                Banner.Spec spec = Banner.Spec.from(name);
                factory.setSpec(spec);
                factory.setTheory(theory);

                return factory.getObject();
            } catch (IllegalArgumentException e) {
                return null;
            }
        };
    }

    /**
     * Get the cache to store the banners in.
     *
     * @return The cache of the banners.
     */
    Cache getCache() {
        var cache = cacheManager.getCache("banners");
        Objects.requireNonNull(cache, "");

        return cache;
    }
}
