/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.api.current;

import me.filoghost.fcommons.Preconditions;
import me.filoghost.holographicdisplays.api.hologram.VisibilitySettings;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultVisibilitySettings implements VisibilitySettings {

    private Visibility globalVisibility;
    private Map<UUID, Visibility> individualVisibilities;

    public DefaultVisibilitySettings() {
        this.globalVisibility = Visibility.VISIBLE;
    }

    @Override
    public @NotNull Visibility getVisibility() {
        return globalVisibility;
    }

    @Override
    public void setVisibility(@NotNull Visibility visibility) {
        if (this.globalVisibility == visibility) {
            return;
        }

        this.globalVisibility = visibility;
    }

    @Override
    public void setIndividualVisibility(@NotNull Player player, @NotNull Visibility visibility) {
        // Lazy initialization
        if (individualVisibilities == null) {
            individualVisibilities = new ConcurrentHashMap<>();
        }
        individualVisibilities.put(player.getUniqueId(), visibility);
    }

    @Override
    public boolean isVisibleTo(@NotNull Player player) {
        Preconditions.notNull(player, "player");

        return getVisibility(player) == Visibility.VISIBLE;
    }

    private Visibility getVisibility(Player player) {
        if (individualVisibilities != null) {
            Visibility visibility = individualVisibilities.get(player.getUniqueId());
            if (visibility != null) {
                return visibility;
            }
        }

        return globalVisibility;
    }

    @Override
    public void removeIndividualVisibility(@NotNull Player player) {
        Preconditions.notNull(player, "player");

        if (individualVisibilities == null) {
            return;
        }

        individualVisibilities.remove(player.getUniqueId());
    }

    @Override
    public void clearIndividualVisibilities() {
        if (individualVisibilities == null) {
            return;
        }

        individualVisibilities.clear();
    }

    @Override
    public String toString() {
        return "VisibilitySettings{"
                + "globalVisibility=" + globalVisibility
                + ", individualVisibilities=" + individualVisibilities
                + "}";
    }

}
