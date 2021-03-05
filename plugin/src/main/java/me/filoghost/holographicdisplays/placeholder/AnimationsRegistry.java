/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.placeholder;

import me.filoghost.fcommons.config.exception.ConfigSaveException;
import me.filoghost.fcommons.logging.Log;
import me.filoghost.holographicdisplays.HolographicDisplays;
import me.filoghost.holographicdisplays.core.DebugLogger;
import me.filoghost.holographicdisplays.disk.ConfigManager;
import me.filoghost.holographicdisplays.disk.StringConverter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class AnimationsRegistry {

    private static final String SPEED_PREFIX = "speed:";
    
    private static final Map<String, Placeholder> animationsByFilename = new HashMap<>();

    public static void loadAnimations(ConfigManager configManager) throws IOException, ConfigSaveException {
        animationsByFilename.clear();
        Path animationFolder = configManager.getAnimationsFolder();

        if (!Files.isDirectory(animationFolder)) {
            Files.createDirectories(animationFolder);
            configManager.getExampleAnimationLoader().createDefault();
            return;
        }
        
        try (Stream<Path> animationFiles = Files.list(animationFolder)) {
            animationFiles.forEach(AnimationsRegistry::readAnimationFile);
        }
    }

    private static void readAnimationFile(Path file) {
        String fileName = file.getFileName().toString();
        
        try {
            List<String> lines = Files.readAllLines(file);
            if (lines.size() == 0) {
                return;
            }

            double speed = 0.5;
            boolean validSpeedFound = false;

            String firstLine = lines.get(0).trim();
            if (firstLine.toLowerCase().startsWith(SPEED_PREFIX)) {
                // Do not consider it.
                lines.remove(0);

                firstLine = firstLine.substring(SPEED_PREFIX.length()).trim();

                try {
                    speed = Double.parseDouble(firstLine);
                    validSpeedFound = true;
                } catch (NumberFormatException ignored) {}
            }

            if (!validSpeedFound) {
                Log.warning("Could not find a valid '" + SPEED_PREFIX + " <number>' in the first line of the file '" + fileName + "'. Default speed of 0.5 seconds will be used.");
            }

            if (lines.isEmpty()) {
                lines.add("[No lines: " + fileName + "]");
                Log.warning("Could not find any line in '" + fileName + "' (excluding the speed). You should add at least one more line.");
            }

            // Replace placeholders.
            for (int i = 0; i < lines.size(); i++) {
                lines.set(i, StringConverter.toReadableFormat(lines.get(i)));
            }

            animationsByFilename.put(fileName, new Placeholder(HolographicDisplays.getInstance(), fileName, speed, new CyclicPlaceholderReplacer(lines)));
            DebugLogger.info("Successfully loaded animation '" + fileName + "', speed = " + speed + ".");

        } catch (Exception e) {
            Log.severe("Couldn't load the file '" + fileName + "'!", e);
        }
    }


    public static Map<String, Placeholder> getAnimationsByFilename() {
        return animationsByFilename;
    }

    public static Placeholder getAnimation(String name) {
        return animationsByFilename.get(name);
    }
    
}
