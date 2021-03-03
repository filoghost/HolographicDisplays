/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.object.api;

import me.filoghost.fcommons.Preconditions;
import me.filoghost.holographicdisplays.api.Hologram;
import me.filoghost.holographicdisplays.api.VisibilityManager;
import me.filoghost.holographicdisplays.api.line.HologramLine;
import me.filoghost.holographicdisplays.api.line.ItemLine;
import me.filoghost.holographicdisplays.api.line.TextLine;
import me.filoghost.holographicdisplays.core.nms.NMSManager;
import me.filoghost.holographicdisplays.object.base.BaseHologram;
import me.filoghost.holographicdisplays.disk.Configuration;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class APIHologram extends BaseHologram implements Hologram {
    
    private final Plugin plugin;
    private final APIHologramManager apiHologramManager;
    private final VisibilityManager visibilityManager;
    private final long creationTimestamp;
    private final List<APIHologramLine> lines;

    private boolean allowPlaceholders;

    protected APIHologram(Location source, Plugin plugin, NMSManager nmsManager, APIHologramManager apiHologramManager) {
        super(source, nmsManager);
        Preconditions.notNull(plugin, "plugin");
        this.plugin = plugin;
        this.apiHologramManager = apiHologramManager;
        this.visibilityManager = new DefaultVisibilityManager(this);
        this.creationTimestamp = System.currentTimeMillis();
        this.lines = new ArrayList<>();

    }
    
    @Override
    public Plugin getOwnerPlugin() {
        return plugin;
    }

    @Override
    public List<APIHologramLine> getLinesUnsafe() {
        return lines;
    }

    @Override
    public TextLine appendTextLine(String text) {
        checkState();

        APITextLine line = createTextLine(text);
        lines.add(line);
        refresh();
        return line;
    }

    @Override
    public ItemLine appendItemLine(ItemStack itemStack) {
        checkState();
        Preconditions.notNull(itemStack, "itemStack");

        APIItemLine line = createItemLine(itemStack);
        lines.add(line);
        refresh();
        return line;
    }

    @Override
    public TextLine insertTextLine(int index, String text) {
        checkState();

        APITextLine line = createTextLine(text);
        lines.add(index, line);
        refresh();
        return line;
    }

    @Override
    public ItemLine insertItemLine(int index, ItemStack itemStack) {
        checkState();
        Preconditions.notNull(itemStack, "itemStack");

        APIItemLine line = createItemLine(itemStack);
        lines.add(index, line);
        refresh();
        return line;
    }

    private APITextLine createTextLine(String text) {
        return new APITextLine(this, getNMSManager(), text);
    }

    private APIItemLine createItemLine(ItemStack itemStack) {
        return new APIItemLine(this, getNMSManager(), itemStack);
    }

    @Override
    public HologramLine getLine(int index) {
        return lines.get(index);
    }

    @Override
    public void setAllowPlaceholders(boolean allowPlaceholders) {
        if (this.allowPlaceholders == allowPlaceholders) {
            return;
        }

        this.allowPlaceholders = allowPlaceholders;
        refresh(true);
    }

    @Override
    public boolean isAllowPlaceholders() {
        return allowPlaceholders;
    }

    @Override
    public boolean isVisibleTo(Player player) {
        return visibilityManager.isVisibleTo(player);
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

        height += Configuration.spaceBetweenLines * (lines.size() - 1);
        return height;
    }

    @Override
    public long getCreationTimestamp() {
        return creationTimestamp;
    }

    @Override
    public VisibilityManager getVisibilityManager() {
        return visibilityManager;
    }

    @Override
    public void delete() {
        apiHologramManager.deleteHologram(this);
    }

    @Override
    public String toFormattedString() {
        return plugin.getName() + "@" + Integer.toHexString(hashCode());
    }

}
