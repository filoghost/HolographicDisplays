/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.api.v2;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.VisibilityManager;
import com.gmail.filoghost.holographicdisplays.api.line.HologramLine;
import com.gmail.filoghost.holographicdisplays.api.line.ItemLine;
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;
import me.filoghost.holographicdisplays.plugin.hologram.base.BaseHologram;
import me.filoghost.holographicdisplays.plugin.hologram.base.BaseHologramLines;
import me.filoghost.holographicdisplays.plugin.hologram.base.BaseHologramPosition;
import me.filoghost.holographicdisplays.plugin.hologram.tracking.LineTrackerManager;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

@SuppressWarnings("deprecation")
public class V2Hologram extends BaseHologram implements Hologram {

    private final Plugin plugin;
    private final V2HologramManager hologramManager;
    private final BaseHologramLines<V2HologramLine> lines;
    private final V2VisibilityManager visibilityManager;
    private final long creationTimestamp;
    private boolean allowPlaceholders;

    public V2Hologram(
            BaseHologramPosition position,
            Plugin plugin,
            LineTrackerManager lineTrackerManager,
            V2HologramManager hologramManager) {
        super(position, lineTrackerManager);
        this.plugin = plugin;
        this.hologramManager = hologramManager;
        this.lines = new BaseHologramLines<>(this);
        this.visibilityManager = new V2VisibilityManager();
        this.creationTimestamp = System.currentTimeMillis();
    }

    @Override
    public BaseHologramLines<V2HologramLine> getLines() {
        return lines;
    }

    @Override
    protected boolean isVisibleTo(Player player) {
        return visibilityManager.isVisibleTo(player);
    }

    @Override
    public Plugin getCreatorPlugin() {
        return plugin;
    }

    @Override
    public TextLine appendTextLine(String text) {
        V2TextLine textLine = new V2TextLine(this, text);
        lines.add(textLine);
        return textLine;
    }

    @Override
    public ItemLine appendItemLine(ItemStack itemStack) {
        V2ItemLine itemLine = new V2ItemLine(this, itemStack);
        lines.add(itemLine);
        return itemLine;
    }

    @Override
    public TextLine insertTextLine(int index, String text) {
        V2TextLine textLine = new V2TextLine(this, text);
        lines.insert(index, textLine);
        return textLine;
    }

    @Override
    public ItemLine insertItemLine(int index, ItemStack itemStack) {
        V2ItemLine itemLine = new V2ItemLine(this, itemStack);
        lines.insert(index, itemLine);
        return itemLine;
    }

    @Override
    public HologramLine getLine(int index) {
        return lines.get(index);
    }

    @Override
    public void removeLine(int index) {
        lines.remove(index);
    }

    @Override
    public void clearLines() {
        lines.clear();
    }

    @Override
    public int size() {
        return lines.size();
    }

    @Override
    public double getHeight() {
        return lines.getHeight();
    }

    @Override
    public void teleport(Location location) {
        super.setPosition(location);
    }

    @Override
    public void teleport(World world, double x, double y, double z) {
        super.setPosition(world, x, y, z);
    }

    @Override
    public Location getLocation() {
        return super.getPosition().toLocation();
    }

    @Override
    public double getX() {
        return super.getPosition().getX();
    }

    @Override
    public double getY() {
        return super.getPosition().getY();
    }

    @Override
    public double getZ() {
        return super.getPosition().getZ();
    }

    @Override
    public World getWorld() {
        return super.getWorldIfLoaded();
    }

    @Override
    public VisibilityManager getVisibilityManager() {
        return visibilityManager;
    }

    @Override
    public long getCreationTimestamp() {
        return creationTimestamp;
    }

    @Override
    public boolean isAllowPlaceholders() {
        return allowPlaceholders;
    }

    @Override
    public void setAllowPlaceholders(boolean allowPlaceholders) {
        this.allowPlaceholders = allowPlaceholders;
    }

    @Override
    public void delete() {
        hologramManager.deleteHologram(this);
    }

}
