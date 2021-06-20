/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.util;

import java.nio.file.Path;


public class FileUtils {

    public static String getExtension(String fileName) {
        int lastFullStop = fileName.lastIndexOf('.');
        if (lastFullStop >= 0) {
            return fileName.substring(lastFullStop + 1);
        } else {
            return "";
        }
    }

    public static boolean isInsideDirectory(Path file, Path directory) {
        Path canonicalFile = file.toAbsolutePath().normalize();
        Path canonicalDirectory = directory.toAbsolutePath().normalize();

        return canonicalFile.getNameCount() > canonicalDirectory.getNameCount() && canonicalFile.startsWith(canonicalDirectory);
    }

}
