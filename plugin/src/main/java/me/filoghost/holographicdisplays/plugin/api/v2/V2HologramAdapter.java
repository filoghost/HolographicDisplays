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
import me.filoghost.holographicdisplays.plugin.hologram.api.APIHologram;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings("deprecation")
public class V2HologramAdapter implements Hologram {

    private final APIHologram v3Hologram;

    public V2HologramAdapter(APIHologram v3Hologram) {
        this.v3Hologram = v3Hologram;
    }

    @Override
    public TextLine appendTextLine(String text) {
        return v3Hologram.appendTextLine(text).getV2Adapter();
    }

    @Override
    public ItemLine appendItemLine(ItemStack itemStack) {
        return v3Hologram.appendItemLine(itemStack).getV2Adapter();
    }

    @Override
    public TextLine insertTextLine(int index, String text) {
        return v3Hologram.insertTextLine(index, text).getV2Adapter();
    }

    @Override
    public ItemLine insertItemLine(int index, ItemStack itemStack) {
        return v3Hologram.insertItemLine(index, itemStack).getV2Adapter();
    }

    @Override
    public HologramLine getLine(int index) {
        return v3Hologram.getLine(index).getV2Adapter();
    }

    @Override
    public void removeLine(int index) {
        v3Hologram.removeLine(index);
    }

    @Override
    public void clearLines() {
        v3Hologram.clearLines();
    }

    @Override
    public int size() {
        return v3Hologram.getLineCount();
    }

    @Override
    public double getHeight() {
        return v3Hologram.getHeight();
    }

    @Override
    public void teleport(Location location) {
        v3Hologram.setPosition(location);
    }

    @Override
    public void teleport(World world, double x, double y, double z) {
        v3Hologram.setPosition(world, x, y, z);
    }

    @Override
    public Location getLocation() {
        return v3Hologram.getPosition().toLocation();
    }

    @Override
    public double getX() {
        return v3Hologram.getPosition().getX();
    }

    @Override
    public double getY() {
        return v3Hologram.getPosition().getY();
    }

    @Override
    public double getZ() {
        return v3Hologram.getPosition().getZ();
    }

    @Override
    public World getWorld() {
        return v3Hologram.getWorldIfLoaded();
    }

    @Override
    public VisibilityManager getVisibilityManager() {
        return v3Hologram.getVisibilitySettings().getV2Adapter();
    }

    @Override
    public long getCreationTimestamp() {
        return v3Hologram.getCreationTimestamp();
    }

    @Override
    public boolean isAllowPlaceholders() {
        return v3Hologram.isAllowPlaceholders();
    }

    @Override
    public void setAllowPlaceholders(boolean allowPlaceholders) {
        v3Hologram.setAllowPlaceholders(allowPlaceholders);
    }

    @Override
    public void delete() {
        v3Hologram.delete();
    }

    @Override
    public boolean isDeleted() {
        return v3Hologram.isDeleted();
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
        return this.v3Hologram.equals(other.v3Hologram);
    }

    @Override
    public final int hashCode() {
        return v3Hologram.hashCode();
    }

    @Override
    public final String toString() {
        return v3Hologram.toString();
    }

}
