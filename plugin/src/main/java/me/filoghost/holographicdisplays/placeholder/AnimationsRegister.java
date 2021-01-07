/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.placeholder;

import me.filoghost.fcommons.logging.Log;
import me.filoghost.holographicdisplays.HolographicDisplays;
import me.filoghost.holographicdisplays.common.DebugLogger;
import me.filoghost.holographicdisplays.disk.StringConverter;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class AnimationsRegister {
    
    // <fileName, lines>
    private final static Map<String, Placeholder> animations = new HashMap<>();
    
    public static void loadAnimations(Plugin plugin) throws IOException {
        animations.clear();
        
        Path animationFolder = HolographicDisplays.getDataFolderPath().resolve("animations");
        if (!Files.isDirectory(animationFolder)) {
            Files.createDirectories(animationFolder);
            plugin.saveResource("animations/example.txt", false);
            return;
        }
        
        try (Stream<Path> animationFiles = Files.list(animationFolder)) {
            animationFiles.forEach(AnimationsRegister::readAnimationFile);
        }
    }

    private static void readAnimationFile(Path file) {
        try {
            List<String> lines = Files.readAllLines(file);
            if (lines.size() == 0) {
                return;
            }

            double speed = 0.5;
            boolean validSpeedFound = false;

            String firstLine = lines.get(0).trim();
            if (firstLine.toLowerCase().startsWith("speed:")) {

                // Do not consider it.
                lines.remove(0);

                firstLine = firstLine.substring("speed:".length()).trim();

                try {
                    speed = Double.parseDouble(firstLine);
                    validSpeedFound = true;
                } catch (NumberFormatException ignored) {}
            }

            if (!validSpeedFound) {
                Log.warning("Could not find a valid 'speed: <number>' in the first line of the file '" + file.getFileName() + "'. Default speed of 0.5 seconds will be used.");
            }

            if (lines.isEmpty()) {
                lines.add("[No lines: " + file.getFileName() + "]");
                Log.warning("Could not find any line in '" + file.getFileName() + "' (excluding the speed). You should add at least one more line.");
            }

            // Replace placeholders.
            for (int i = 0; i < lines.size(); i++) {
                lines.set(i, StringConverter.toReadableFormat(lines.get(i)));
            }

            animations.put(file.getFileName().toString(), new Placeholder(HolographicDisplays.getInstance(), file.getFileName().toString(), speed, new CyclicPlaceholderReplacer(lines.toArray(new String[0]))));
            DebugLogger.info("Successfully loaded animation '" + file.getFileName() + "', speed = " + speed + ".");

        } catch (Exception e) {
            Log.severe("Couldn't load the file '" + file.getFileName() + "'!", e);
        }
    }


    public static Map<String, Placeholder> getAnimations() {
        return animations;
    }

    public static Placeholder getAnimation(String name) {
        return animations.get(name);
    }
    
}
