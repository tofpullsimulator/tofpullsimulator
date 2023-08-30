package org.eos.tof.common;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.text.Normalizer;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;

import org.eos.tof.common.items.Item;
import org.eos.tof.common.items.Normal;
import org.eos.tof.common.items.Rare;
import org.eos.tof.common.items.SRare;
import org.eos.tof.common.items.SSRItem;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

public abstract class Drawer {

    protected static final Font BIG_FONT = new Font("Arial", Font.PLAIN, 18);
    protected static final Font SMALL_FONT = new Font("Arial", Font.BOLD, 12);
    protected static final Color WHITE = Color.white;
    protected static final Color GRAY = new Color(55, 75, 125, 80);

    protected static final String GRADE_N = "Icon_Grade_N.png";
    protected static final String GRADE_R = "Icon_Grade_R.png";
    protected static final String GRADE_SR = "Icon_Grade_SR.png";
    protected static final String GRADE_SSR = "Icon_Grade_SSR.png";

    private static final Pattern NON_LATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("\\s");

    protected final Banner banner;
    protected final PathMatchingResourcePatternResolver matcher;

    protected final BufferedImage image;
    protected final Graphics2D g2d;
    protected final FontMetrics metrics;

    protected final int amount;
    protected final int gaps;
    protected final int width;

    Drawer(final Banner banner) {
        this.banner = banner;
        this.matcher = new PathMatchingResourcePatternResolver();

        this.amount = Math.min(banner.history().get().size(), 10);
        this.gaps = 2 * 10;
        this.width = 150 * 10;
        int height = 600;

        this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        this.g2d = image.createGraphics();
        this.g2d.setFont(BIG_FONT);
        this.metrics = g2d.getFontMetrics(BIG_FONT);
    }

    public void draw() throws IOException {
        drawRectangles();
        drawIcons();
        drawValues();
        drawPity();
        drawItems();
        g2d.dispose();
    }

    public void save(final String name, final String format) throws IOException {
        File file = new File(name);
        ImageIO.write(image, format, file);
    }

    protected abstract void drawRectangles();

    protected abstract void drawIcons() throws IOException;

    protected abstract void drawValues();

    protected abstract void drawPity();

    protected abstract void drawItems() throws IOException;

    protected void drawIcon(final String name, final int x, final int y) throws IOException {
        drawIcon(name, 48, 24, x, y);
    }

    protected void drawIcon(final String name, final int width, final int height, final int x, final int y)
            throws IOException {
        Resource resource = matcher.getResource("icons/" + name);
        Image normal = ImageIO.read(resource.getInputStream());
        normal = normal.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        g2d.drawImage(normal, x, y, null);
    }

    protected void drawItem(final String path, final int i) throws IOException {
        Item item = banner.history().get().get(banner.history().get().size() - amount + i);

        String resourceName = slugify(item.getName());
        if (item instanceof SSRItem ssrItem && !ssrItem.isStandard()) {
            if (this instanceof MatrixDrawer) {
                resourceName = slugify(banner.spec().getMatrix());
            } else if (this instanceof WeaponDrawer) {
                resourceName = slugify(banner.spec().getWeapon());
            }
        }

        Resource resource = matcher.getResource(path + "/" + resourceName + ".png");
        BufferedImage subImage = ImageIO.read(resource.getInputStream());
        subImage = subImage.getSubimage(0, 0, subImage.getWidth() - (gaps / amount), subImage.getHeight());
        int w = i * ((width / amount) + (gaps / amount));
        g2d.drawImage(subImage, w, 100, null);

        g2d.setColor(Drawer.GRAY);
        g2d.fill(new Rectangle(w, 500, subImage.getWidth(), 20));

        g2d.setColor(Drawer.WHITE);
        g2d.setFont(Drawer.SMALL_FONT);
        int t = g2d.getFontMetrics(Drawer.SMALL_FONT).stringWidth(item.getName());
        int center = (w + (i + 1) * ((width / amount) + (gaps / amount)) - t) / 2;
        g2d.drawString(item.getName(), center, 515);

        if (item instanceof SSRItem) {
            drawIcon(Drawer.GRADE_SSR, 32, 32, w + 12, 120);
        } else if (item instanceof SRare) {
            drawIcon(Drawer.GRADE_SR, 32, 32, w + 12, 120);
        } else if (item instanceof Rare) {
            drawIcon(Drawer.GRADE_R, 32, 32, w + 12, 120);
        } else if (item instanceof Normal) {
            drawIcon(Drawer.GRADE_N, 32, 32, w + 12, 120);
        }
    }

    protected void rightAlign(final Graphics2D g2d, final FontMetrics metrics, final String str,
                              final int y, final int additionalWidth) {
        int w = metrics.stringWidth(str);
        if (w == 10) {
            w += 80 + additionalWidth;
        } else if (w == 20) {
            w += 60 + additionalWidth;
        } else if (w == 25) {
            w += 65 + additionalWidth;
        } else if (w == 30) {
            w += 40 + additionalWidth;
        } else if (w == 35) {
            w += 45 + additionalWidth;
        } else if (w == 45) {
            w += 25 + additionalWidth;
        }

        g2d.drawString(str, w, y);
    }

    protected String slugify(final String input) {
        String noWhiteSpace = WHITESPACE.matcher(input).replaceAll("_");
        String normalized = Normalizer.normalize(noWhiteSpace, Normalizer.Form.NFD);

        return NON_LATIN.matcher(normalized).replaceAll("");
    }
}

