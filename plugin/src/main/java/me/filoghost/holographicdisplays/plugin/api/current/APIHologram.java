/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.api.current;

import me.filoghost.fcommons.Preconditions;
import me.filoghost.holographicdisplays.api.Position;
import me.filoghost.holographicdisplays.api.hologram.Hologram;
import me.filoghost.holographicdisplays.plugin.hologram.base.BaseHologram;
import me.filoghost.holographicdisplays.plugin.hologram.base.ImmutablePosition;
import me.filoghost.holographicdisplays.plugin.hologram.tracking.LineTrackerManager;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class APIHologram extends BaseHologram implements Hologram {

    private final APIHologramLines lines;
    private final Plugin plugin;
    private final APIHologramManager hologramManager;
    private final DefaultVisibilitySettings visibilitySettings;

    private boolean allowPlaceholders;

    protected APIHologram(
            ImmutablePosition position,
            Plugin plugin,
            APIHologramManager hologramManager,
            LineTrackerManager lineTrackerManager) {
        super(position, lineTrackerManager);
        Preconditions.notNull(plugin, "plugin");
        this.lines = new APIHologramLines(this);
        this.plugin = plugin;
        this.hologramManager = hologramManager;
        this.visibilitySettings = new DefaultVisibilitySettings();
    }

    @Override
    public @NotNull APIHologramLines getLines() {
        return lines;
    }

    @Override
    public @NotNull DefaultVisibilitySettings getVisibilitySettings() {
        return visibilitySettings;
    }

    @Override
    public void setPosition(@NotNull Position position) {
        super.setPosition(ImmutablePosition.of(position));
    }

    @Override
    public boolean isAllowPlaceholders() {
        return allowPlaceholders;
    }

    @Override
    public void setAllowPlaceholders(boolean allowPlaceholders) {
        checkNotDeleted();

        if (this.allowPlaceholders == allowPlaceholders) {
            return;
        }

        this.allowPlaceholders = allowPlaceholders;
        for (APIHologramLine line : lines) {
            line.setChanged();
        }
    }

    @Override
    public Plugin getCreatorPlugin() {
        return plugin;
    }

    @Override
    public boolean isVisibleTo(Player player) {
        return visibilitySettings.isVisibleTo(player);
    }

    @Override
    public void delete() {
        hologramManager.deleteHologram(this);
    }

}
