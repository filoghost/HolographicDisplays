/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package com.gmail.filoghost.holograms.api.internal;

import com.gmail.filoghost.holograms.api.FloatingItem;
import com.gmail.filoghost.holograms.api.ItemTouchHandler;
import com.gmail.filoghost.holograms.api.PickupHandler;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.List;

@SuppressWarnings("deprecation")
class FloatingItemAdapter extends BaseHologramAdapter implements FloatingItem {
    
    private final me.filoghost.holographicdisplays.api.line.ItemLine itemLine;
    private ItemTouchHandler touchHandler;
    private PickupHandler pickupHandler;

    FloatingItemAdapter(Plugin plugin, Location source, ItemStack itemstack) {
        super(plugin, source);
        this.itemLine = hologram.appendItemLine(itemstack);

        ActiveObjectsRegistry.addFloatingItem(this);
    }
    
    FloatingItemAdapter(Plugin plugin, Location source, ItemStack itemstack, List<Player> whoCanSee) {
        super(plugin, source);
        restrictVisibityTo(whoCanSee);
        this.itemLine = hologram.appendItemLine(itemstack);
        
        ActiveObjectsRegistry.addFloatingItem(this);
    }

    @Override
    public void delete() {
        hologram.delete();
        
        ActiveObjectsRegistry.removeFloatingItem(this);
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
    public boolean isDeleted() {
        return hologram.isDeleted();
    }

}
