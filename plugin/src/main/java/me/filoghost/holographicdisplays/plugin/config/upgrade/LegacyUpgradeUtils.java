/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.config.upgrade;

import me.filoghost.holographicdisplays.plugin.util.FileUtils;

import java.nio.file.Path;

class LegacyUpgradeUtils {

    static Path getBackupFile(Path file) {
        return file.resolveSibling(file.getFileName() + ".backup");
    }

    static boolean isBackupFile(Path file) {
        return FileUtils.hasFileExtension(file, "backup");
    }

}
