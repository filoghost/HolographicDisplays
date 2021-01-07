/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.util;

import me.filoghost.holographicdisplays.exception.UnreadableImageException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;


public class FileUtils {
    
    public static BufferedImage readImage(Path file) throws UnreadableImageException, IOException {
        BufferedImage image = ImageIO.read(Files.newInputStream(file));
        
        if (image == null) {
            throw new UnreadableImageException();
        }
            
        return image;
    }
    
    public static BufferedImage readImage(URL url) throws UnreadableImageException, IOException {
        BufferedImage image = ImageIO.read(url);
        
        if (image == null) {
            throw new UnreadableImageException();
        }
            
        return image;
    }

    public static boolean isInsideDirectory(Path file, Path directory) {
        Path canonicalFile = file.toAbsolutePath().normalize();
        Path canonicalDirectory = directory.toAbsolutePath().normalize();

        return canonicalFile.getNameCount() > canonicalDirectory.getNameCount() && canonicalFile.startsWith(canonicalDirectory);
    }

}
