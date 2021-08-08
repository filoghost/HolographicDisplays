/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.hologram.api;

import me.filoghost.fcommons.Preconditions;
import me.filoghost.holographicdisplays.api.hologram.Hologram;
import me.filoghost.holographicdisplays.api.hologram.HologramPosition;
import me.filoghost.holographicdisplays.plugin.api.v2.V2HologramAdapter;
import me.filoghost.holographicdisplays.plugin.config.Settings;
import me.filoghost.holographicdisplays.plugin.hologram.base.BaseHologram;
import me.filoghost.holographicdisplays.plugin.hologram.base.BaseHologramLines;
import me.filoghost.holographicdisplays.plugin.hologram.base.BaseHologramPosition;
import me.filoghost.holographicdisplays.plugin.hologram.tracking.LineTrackerManager;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class APIHologram extends BaseHologram implements Hologram {

    private final BaseHologramLines<APIHologramLine> lines;
    private final Plugin plugin;
    private final APIHologramManager apiHologramManager;
    private final DefaultVisibilitySettings visibilitySettings;
    private final long creationTimestamp;
    private final V2HologramAdapter v2Adapter;

    private boolean allowPlaceholders;

    protected APIHologram(
            BaseHologramPosition position,
            Plugin plugin,
            APIHologramManager apiHologramManager,
            LineTrackerManager lineTrackerManager) {
        super(position, lineTrackerManager);
        Preconditions.notNull(plugin, "plugin");
        this.lines = new BaseHologramLines<>(this);
        this.plugin = plugin;
        this.apiHologramManager = apiHologramManager;
        this.visibilitySettings = new DefaultVisibilitySettings();
        this.creationTimestamp = System.currentTimeMillis();
        this.v2Adapter = new V2HologramAdapter(this);
    }

    @Override
    protected BaseHologramLines<APIHologramLine> getLines() {
        return lines;
    }

    @Override
    public Plugin getCreatorPlugin() {
        return plugin;
    }

    @Override
    public @NotNull APIHologramLine getLine(int index) {
        return lines.get(index);
    }

    @Override
    public @NotNull APITextLine appendTextLine(@Nullable String text) {
        checkNotDeleted();

        APITextLine line = new APITextLine(this, text);
        lines.add(line);
        return line;
    }

    @Override
    public @NotNull APIItemLine appendItemLine(@NotNull ItemStack itemStack) {
        Preconditions.notNull(itemStack, "itemStack");
        checkNotDeleted();

        APIItemLine line = new APIItemLine(this, itemStack);
        lines.add(line);
        return line;
    }

    @Override
    public @NotNull APITextLine insertTextLine(int index, @Nullable String text) {
        checkNotDeleted();

        APITextLine line = new APITextLine(this, text);
        lines.add(line);
        return line;
    }

    @Override
    public @NotNull APIItemLine insertItemLine(int index, @NotNull ItemStack itemStack) {
        Preconditions.notNull(itemStack, "itemStack");
        checkNotDeleted();

        APIItemLine line = new APIItemLine(this, itemStack);
        lines.add(line);
        return line;
    }

    @Override
    public void removeLine(int index) {
        checkNotDeleted();

        lines.remove(index);
    }

    public void removeLine(APIHologramLine line) {
        checkNotDeleted();

        lines.remove(line);
    }

    @Override
    public void clearLines() {
        checkNotDeleted();

        lines.clear();
    }

    @Override
    public int getLineCount() {
        return getLines().size();
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
    public boolean isAllowPlaceholders() {
        return allowPlaceholders;
    }

    @Override
    public boolean isVisibleTo(Player player) {
        return visibilitySettings.isVisibleTo(player);
    }

    @Override
    public double getHeight() {
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
    public @NotNull HologramPosition getPosition() {
        return new APIHologramPosition(getPositionWorldName(), getPositionX(), getPositionY(), getPositionZ());
    }

    @Override
    public void setPosition(@NotNull HologramPosition position) {
        super.setPosition(position.getWorldName(), position.getX(), position.getY(), position.getZ());
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
