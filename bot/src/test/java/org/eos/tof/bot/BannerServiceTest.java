package org.eos.tof.bot;

import org.eos.tof.common.Banner;
import org.eos.tof.common.WeaponBanner;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import reactor.test.StepVerifier;

@SpringBootTest(classes = {BannerService.class})
@ComponentScan(basePackages = {"org.eos.tof.common"})
@EnableAutoConfiguration
class BannerServiceTest {

    @Autowired
    private BannerService service;

    @BeforeEach
    void setUp() {
        service.getCache().invalidate();
    }

    @Test
    void shouldGetABannerWhichIsCached() {
        var banner = service.get(1L, "Yu Lan", true, WeaponBanner.class).block();
        Assertions.assertNotNull(banner);
        Assertions.assertInstanceOf(Banner.class, banner);

        var mono = service.get(1L, "Yu Lan", true, WeaponBanner.class);
        StepVerifier.create(mono)
                .expectNext(banner)
                .verifyComplete();
    }

    @Test
    void shouldOverrideAnExistingBanner() {
        var banner = service.get(1L, "Yu Lan", true, WeaponBanner.class).block();
        Assertions.assertNotNull(banner);
        Assertions.assertInstanceOf(Banner.class, banner);

        var mono = service.get(1L, "Alyss", true, WeaponBanner.class);
        StepVerifier.create(mono)
                .assertNext(cached -> Assertions.assertNotEquals(banner, cached))
                .verifyComplete();
    }

    @Test
    void shouldThrowAnErrorWhenInvalidSpec() {
        var mono = service.get(1L, "invalid", true, WeaponBanner.class);
        StepVerifier.create(mono).verifyComplete();
    }

    @Test
    void shouldGetBannerWithTheoryEnabled() {
        var mono = service.get(1L, "Yu Lan", WeaponBanner.class);
        StepVerifier.create(mono)
                .assertNext(cache -> Assertions.assertTrue(cache.theory()))
                .verifyComplete();
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    void shouldGetFromCache(final boolean evict) {
        var banner = service.get(1L, "Yu Lan", WeaponBanner.class).block();
        var mono = service.get(1L, evict);
        StepVerifier.create(mono)
                .assertNext(cache -> Assertions.assertEquals(banner, cache))
                .verifyComplete();
    }

    @Test
    void shouldThrowAnErrorWhenNoEmptyCache() {
        var mono = service.get(1L);
        StepVerifier.create(mono).verifyComplete();
    }

    @Test
    void shouldOverwriteExistingBanner() {
        var banner = service.get(1L, "Yu Lan", WeaponBanner.class).block();
        var mono = service.get(1L, "Yu Lan", true, true, WeaponBanner.class);
        StepVerifier.create(mono)
                .assertNext(cache -> Assertions.assertNotEquals(banner, cache))
                .verifyComplete();
    }

    @Test
    void shouldPullOnAnExistingBanner() {
        var banner = service.get(1L, "Yu Lan", WeaponBanner.class).block();
        var mono = service.pull(1L, 10L);
        StepVerifier.create(mono)
                .assertNext(cached -> {
                    Assertions.assertEquals(banner, cached);
                    Assertions.assertEquals(10, cached.pity().getSSR());
                })
                .verifyComplete();
    }

    @Test
    void shouldPullOnANewBanner() {
        var banner = service.get(1L, "Yu Lan", WeaponBanner.class).block();
        var mono = service.pull(1L, "Alyss", true, 10L, WeaponBanner.class);
        StepVerifier.create(mono)
                .assertNext(cached -> {
                    Assertions.assertNotEquals(banner, cached);
                    Assertions.assertEquals(10, cached.pity().getSSR());
                })
                .verifyComplete();
    }

    @Test
    void shouldResetABanner() {
        var banner = service.pull(1L, "Yu Lan", true, 10L, WeaponBanner.class).block();
        var mono = service.reset(1L);
        StepVerifier.create(mono)
                .assertNext(cached -> {
                    Assertions.assertEquals(banner, cached);
                    Assertions.assertEquals(0, cached.pity().getSSR());
                })
                .verifyComplete();
    }
}
