/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.config.upgrade;

import me.filoghost.fcommons.Preconditions;
import me.filoghost.fcommons.config.exception.ConfigException;
import me.filoghost.fcommons.logging.ErrorCollector;
import me.filoghost.holographicdisplays.plugin.config.ConfigManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public abstract class LegacyUpgrade implements LegacyUpgradeTask {

    protected final ConfigManager configManager;
    private final ErrorCollector errorCollector;
    private final Path rootDataFolder;
    private final Path backupsFolder;

    public LegacyUpgrade(ConfigManager configManager, ErrorCollector errorCollector) {
        this.configManager = configManager;
        this.errorCollector = errorCollector;
        this.rootDataFolder = configManager.getRootDataFolder();
        this.backupsFolder = rootDataFolder.resolve("old-files");
    }

    protected abstract Path getFile();

    public final void tryRun() {
        tryRun(getFile(), this);
    }

    protected final void tryRun(Path file, LegacyUpgradeTask task) {
        try {
            task.run();
        } catch (IOException | ConfigException e) {
            errorCollector.add(e, "error while upgrading \"" + configManager.formatPath(file) + "\" to the new format");
        }
    }

    protected final void createBackupFile(Path file) {
        Preconditions.checkArgument(file.startsWith(rootDataFolder), "file is outside data folder");
        Preconditions.checkArgument(!file.startsWith(backupsFolder), "file is inside backups folder");

        Path pathFromRootDataFolderToFile = file.subpath(rootDataFolder.getNameCount(), file.getNameCount());
        Path backupFile = backupsFolder.resolve(pathFromRootDataFolderToFile);

        // Find the first available destination file if already existing
        int copyIndex = 1;
        while (Files.isRegularFile(backupFile)) {
            backupFile = getAlternativeCopyFile(backupFile, copyIndex);
            copyIndex++;
        }

        try {
            Files.createDirectories(backupFile.getParent());
            Files.copy(file, backupFile);
        } catch (IOException e) {
            errorCollector.add(e, "error while copying file \"" + configManager.formatPath(file) + "\""
                    + " to \"" + configManager.formatPath(backupsFolder) + "\"");
        }
    }

    private Path getAlternativeCopyFile(Path file, int copyIndex) {
        String fileName = file.getFileName().toString();
        int extensionBeginIndex = fileName.lastIndexOf('.');
        String fileNameWithoutExtension;
        String extensionWithSeparator;

        if (extensionBeginIndex >= 0) {
            fileNameWithoutExtension = fileName.substring(0, extensionBeginIndex);
            extensionWithSeparator = fileName.substring(extensionBeginIndex);
        } else {
            fileNameWithoutExtension = fileName;
            extensionWithSeparator = "";
        }

        // Insert the copy index before the extension
        return file.resolveSibling(fileNameWithoutExtension + " (" + copyIndex + ")" + extensionWithSeparator);
    }

}
