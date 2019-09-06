/*
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package com.gmail.filoghost.holographicdisplays.placeholder;

import com.gmail.filoghost.holographicdisplays.nms.interfaces.entity.NMSNameable;
import com.gmail.filoghost.holographicdisplays.object.line.CraftTextLine;
import com.gmail.filoghost.holographicdisplays.util.ConsoleLogger;
import com.gmail.filoghost.holographicdisplays.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlaceholdersManager {

    private static long elapsedTenthsOfSecond;
    protected static Set<DynamicLineData> linesToUpdate = new HashSet<>();

    private static final Pattern ANIMATION_PATTERN = Pattern.compile("\\{animation:(.+?)}");

    private static String extractArgumentFromPlaceholder(Matcher matcher) {
        return matcher.group(1).trim();
    }


    public static void load(Plugin plugin) {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {

            for (Placeholder placeholder : PlaceholdersRegister.getPlaceholders()) {
                if (elapsedTenthsOfSecond % placeholder.getTenthsToRefresh() == 0) {
                    try {
                        placeholder.update();
                    } catch (Throwable t) {
                        ConsoleLogger.log(Level.WARNING, "The placeholder " + placeholder.getTextPlaceholder() +
                                " registered by the plugin " + placeholder.getOwner().getName() +
                                " generated an exception while updating. Please contact the author of " +
                                placeholder.getOwner().getName(), t);
                    }
                }
            }

            for (Placeholder placeholder : AnimationsRegister.getAnimations().values()) {
                if (elapsedTenthsOfSecond % placeholder.getTenthsToRefresh() == 0) {
                    placeholder.update();
                }
            }

            Iterator<DynamicLineData> iter = linesToUpdate.iterator();
            DynamicLineData currentLineData;

            while (iter.hasNext()) {
                currentLineData = iter.next();

                if (currentLineData.getEntity().isDeadNMS()) {
                    iter.remove();
                } else {
                    updatePlaceholders(currentLineData);
                }
            }

            elapsedTenthsOfSecond++;

        }, 2L, 2L);
    }


    public static void untrackAll() {
        linesToUpdate.clear();
    }

    public static void untrack(CraftTextLine line) {
        if (line == null || !line.isSpawned()) {
            return;
        }

        Iterator<DynamicLineData> iter = linesToUpdate.iterator();
        while (iter.hasNext()) {
            DynamicLineData data = iter.next();
            if (data.getEntity() == line.getNmsNameable()) {
                iter.remove();
                data.getEntity().setCustomNameNMS(data.getOriginalName());
            }
        }
    }

    public static void trackIfNecessary(CraftTextLine line) {
        NMSNameable nameableEntity = line.getNmsNameable();
        if (nameableEntity == null) {
            return;
        }

        String name = line.getText();
        if (name == null || name.isEmpty()) {
            return;
        }

        boolean updateName = false;

        // Lazy initialization.
        Set<Placeholder> normalPlaceholders = null;
        Set<PatternPlaceholder> patternPlaceholders = null;
        Map<String, Placeholder> animationsPlaceholders = null;

        Matcher matcher;

        for (Placeholder placeholder : PlaceholdersRegister.getPlaceholders()) {

            if (name.contains(placeholder.getTextPlaceholder())) {

                if (normalPlaceholders == null) {
                    normalPlaceholders = new HashSet<>();
                }

                normalPlaceholders.add(placeholder);
            }
        }

        for (PatternPlaceholder placeholder : PlaceholdersRegister.getPatternPlaceholders()) {

            if (placeholder.getPatternPlaceholder().matcher(name).find()) {

                if (patternPlaceholders == null) {
                    patternPlaceholders = new HashSet<>();
                }

                patternPlaceholders.add(placeholder);
            }
        }

        // Animation pattern.
        matcher = ANIMATION_PATTERN.matcher(name);
        while (matcher.find()) {

            String fileName = extractArgumentFromPlaceholder(matcher);
            Placeholder animation = AnimationsRegister.getAnimation(fileName);

            // If exists...
            if (animation != null) {

                if (animationsPlaceholders == null) {
                    animationsPlaceholders = new HashMap<>();
                }

                animationsPlaceholders.put(matcher.group(), animation);

            } else {
                name = name.replace(matcher.group(), "[Animation not found: " + fileName + "]");
                updateName = true;
            }
        }

        if (Utils.isThereNonNull(normalPlaceholders, patternPlaceholders, animationsPlaceholders)) {

            DynamicLineData lineData = new DynamicLineData(nameableEntity, name);

            if (normalPlaceholders != null) {
                lineData.setPlaceholders(normalPlaceholders);
            }

            if (patternPlaceholders != null) {
                lineData.setPatternPlaceholders(patternPlaceholders);
            }

            if (animationsPlaceholders != null) {
                lineData.getAnimations().putAll(animationsPlaceholders);
            }

            // It could be already tracked!
            if (!linesToUpdate.add(lineData)) {
                linesToUpdate.remove(lineData);
                linesToUpdate.add(lineData);
            }

            updatePlaceholders(lineData);

        } else {

            // The name needs to be updated anyways.
            if (updateName) {
                nameableEntity.setCustomNameNMS(name);
            }
        }
    }


    private static void updatePlaceholders(DynamicLineData lineData) {
        String oldCustomName = lineData.getEntity().getCustomNameNMS();
        String newCustomName = lineData.getOriginalName();

        if (!lineData.getPlaceholders().isEmpty()) {
            for (Placeholder placeholder : lineData.getPlaceholders()) {
                newCustomName = newCustomName.replace(placeholder.getTextPlaceholder(), Utils.sanitize(placeholder.getCurrentReplacement()));
            }
        }

        if (!lineData.getPatternPlaceholders().isEmpty()) {
            for (PatternPlaceholder placeholder : lineData.getPatternPlaceholders()) {
				final Matcher matcher = placeholder.getPatternPlaceholder().matcher(newCustomName);

				// Update the value
                if (elapsedTenthsOfSecond % placeholder.getTenthsToRefresh() == 0 && matcher.matches()) {
                    try {
						placeholder.update(matcher);
                    } catch (Throwable t) {
                        ConsoleLogger.log(Level.WARNING, "The placeholder " + placeholder.getPatternPlaceholder() +
                                " registered by the plugin " + placeholder.getOwner().getName() +
                                " generated an exception while updating. Please contact the author of " +
                                placeholder.getOwner().getName(), t);
                    }
                }

                newCustomName = matcher.replaceFirst(Utils.sanitize(placeholder.getCurrentReplacement()));
            }
        }

        if (!lineData.getAnimations().isEmpty()) {
            for (Entry<String, Placeholder> entry : lineData.getAnimations().entrySet()) {
                newCustomName = newCustomName.replace(entry.getKey(), Utils.sanitize(entry.getValue().getCurrentReplacement()));
            }
        }

        // Update only if needed, don't send useless packets.
        if (!oldCustomName.equals(newCustomName)) {
            lineData.getEntity().setCustomNameNMS(newCustomName);
        }
    }

}
