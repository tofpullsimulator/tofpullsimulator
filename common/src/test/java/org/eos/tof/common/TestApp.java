package org.eos.tof.common;

import lombok.extern.log4j.Log4j2;
import lombok.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;

/**
 * Main for the test application.
 *
 * @author Eos
 */
@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@Generated
@Log4j2
@SpringBootApplication
public class TestApp implements CommandLineRunner {

    @Autowired
    private BannerFactory factory;

    /**
     * Start the test application.
     *
     * @param args The arguments passed to the test application.
     */
    public static void main(final String[] args) {
        SpringApplication.run(TestApp.class, args);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run(final String... args) {
        factory.setClazz(MatrixBanner.class);
        factory.setRate(Banner.RateMode.MATRIX_NORMAL);
        factory.setSpec(Banner.Spec.FEISE);

        var banner = factory.getObject();
        if (banner instanceof MatrixBanner mBanner) {
            mBanner.boxes(1);
        }
        banner.pull(-1);

        log.info("");
        log.info("Banner:");
        if (banner instanceof MatrixBanner) {
            log.info("\tspec:                {}", banner.spec().getMatrix());
        } else {
            log.info("\tspec:                {}", banner.spec().getWeapon());
        }
        log.info("\trate:                {}", banner.rate());
        log.info("\ttheory:              {}", banner.theory());
        if (banner instanceof MatrixBanner mBanner) {
            log.info("\tblocks:              {}", mBanner.blocked());
            log.info("\tboxes:               {}", mBanner.boxes());
        }
        log.info("History:");
        log.info("\tlast:                {}", banner.history().getLast().getName());
        log.info("\tsize:                {}", banner.history().get().size());
        log.info("Pity:");
        log.info("\tSSR:                 {}", banner.pity().getSSR());
        log.info("\tSR:                  {}", banner.pity().getSR());
        log.info("\tWon:                 {}", banner.pity().getWon());
        log.info("\tWon in a row:        {}", banner.pity().getWonInARow());
        log.info("\tLost:                {}", banner.pity().getLost());
        log.info("\tLost in a row:       {}", banner.pity().getLostInARow());
        log.info("Statistics:");
        log.info("\tSSR:                 {}", banner.statistics().getSSR());
        log.info("\tSR:                  {}", banner.statistics().getSR());
        log.info("\tRare:                {}", banner.statistics().getRare());
        log.info("\tNormal:              {}", banner.statistics().getNormal());
        if (banner instanceof MatrixBanner) {
            log.info("\tTotal matrix pieces: {}", banner.statistics().getTotalMatrixPieces());
            log.info("\tBrain pieces:        {}", banner.statistics().getBrainPieces());
            log.info("\tHands pieces:        {}", banner.statistics().getHandsPieces());
            log.info("\tHead pieces:         {}", banner.statistics().getHeadPieces());
            log.info("\tHeart pieces:        {}", banner.statistics().getHeartPieces());
        }
        if (banner instanceof WeaponBanner) {
            log.info("\tBanner weapon:       {}", banner.statistics().getWeaponBanner());
        }
        log.info("Tokens:");
        if (banner instanceof MatrixBanner) {
            log.info("\tOverclocking chips:  {}", banner.tokens().getMatrixTokens());
            log.info("\tBuy brain pieces:    {}", banner.tokens().getBuyBrainPieces());
            log.info("\tBuy hands pieces:    {}", banner.tokens().getBuyHandsPieces());
            log.info("\tBuy head pieces:     {}", banner.tokens().getBuyHeadPieces());
            log.info("\tBuy heart pieces:    {}", banner.tokens().getBuyHeartPieces());
        }
        if (banner instanceof WeaponBanner) {
            log.info("\tFlame gold:          {}", banner.tokens().getWeaponTokens());
        }
    }
}
