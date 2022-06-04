/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.core.api.current;

import me.filoghost.fcommons.Preconditions;
import me.filoghost.holographicdisplays.api.hologram.VisibilitySettings;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class DefaultVisibilitySettings implements VisibilitySettings {

    private final AtomicInteger version;
    private Visibility globalVisibility;
    private Map<UUID, Visibility> individualVisibilities;

    public DefaultVisibilitySettings() {
        this.version = new AtomicInteger();
        this.globalVisibility = Visibility.VISIBLE;
    }

    @Override
    public @NotNull Visibility getGlobalVisibility() {
        return globalVisibility;
    }

    @Override
    public void setGlobalVisibility(@NotNull Visibility visibility) {
        if (this.globalVisibility == visibility) {
            return;
        }

        this.globalVisibility = visibility;
        version.incrementAndGet();
    }

    @Override
    public void setIndividualVisibility(@NotNull Player player, @NotNull Visibility visibility) {
        // Lazy initialization
        if (individualVisibilities == null) {
            individualVisibilities = new ConcurrentHashMap<>();
        }
        Visibility previousVisibility = individualVisibilities.put(player.getUniqueId(), visibility);
        if (visibility != previousVisibility) {
            version.incrementAndGet();
        }
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

        Visibility previousVisibility = individualVisibilities.remove(player.getUniqueId());
        if (previousVisibility != null) {
            version.incrementAndGet();
        }
    }

    @Override
    public void clearIndividualVisibilities() {
        if (individualVisibilities == null || individualVisibilities.isEmpty()) {
            return;
        }

        individualVisibilities.clear();
        version.incrementAndGet();
    }

    public int getVersion() {
        return version.get();
    }

    @Override
    public String toString() {
        return "VisibilitySettings{"
                + "globalVisibility=" + globalVisibility
                + ", individualVisibilities=" + individualVisibilities
                + "}";
    }

}
