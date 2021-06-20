/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.placeholder.tracking;

import me.filoghost.holographicdisplays.common.hologram.StandardTextLine;
import me.filoghost.holographicdisplays.common.nms.entity.NMSArmorStand;
import me.filoghost.holographicdisplays.plugin.placeholder.parsing.StringWithPlaceholders;
import me.filoghost.holographicdisplays.plugin.placeholder.parsing.PlaceholderReplaceFunction;
import org.bukkit.entity.Player;

public class TrackedLine {

    private final PlaceholderTracker placeholderTracker;
    private final StandardTextLine textLine;
    private final NMSArmorStand entity;
    private final StringWithPlaceholders textWithPlaceholders;
    private final boolean containsIndividualPlaceholders;
    private final PlaceholderReplaceFunction globalReplaceFunction;

    private StringWithPlaceholders textWithGlobalReplacements;

    TrackedLine(
            PlaceholderTracker placeholderTracker,
            StandardTextLine textLine,
            NMSArmorStand entity,
            StringWithPlaceholders textWithPlaceholders) {
        this.placeholderTracker = placeholderTracker;
        this.textLine = textLine;
        this.entity = entity;
        this.textWithPlaceholders = textWithPlaceholders;
        this.containsIndividualPlaceholders = placeholderTracker.containsIndividualPlaceholders(textWithPlaceholders);
        this.globalReplaceFunction = placeholderTracker::updateAndGetGlobalReplacement;
    }

    void updateEntityWithGlobalPlaceholders() {
        textWithGlobalReplacements = textWithPlaceholders.partiallyReplacePlaceholders(globalReplaceFunction);
        entity.setCustomNameNMS(textWithGlobalReplacements.toString());
    }

    public String replaceIndividualPlaceholders(Player player) {
        if (containsIndividualPlaceholders) {
            return textWithGlobalReplacements.replacePlaceholders((occurrence) ->
                    placeholderTracker.updateAndGetIndividualReplacement(occurrence, player));
        } else {
            return textWithGlobalReplacements.toString();
        }
    }

    void restoreOriginalName() {
        if (!entity.isDeadNMS()) {
            entity.setCustomNameNMS(textLine.getText());
        }
    }

    public boolean shouldBeUntracked() {
        return entity.isDeadNMS();
    }

}
