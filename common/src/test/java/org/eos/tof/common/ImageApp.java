package org.eos.tof.common;

import java.io.IOException;

import lombok.extern.log4j.Log4j2;
import lombok.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;

/**
 * Main for the image application.
 *
 * @author Eos
 */
@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@Generated
@Log4j2
@SpringBootApplication
public class ImageApp implements CommandLineRunner {

    @Autowired
    private BannerFactory factory;

    /**
     * Start the image application.
     *
     * @param args The arguments passed to the test application.
     */
    public static void main(final String[] args) {
        SpringApplication.run(ImageApp.class, args);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run(final String... args) throws IOException {
        factory.setClazz(WeaponBanner.class);
        factory.setRate(Banner.RateMode.MATRIX_NORMAL);
        factory.setSpec(Banner.Spec.YULAN);

        var banner = factory.getObject();
        banner.pull(80);

        var drawer = new WeaponDrawer(banner);
        drawer.draw();
        drawer.save(banner.spec().getSimulacra() + ".png", "png");

        log.info("Done");
    }
}

