package org.eos.tof.common;

import java.awt.Color;
import java.awt.Rectangle;
import java.io.IOException;

import org.eos.tof.common.items.Item;
import org.eos.tof.common.items.Matrix;

public class MatrixDrawer extends Drawer {

    public MatrixDrawer(final Banner banner) {
        super(banner);
    }

    @Override
    protected void drawRectangles() {
        g2d.setColor(Drawer.GRAY);
        g2d.fill(new Rectangle(5, 10, 100, 30));
        g2d.fill(new Rectangle(120, 10, 100, 30));
        g2d.fill(new Rectangle(235, 10, 100, 30));
        g2d.fill(new Rectangle(350, 10, 100, 30));

        g2d.fill(new Rectangle(5, 50, 100, 30));
        g2d.fill(new Rectangle(120, 50, 100, 30));
        g2d.fill(new Rectangle(235, 50, 100, 30));
        g2d.fill(new Rectangle(350, 50, 100, 30));

        g2d.fill(new Rectangle(width - 450, 10, 100, 30));
        g2d.fill(new Rectangle(width - 335, 10, 100, 30));
        g2d.fill(new Rectangle(width - 220, 10, 100, 30));
        g2d.fill(new Rectangle(width - 105, 10, 100, 30));

        g2d.fill(new Rectangle(width - 450, 50, 100, 30));
        g2d.fill(new Rectangle(width - 335, 50, 100, 30));
        g2d.fill(new Rectangle(width - 220, 50, 100, 30));
        g2d.fill(new Rectangle(width - 105, 50, 100, 30));
    }

    @Override
    protected void drawIcons() throws IOException {
        drawIcon("Icon_MatrixType_Brain.png", 42, 42, 0, 5);
        drawIcon("Icon_MatrixType_Hands.png", 42, 42, 114, 5);
        drawIcon("Icon_MatrixType_Head.png", 42, 42, 228, 5);
        drawIcon("Icon_MatrixType_Heart.png", 42, 42, 342, 5);

        drawIcon("Icon_MatrixType_Brain.png", 42, 42, 0, 45);
        drawIcon("Icon_MatrixType_Hands.png", 42, 42, 114, 45);
        drawIcon("Icon_MatrixType_Head.png", 42, 42, 228, 45);
        drawIcon("Icon_MatrixType_Heart.png", 42, 42, 342, 45);

        drawIcon(Drawer.GRADE_N, 1030, 14);
        drawIcon(Drawer.GRADE_R, 1145, 14);
        drawIcon(Drawer.GRADE_SR, 1268, 14);
        drawIcon(Drawer.GRADE_SSR, 1390, 14);

        drawIcon("Overclocking_Chip.png", 64, 64, width - 470, 33);
        drawIcon("Fusion_Core.png", 64, 64, width - 355, 33);

        g2d.setColor(Drawer.WHITE);
        g2d.drawString("W/L:", width - 215, 72);
        g2d.drawString("Total:", width - 100, 72);

        g2d.drawString("buy", 40, 32);
        g2d.drawString("buy", 154, 32);
        g2d.drawString("buy", 268, 32);
        g2d.drawString("buy", 382, 32);
    }

    @Override
    protected void drawValues() {
        g2d.setColor(Drawer.WHITE);
        rightAlign(g2d, metrics, banner.tokens().getBuyBrainPieces().toString(), 32, 0);
        rightAlign(g2d, metrics, banner.tokens().getBuyHandsPieces().toString(), 32, 115);
        rightAlign(g2d, metrics, banner.tokens().getBuyHeadPieces().toString(), 32, 230);
        rightAlign(g2d, metrics, banner.tokens().getBuyHeartPieces().toString(), 32, 345);

        rightAlign(g2d, metrics, banner.statistics().getBrainPieces().toString(), 72, 0);
        rightAlign(g2d, metrics, banner.statistics().getHandsPieces().toString(), 72, 115);
        rightAlign(g2d, metrics, banner.statistics().getHeadPieces().toString(), 72, 230);
        rightAlign(g2d, metrics, banner.statistics().getHeartPieces().toString(), 72, 345);

        rightAlign(g2d, metrics, banner.statistics().getNormal().toString(), 32, 1045);
        rightAlign(g2d, metrics, banner.statistics().getRare().toString(), 32, 1160);
        rightAlign(g2d, metrics, banner.statistics().getSR().toString(), 32, 1275);
        rightAlign(g2d, metrics, banner.statistics().getSSR().toString(), 32, 1390);

        rightAlign(g2d, metrics, banner.tokens().getWeaponTokens().toString(), 72, 1045);
        rightAlign(g2d, metrics, banner.statistics().getWeaponBanner().toString(), 72, 1160);
        String lostWon = banner.pity().getWon().toString() + "/" + banner.pity().getLost().toString();
        rightAlign(g2d, metrics, lostWon, 72, 1260);
        rightAlign(g2d, metrics, Integer.toString(banner.history().get().size()), 72, 1390);
    }

    @Override
    protected void drawPity() {
        g2d.setColor(Drawer.GRAY);
        int barLength = 500;
        g2d.fill(new Rectangle((width - barLength) / 2, 37, barLength, 15));

        Integer pity = banner.pity().getSSR();
        g2d.setColor(Color.white);
        g2d.fill(new Rectangle((width - barLength) / 2, 37, barLength / 40 * pity, 15));

        String pityString = pity + "/40";
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
            drawItem("matrices", i);

            Item item = banner.history().get().get(banner.history().get().size() - amount + i);
            if (item instanceof Matrix matrix) {
                int w = i * ((width / amount) + (gaps / amount));
                drawIcon("Icon_MatrixType_" + matrix.getPosition().toString() + ".png", 32, 32, w + 12, 160);
            }
        }
    }

}
