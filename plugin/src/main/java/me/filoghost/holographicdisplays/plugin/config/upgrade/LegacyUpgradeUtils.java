/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.config.upgrade;

import java.nio.file.Path;

class LegacyUpgradeUtils {

    private static final String BACKUP_FILE_EXTENSION = ".backup";

    static Path getBackupFile(Path file) {
        return file.resolveSibling(file.getFileName() + BACKUP_FILE_EXTENSION);
    }

    static boolean isBackupFile(Path file) {
        return file.getFileName().toString().toLowerCase().endsWith(BACKUP_FILE_EXTENSION);
    }

    static boolean hasFileExtension(String fileName, String extension) {
        int extensionBeginIndex = fileName.lastIndexOf('.');

        if (extensionBeginIndex < 0) {
            return false;
        }

        return fileName.substring(extensionBeginIndex + 1).equalsIgnoreCase(extension);
    }

    static String removeFileExtension(String fileName) {
        int extensionBeginIndex = fileName.lastIndexOf('.');

        if (extensionBeginIndex < 0) {
            return fileName;
        }

        return fileName.substring(0, extensionBeginIndex);
    }

}
