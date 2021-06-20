/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.placeholder.tracking;

import me.filoghost.holographicdisplays.common.hologram.StandardTextLine;
import me.filoghost.holographicdisplays.common.nms.entity.NMSArmorStand;
import me.filoghost.holographicdisplays.plugin.placeholder.parsing.StringWithPlaceholders;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class PlaceholderLineTracker {

    private final PlaceholderTracker replacementTracker;
    private final Map<StandardTextLine, TrackedLine> trackedLines;

    public PlaceholderLineTracker(PlaceholderTracker replacementTracker) {
        this.replacementTracker = replacementTracker;
        this.trackedLines = new LinkedHashMap<>();
    }

    public void updateEntitiesWithGlobalPlaceholders() {
        Iterator<TrackedLine> iterator = trackedLines.values().iterator();
        while (iterator.hasNext()) {
            TrackedLine trackedLine = iterator.next();

            if (trackedLine.shouldBeUntracked()) {
                iterator.remove();
                continue;
            }

            trackedLine.updateEntityWithGlobalPlaceholders();
        }
    }

    public void onTextLineChange(StandardTextLine line) {
        TrackedLine trackedLine = createTrackedLineIfNeeded(line);

        if (trackedLine != null) {
            trackedLines.put(line, trackedLine);
            trackedLine.updateEntityWithGlobalPlaceholders(); // Update placeholders instantly to avoid flashing the non-replaced text
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

        return new TrackedLine(replacementTracker, textLine, entity, textWithPlaceholders);
    }

    public TrackedLine getTrackedLine(StandardTextLine line) {
        return trackedLines.get(line);
    }

}
