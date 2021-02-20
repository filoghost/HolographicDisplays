/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package com.gmail.filoghost.holograms.api.adapter;

import com.gmail.filoghost.holograms.api.FloatingItem;
import com.gmail.filoghost.holograms.api.ItemTouchHandler;
import com.gmail.filoghost.holograms.api.PickupHandler;
import me.filoghost.holographicdisplays.api.Hologram;
import me.filoghost.holographicdisplays.api.line.ItemLine;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("deprecation")
public class FloatingItemAdapter implements FloatingItem {
    
    public static final Map<Plugin, Collection<FloatingItemAdapter>> activeFloatingItems = new HashMap<>();
    
    private final Plugin plugin;
    private final Hologram hologram;
    private final ItemLine itemLine;
    private ItemTouchHandler touchHandler;
    private PickupHandler pickupHandler;
    
    public FloatingItemAdapter(Plugin plugin, Hologram delegateHologram, ItemLine delegateItemLine) {
        this.plugin = plugin;
        this.hologram = delegateHologram;        
        this.itemLine = delegateItemLine;
        
        activeFloatingItems.computeIfAbsent(plugin, __ -> new ArrayList<>()).add(this);
    }

    @Override
    public boolean update() {
        return true;
    }

    @Override
    public void hide() {
        
    }

    @Override
    public void setItemStack(ItemStack itemstack) {
        itemLine.setItemStack(itemstack);
    }

    @Override
    public ItemStack getItemStack() {
        return itemLine.getItemStack();
    }

    @Override
    public Location getLocation() {
        return hologram.getLocation();
    }

    @Override
    public double getX() {
        return hologram.getX();
    }

    @Override
    public double getY() {
        return hologram.getY();
    }

    @Override
    public double getZ() {
        return hologram.getZ();
    }

    @Override
    public World getWorld() {
        return hologram.getWorld();
    }

    @Override
    public void teleport(Location location) {
        hologram.teleport(location);
    }

    @Override
    public void setTouchHandler(ItemTouchHandler handler) {
        this.touchHandler = handler;
        
        if (handler != null) {
            itemLine.setTouchHandler(new ItemTouchHandlerAdapter(this, handler));
        } else {
            itemLine.setTouchHandler(null);
        }
    }

    @Override
    public ItemTouchHandler getTouchHandler() {
        return touchHandler;
    }

    @Override
    public boolean hasTouchHandler() {
        return touchHandler != null;
    }

    @Override
    public void setPickupHandler(PickupHandler handler) {
        this.pickupHandler = handler;
        
        if (handler != null) {
            itemLine.setPickupHandler(new PickupHandlerAdapter(this, handler));
        } else {
            itemLine.setPickupHandler(null);
        }
    }

    @Override
    public PickupHandler getPickupHandler() {
        return pickupHandler;
    }

    @Override
    public boolean hasPickupHandler() {
        return pickupHandler != null;
    }

    @Override
    public long getCreationTimestamp() {
        return hologram.getCreationTimestamp();
    }

    @Override
    public void delete() {
        hologram.delete();
        
        activeFloatingItems.get(plugin).remove(this);
    }

    @Override
    public boolean isDeleted() {
        return hologram.isDeleted();
    }

}
