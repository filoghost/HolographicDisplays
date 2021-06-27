/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.image;

import me.filoghost.holographicdisplays.plugin.disk.Settings;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.Nullable;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/*
 * Credits: https://forums.bukkit.org/threads/lib-imagemessage-v2-1-send-images-to-players-via-the-chat.204902
 */
public class ImageMessage {

    private static final List<ColorMapping> COLORS = Arrays.asList(
            new ColorMapping(new Color(0, 0, 170), ChatColor.DARK_BLUE),
            new ColorMapping(new Color(0, 170, 0), ChatColor.DARK_GREEN),
            new ColorMapping(new Color(0, 170, 170), ChatColor.DARK_AQUA),
            new ColorMapping(new Color(170, 0, 0), ChatColor.DARK_RED),
            new ColorMapping(new Color(170, 0, 170), ChatColor.DARK_PURPLE),
            new ColorMapping(new Color(255, 170, 0), ChatColor.GOLD),
            new ColorMapping(new Color(85, 85, 255), ChatColor.BLUE),
            new ColorMapping(new Color(85, 255, 85), ChatColor.GREEN),
            new ColorMapping(new Color(85, 255, 255), ChatColor.AQUA),
            new ColorMapping(new Color(255, 85, 85), ChatColor.RED),
            new ColorMapping(new Color(255, 85, 255), ChatColor.LIGHT_PURPLE),
            new ColorMapping(new Color(255, 255, 85), ChatColor.YELLOW)
    );

    private static final List<ColorMapping> GRAYS = Arrays.asList(
            new ColorMapping(new Color(0, 0, 0), ChatColor.BLACK),
            new ColorMapping(new Color(85, 85, 85), ChatColor.DARK_GRAY),
            new ColorMapping(new Color(170, 170, 170), ChatColor.GRAY),
            new ColorMapping(new Color(255, 255, 255), ChatColor.WHITE)
    );

    private final List<String> lines;

    public ImageMessage(BufferedImage image, int width) {
        this.lines = toChatLines(resizeImage(image, width));
    }

    private List<String> toChatLines(BufferedImage image) {
        List<String> lines = new ArrayList<>(image.getHeight());
        ChatColor transparencyColor = Settings.transparencyColor;
        String transparencySymbol = Settings.transparencySymbol;
        String imageSymbol = Settings.imageSymbol;

        for (int y = 0; y < image.getHeight(); y++) {
            StringBuilder line = new StringBuilder();
            ChatColor previousColor = null;

            for (int x = 0; x < image.getWidth(); x++) {
                ChatColor currentColor = getClosestChatColor(image, x, y);
                String symbol;

                if (currentColor == null) {
                    // Use the transparent char
                    currentColor = transparencyColor;
                    symbol = transparencySymbol;
                } else {
                    symbol = imageSymbol;
                }

                if (currentColor != previousColor) {
                    // Append the different color and save it
                    line.append(currentColor);
                    previousColor = currentColor;
                }
                line.append(symbol);
            }

            lines.add(line.toString());
        }

        return lines;
    }

    private BufferedImage resizeImage(BufferedImage image, int width) {
        double ratio = (double) image.getHeight() / image.getWidth();
        int height = (int) (width * ratio);
        if (height == 0) {
            height = 1;
        }
        return toBufferedImage(image.getScaledInstance(width, height, Image.SCALE_DEFAULT));
    }

    private BufferedImage toBufferedImage(Image img) {
        // Creates a buffered image with transparency
        BufferedImage bufferedImage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draws the image on to the buffered image
        Graphics2D graphics = bufferedImage.createGraphics();
        graphics.drawImage(img, 0, 0, null);
        graphics.dispose();

        return bufferedImage;
    }

    private @Nullable ChatColor getClosestChatColor(BufferedImage image, int x, int y) {
        Color color = new Color(image.getRGB(x, y), true);
        if (color.getAlpha() < 80) {
            return null;
        }

        for (ColorMapping colorMapping : COLORS) {
            if (colorMapping.isIdenticalTo(color)) {
                return colorMapping.chatColor;
            }
        }

        ColorMapping bestGrayMatch = getClosestColorMapping(GRAYS, color);

        if (bestGrayMatch.getDistance(color) < 17500) {
            return bestGrayMatch.chatColor;
        } else {
            return getClosestColorMapping(COLORS, color).chatColor;
        }
    }

    private ColorMapping getClosestColorMapping(Collection<ColorMapping> colorMappings, Color color) {
        double bestDistance = 0;
        ColorMapping bestMatch = null;

        for (ColorMapping colorMapping : colorMappings) {
            double distance = colorMapping.getDistance(color);

            if (bestMatch == null || distance < bestDistance) {
                bestMatch = colorMapping;
                bestDistance = distance;
            }
        }

        return bestMatch;
    }

    public List<String> getLines() {
        return lines;
    }


    private static class ColorMapping {

        private final Color color;
        private final ChatColor chatColor;

        ColorMapping(Color color, ChatColor chatColor) {
            this.chatColor = chatColor;
            this.color = color;
        }

        boolean isIdenticalTo(Color otherColor) {
            return Math.abs(color.getRed() - otherColor.getRed()) <= 5
                    && Math.abs(color.getGreen() - otherColor.getGreen()) <= 5
                    && Math.abs(color.getBlue() - otherColor.getBlue()) <= 5;
        }

        double getDistance(Color otherColor) {
            int redDiff = color.getRed() - otherColor.getRed();
            int greenDiff = color.getGreen() - otherColor.getGreen();
            int blueDiff = color.getBlue() - otherColor.getBlue();
            double redMean = (color.getRed() + otherColor.getRed()) / 2.0;
            double redWeight = 2 + redMean / 256.0;
            double greenWeight = 4.0;
            double blueWeight = 2 + (255 - redMean) / 256.0;

            return redWeight * redDiff * redDiff
                    + greenWeight * greenDiff * greenDiff
                    + blueWeight * blueDiff * blueDiff;
        }

    }

}
