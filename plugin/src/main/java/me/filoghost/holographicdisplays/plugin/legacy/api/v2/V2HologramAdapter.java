/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.legacy.api.v2;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.VisibilityManager;
import com.gmail.filoghost.holographicdisplays.api.line.HologramLine;
import com.gmail.filoghost.holographicdisplays.api.line.ItemLine;
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;
import me.filoghost.holographicdisplays.plugin.object.api.APIHologram;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings("deprecation")
public class V2HologramAdapter implements Hologram {

    private final APIHologram newHologram;

    public V2HologramAdapter(APIHologram newHologram) {
        this.newHologram = newHologram;
    }

    @Override
    public TextLine appendTextLine(String text) {
        return newHologram.appendTextLine(text).getV2Adapter();
    }

    @Override
    public ItemLine appendItemLine(ItemStack itemStack) {
        return newHologram.appendItemLine(itemStack).getV2Adapter();
    }

    @Override
    public TextLine insertTextLine(int index, String text) {
        return newHologram.insertTextLine(index, text).getV2Adapter();
    }

    @Override
    public ItemLine insertItemLine(int index, ItemStack itemStack) {
        return newHologram.insertItemLine(index, itemStack).getV2Adapter();
    }

    @Override
    public HologramLine getLine(int index) {
        return newHologram.getLine(index).getV2Adapter();
    }

    @Override
    public void removeLine(int index) {
        newHologram.removeLine(index);
    }

    @Override
    public void clearLines() {
        newHologram.clearLines();
    }

    @Override
    public int size() {
        return newHologram.getLineCount();
    }

    @Override
    public double getHeight() {
        return newHologram.getHeight();
    }

    @Override
    public void teleport(Location location) {
        newHologram.teleport(location);
    }

    @Override
    public void teleport(World world, double x, double y, double z) {
        newHologram.teleport(world, x, y, z);
    }

    @Override
    public Location getLocation() {
        return newHologram.getLocation();
    }

    @Override
    public double getX() {
        return newHologram.getX();
    }

    @Override
    public double getY() {
        return newHologram.getY();
    }

    @Override
    public double getZ() {
        return newHologram.getZ();
    }

    @Override
    public World getWorld() {
        return newHologram.getWorld();
    }

    @Override
    public VisibilityManager getVisibilityManager() {
        return newHologram.getVisibilitySettings().getV2Adapter();
    }

    @Override
    public long getCreationTimestamp() {
        return newHologram.getCreationTimestamp();
    }

    @Override
    public boolean isAllowPlaceholders() {
        return newHologram.isAllowPlaceholders();
    }

    @Override
    public void setAllowPlaceholders(boolean allowPlaceholders) {
        newHologram.setAllowPlaceholders(allowPlaceholders);
    }

    @Override
    public void delete() {
        newHologram.delete();
    }

    @Override
    public boolean isDeleted() {
        return newHologram.isDeleted();
    }

    @Override
    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof V2HologramAdapter)) {
            return false;
        }

        V2HologramAdapter other = (V2HologramAdapter) obj;
        return this.newHologram.equals(other.newHologram);
    }

    @Override
    public final int hashCode() {
        return newHologram.hashCode();
    }

    @Override
    public final String toString() {
        return newHologram.toString();
    }

}
