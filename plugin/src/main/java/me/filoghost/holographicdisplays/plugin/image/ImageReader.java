/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

public class ImageReader {

    public static BufferedImage readImage(Path file) throws ImageReadException, IOException {
        BufferedImage image = ImageIO.read(Files.newInputStream(file));

        if (image == null) {
            throw new ImageReadException();
        }

        return image;
    }

    public static BufferedImage readImage(URL url) throws ImageReadException, IOException {
        BufferedImage image = ImageIO.read(url);

        if (image == null) {
            throw new ImageReadException();
        }

        return image;
    }

}
