/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.placeholder;

import me.filoghost.holographicdisplays.core.hologram.StandardTextLine;
import me.filoghost.holographicdisplays.core.nms.entity.NMSArmorStand;
import me.filoghost.holographicdisplays.placeholder.parsing.StringWithPlaceholders;
import me.filoghost.holographicdisplays.placeholder.registry.PlaceholderRegistry;

public class TrackedLine {

    private final PlaceholdersUpdateTask placeholdersUpdateTask;
    private final PlaceholderRegistry placeholderRegistry;
    private final StandardTextLine textLine;
    private final NMSArmorStand entity;
    private final StringWithPlaceholders nameWithPlaceholders;
    private final boolean containsRelativePlaceholders;

    TrackedLine(PlaceholdersUpdateTask placeholdersUpdateTask, PlaceholderRegistry placeholderRegistry, StandardTextLine textLine, NMSArmorStand entity, StringWithPlaceholders nameWithPlaceholders) {
        this.placeholdersUpdateTask = placeholdersUpdateTask;
        this.placeholderRegistry = placeholderRegistry;
        this.textLine = textLine;
        this.entity = entity;
        this.nameWithPlaceholders = nameWithPlaceholders;
        this.containsRelativePlaceholders = nameWithPlaceholders.containsPlaceholdersMatching(
                occurrence -> this.placeholderRegistry.findRelative(occurrence) != null
        );
    }

    void updateNameWithPlaceholders() {
        String newName = nameWithPlaceholders.replacePlaceholders(placeholdersUpdateTask::getCurrentReplacement);
        entity.setCustomNameNMS(newName);
    }

    void restoreOriginalName() {
        if (!entity.isDeadNMS()) {
            entity.setCustomNameNMS(textLine.getText());
        }
    }

    public boolean containsRelativePlaceholders() {
        return containsRelativePlaceholders;
    }

    public boolean shouldBeUntracked() {
        return entity.isDeadNMS();
    }

}
