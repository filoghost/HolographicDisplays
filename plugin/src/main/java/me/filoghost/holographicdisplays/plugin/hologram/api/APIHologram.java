/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.hologram.api;

import me.filoghost.fcommons.Preconditions;
import me.filoghost.holographicdisplays.api.hologram.Hologram;
import me.filoghost.holographicdisplays.plugin.api.v2.V2HologramAdapter;
import me.filoghost.holographicdisplays.plugin.disk.Settings;
import me.filoghost.holographicdisplays.plugin.hologram.base.BaseHologram;
import me.filoghost.holographicdisplays.plugin.hologram.tracking.LineTrackerManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class APIHologram extends BaseHologram<APIHologramLine> implements Hologram {

    private final Plugin plugin;
    private final APIHologramManager apiHologramManager;
    private final DefaultVisibilitySettings visibilitySettings;
    private final long creationTimestamp;
    private final V2HologramAdapter v2Adapter;

    private boolean allowPlaceholders;

    protected APIHologram(
            Location location,
            Plugin plugin,
            APIHologramManager apiHologramManager,
            LineTrackerManager lineTrackerManager) {
        super(location, lineTrackerManager);
        Preconditions.notNull(plugin, "plugin");
        this.plugin = plugin;
        this.apiHologramManager = apiHologramManager;
        this.visibilitySettings = new DefaultVisibilitySettings();
        this.creationTimestamp = System.currentTimeMillis();
        this.v2Adapter = new V2HologramAdapter(this);
    }

    @Override
    public Plugin getCreatorPlugin() {
        return plugin;
    }

    @Override
    public @NotNull APITextLine appendTextLine(@Nullable String text) {
        APITextLine line = createTextLine(text);
        addLine(line);
        return line;
    }

    @Override
    public @NotNull APIItemLine appendItemLine(@NotNull ItemStack itemStack) {
        Preconditions.notNull(itemStack, "itemStack");

        APIItemLine line = createItemLine(itemStack);
        addLine(line);
        return line;
    }

    @Override
    public @NotNull APITextLine insertTextLine(int index, @Nullable String text) {
        APITextLine line = createTextLine(text);
        addLine(line);
        return line;
    }

    @Override
    public @NotNull APIItemLine insertItemLine(int index, @NotNull ItemStack itemStack) {
        Preconditions.notNull(itemStack, "itemStack");

        APIItemLine line = createItemLine(itemStack);
        addLine(line);
        return line;
    }

    private APITextLine createTextLine(String text) {
        return new APITextLine(this, text);
    }

    private APIItemLine createItemLine(ItemStack itemStack) {
        return new APIItemLine(this, itemStack);
    }

    @Override
    public @NotNull APIHologramLine getLine(int index) {
        return getLines().get(index);
    }

    @Override
    public void setAllowPlaceholders(boolean allowPlaceholders) {
        if (this.allowPlaceholders == allowPlaceholders) {
            return;
        }

        this.allowPlaceholders = allowPlaceholders;
        for (APIHologramLine line : getLines()) {
            line.setChanged();
        }
    }

    @Override
    public boolean isAllowPlaceholders() {
        return allowPlaceholders;
    }

    @Override
    public boolean isVisibleTo(Player player) {
        return visibilitySettings.isVisibleTo(player);
    }

    @Override
    public double getHeight() {
        List<APIHologramLine> lines = getLines();
        if (lines.isEmpty()) {
            return 0;
        }

        double height = 0.0;

        for (APIHologramLine line : lines) {
            height += line.getHeight();
        }

        height += Settings.spaceBetweenLines * (lines.size() - 1);
        return height;
    }

    @Override
    public long getCreationTimestamp() {
        return creationTimestamp;
    }

    @Override
    public @NotNull DefaultVisibilitySettings getVisibilitySettings() {
        return visibilitySettings;
    }

    @Override
    public void delete() {
        apiHologramManager.deleteHologram(this);
    }

    public V2HologramAdapter getV2Adapter() {
        return v2Adapter;
    }

}
