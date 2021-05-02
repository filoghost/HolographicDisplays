/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.placeholder;

import me.filoghost.fcommons.logging.Log;
import me.filoghost.holographicdisplays.core.hologram.StandardTextLine;
import me.filoghost.holographicdisplays.core.nms.entity.NMSArmorStand;
import me.filoghost.holographicdisplays.placeholder.parsing.PlaceholderOccurrence;
import me.filoghost.holographicdisplays.placeholder.parsing.StringWithPlaceholders;
import me.filoghost.holographicdisplays.placeholder.registry.PlaceholderExpansion;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.WeakHashMap;

public class PlaceholdersUpdateTask implements Runnable {

    private final PlaceholdersReplacementTracker placeholdersReplacementTracker;
    private final Map<StandardTextLine, TrackedLine> trackedLines;
    private final Map<PlaceholderExpansion, Long> lastErrorLogByPlaceholderExpansion;
    
    private long currentTick;

    public PlaceholdersUpdateTask(PlaceholdersReplacementTracker placeholdersReplacementTracker) {
        this.placeholdersReplacementTracker = placeholdersReplacementTracker;
        this.trackedLines = new LinkedHashMap<>();
        this.lastErrorLogByPlaceholderExpansion = new WeakHashMap<>();
    }

    @Override
    public void run() {
        Iterator<TrackedLine> iterator = trackedLines.values().iterator();
        while (iterator.hasNext()) {
            TrackedLine trackedLine = iterator.next();

            if (trackedLine.entity.isDeadNMS()) {
                iterator.remove();
                continue;
            }

            trackedLine.updateNameWithPlaceholders();
        }
        
        currentTick++;
    }

    public void updateTracking(StandardTextLine line) {
        TrackedLine trackedLine = createTrackedLineIfNeeded(line);

        if (trackedLine != null) {
            trackedLines.put(line, trackedLine);
            trackedLine.updateNameWithPlaceholders();
        } else {
            TrackedLine untrackedLine = trackedLines.remove(line);
            if (untrackedLine != null) {
                untrackedLine.restoreOriginalName();
            }
        }
    }
    
    private @Nullable TrackedLine createTrackedLineIfNeeded(StandardTextLine textLine) {
        if (!textLine.isAllowPlaceholders()) {
            return null;
        }

        String text = textLine.getText();
        if (text == null || text.isEmpty()) {
            return null;
        }

        NMSArmorStand entity = textLine.getNMSArmorStand();
        if (entity == null) {
            return null;
        }

        StringWithPlaceholders textWithPlaceholders = new StringWithPlaceholders(text);
        if (!textWithPlaceholders.containsPlaceholders()) {
            return null;
        }

        return new TrackedLine(textLine, entity, textWithPlaceholders);
    }

    private String getCurrentReplacement(PlaceholderOccurrence placeholderOccurrence) {        
        try {
            return placeholdersReplacementTracker.getOrUpdateReplacement(placeholderOccurrence, currentTick);
        } catch (PlaceholderException e) {
            handleException(e);
            return "[Error]";
        }
    }

    private void handleException(PlaceholderException exception) {
        PlaceholderExpansion placeholderExpansion = exception.getPlaceholderExpansion();
        
        Long lastErrorLog = lastErrorLogByPlaceholderExpansion.get(placeholderExpansion);
        if (lastErrorLog != null && currentTick - lastErrorLog < 20) {
            return; // Avoid spamming the console too frequently
        }
        lastErrorLogByPlaceholderExpansion.put(placeholderExpansion, currentTick);
        
        Log.warning("The placeholder \"" + placeholderExpansion.getIdentifier() + "\""
                        + " registered by the plugin " + placeholderExpansion.getPluginName()
                        + " generated an exception."
                        + " Please contact the author of " + placeholderExpansion.getPluginName(),
                exception.getCause());
    }
    
    
    private class TrackedLine {
        
        final StandardTextLine textLine;
        final NMSArmorStand entity;
        final StringWithPlaceholders nameWithPlaceholders;
        
        TrackedLine(StandardTextLine textLine, NMSArmorStand entity, StringWithPlaceholders nameWithPlaceholders) {
            this.textLine = textLine;
            this.entity = entity;
            this.nameWithPlaceholders = nameWithPlaceholders;
        }
    
        void updateNameWithPlaceholders() {
            String newName = nameWithPlaceholders.replacePlaceholders(PlaceholdersUpdateTask.this::getCurrentReplacement);
            entity.setCustomNameNMS(newName);
        }
    
        void restoreOriginalName() {
            if (!entity.isDeadNMS()) {
                entity.setCustomNameNMS(textLine.getText());
            }
        }

    }

}
