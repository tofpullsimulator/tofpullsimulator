package org.eos.tof.common;

import java.awt.Color;
import java.awt.Rectangle;
import java.io.IOException;

public class WeaponDrawer extends Drawer {

    public WeaponDrawer(final Banner banner) {
        super(banner);
    }

    @Override
    protected void drawRectangles() {
        g2d.setColor(Drawer.GRAY);
        g2d.fill(new Rectangle(5, 30, 100, 30));
        g2d.fill(new Rectangle(120, 30, 100, 30));
        g2d.fill(new Rectangle(235, 30, 100, 30));
        g2d.fill(new Rectangle(350, 30, 100, 30));

        g2d.fill(new Rectangle(width - 450, 30, 100, 30));
        g2d.fill(new Rectangle(width - 335, 30, 100, 30));
        g2d.fill(new Rectangle(width - 220, 30, 100, 30));
        g2d.fill(new Rectangle(width - 105, 30, 100, 30));
    }

    @Override
    protected void drawIcons() throws IOException {
        drawIcon(Drawer.GRADE_N, -15, 34);
        drawIcon(Drawer.GRADE_R, 100, 34);
        drawIcon(Drawer.GRADE_SR, 223, 34);
        drawIcon(Drawer.GRADE_SSR, 348, 34);

        drawIcon("Flame_Gold.png", 64, 64, width - 470, 13);
        drawIcon("Fusion_Core.png", 64, 64, width - 355, 13);

        g2d.setColor(Drawer.WHITE);
        g2d.drawString("W/L:", width - 215, 52);
        g2d.drawString("Total:", width - 100, 52);
    }

    @Override
    protected void drawValues() {
        g2d.setColor(Drawer.WHITE);
        rightAlign(banner.statistics().getNormal().toString(), 52, 1400);
        rightAlign(banner.statistics().getRare().toString(), 52, 1285);
        rightAlign(banner.statistics().getSR().toString(), 52, 1170);
        rightAlign(banner.statistics().getSSR().toString(), 52, 1055);

        rightAlign(banner.tokens().getWeaponTokens().toString(), 52, 355);
        rightAlign(banner.statistics().getWeaponBanner().toString(), 52, 240);
        String wonLost = banner.pity().getWon().toString() + "/" + banner.pity().getLost().toString();
        rightAlign(wonLost, 52, 125);
        rightAlign(Integer.toString(banner.history().get().size()), 52, 10);
    }

    @Override
    protected void drawPity() {
        g2d.setColor(Drawer.GRAY);
        int barLength = 500;
        g2d.fill(new Rectangle((width - barLength) / 2, 37, barLength, 15));

        Integer pity = banner.pity().getSSR();
        g2d.setColor(Color.white);
        g2d.fill(new Rectangle((width - barLength) / 2, 37, barLength / 80 * pity, 15));

        String pityString = pity + "/80";
        int stringWidth = metrics.stringWidth(pityString);
        if (stringWidth == 35) {
            g2d.drawString(pityString, (width - stringWidth) / 2 - 5, 72);
        } else {
            g2d.drawString(pityString, (width - stringWidth) / 2 - 10, 72);
        }
    }

    @Override
    protected void drawItems() throws IOException {
        for (int i = 0; i < amount; i++) {
            drawItem("weapons", i);
        }
    }
}
