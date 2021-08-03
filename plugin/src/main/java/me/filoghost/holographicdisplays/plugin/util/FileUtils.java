/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.util;

import me.filoghost.fcommons.Strings;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;


public class FileUtils {

    public static boolean hasFileExtension(@NotNull Path file, @NotNull String... validExtensions) {
        return hasFileExtension(file.getFileName().toString(), validExtensions);
    }

    public static boolean hasFileExtension(@NotNull String fileName, @NotNull String... validExtensions) {
        int extensionBeginIndex = fileName.lastIndexOf('.');

        if (extensionBeginIndex < 0) {
            return false;
        }

        String extension = fileName.substring(extensionBeginIndex + 1);
        return Strings.containsIgnoreCase(validExtensions, extension);
    }

    public static String removeFileExtension(@NotNull String fileName) {
        int extensionBeginIndex = fileName.lastIndexOf('.');

        if (extensionBeginIndex < 0) {
            return fileName;
        }

        return fileName.substring(0, extensionBeginIndex);
    }

    public static boolean isInsideDirectory(Path file, Path directory) {
        Path canonicalFile = file.toAbsolutePath().normalize();
        Path canonicalDirectory = directory.toAbsolutePath().normalize();

        return canonicalFile.getNameCount() > canonicalDirectory.getNameCount() && canonicalFile.startsWith(canonicalDirectory);
    }

}
