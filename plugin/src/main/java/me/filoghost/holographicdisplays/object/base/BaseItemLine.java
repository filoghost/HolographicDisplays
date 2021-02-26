/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.object.base;

import me.filoghost.fcommons.Preconditions;
import me.filoghost.holographicdisplays.api.handler.PickupHandler;
import me.filoghost.holographicdisplays.api.line.ItemLine;
import me.filoghost.holographicdisplays.nms.interfaces.entity.NMSArmorStand;
import me.filoghost.holographicdisplays.nms.interfaces.entity.NMSItem;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

public class BaseItemLine extends BaseTouchableLine implements ItemLine {
    
    private ItemStack itemStack;
    private PickupHandler pickupHandler;
    
    private NMSItem itemEntity;
    private NMSArmorStand vehicleEntity;
    
    public BaseItemLine(BaseHologram parent, ItemStack itemStack) {
        super(parent);
        setItemStack(itemStack);
    }
    
    @Override
    public ItemStack getItemStack() {
        return itemStack;
    }

    @Override
    public void setItemStack(ItemStack itemStack) {
        Preconditions.notNull(itemStack, "itemStack");
        Preconditions.checkArgument(0 < itemStack.getAmount() && itemStack.getAmount() <= 64, "Item must have amount between 1 and 64");
        this.itemStack = itemStack;
        
        if (itemEntity != null) {
            itemEntity.setItemStackNMS(itemStack);
        }
    }

    @Override
    public PickupHandler getPickupHandler() {
        return pickupHandler;
    }

    @Override
    public void setPickupHandler(PickupHandler pickupHandler) {
        this.pickupHandler = pickupHandler;
    }

    @Override
    public void spawnEntities(World world, double x, double y, double z) {
        super.spawnEntities(world, x, y, z);
        
        if (itemStack != null) {
            itemEntity = getNMSManager().spawnNMSItem(world, x, y + getItemSpawnOffset(), z, this, itemStack);
            vehicleEntity = getNMSManager().spawnNMSArmorStand(world, x, y + getItemSpawnOffset(), z, this);

            itemEntity.setPassengerOfNMS(vehicleEntity);
        }
    }
    
    @Override
    public void teleportEntities(double x, double y, double z) {
        super.teleportEntities(x, y, z);

        if (vehicleEntity != null) {
            vehicleEntity.setLocationNMS(x, y + getItemSpawnOffset(), z);
        }
        if (itemEntity != null) {
            itemEntity.setLocationNMS(x, y + getItemSpawnOffset(), z);
        }
    }

    @Override
    public void despawnEntities() {
        super.despawnEntities();
        
        if (vehicleEntity != null) {
            vehicleEntity.killEntityNMS();
            vehicleEntity = null;
        }
        
        if (itemEntity != null) {
            itemEntity.killEntityNMS();
            itemEntity = null;
        }
    }

    @Override
    public double getHeight() {
        return 0.7;
    }

    @Override
    public void collectEntityIDs(Collection<Integer> collector) {
        super.collectEntityIDs(collector);
        
        if (vehicleEntity != null) {
            collector.add(vehicleEntity.getIdNMS());
        }
        if (itemEntity != null) {
            collector.add(itemEntity.getIdNMS());
        }
    }

    public NMSItem getNMSItem() {
        return itemEntity;
    }

    public NMSArmorStand getNMSVehicle() {
        return vehicleEntity;
    }
    
    private double getItemSpawnOffset() {
        return 0;
    }

    @Override
    public String toString() {
        return "ItemLine [itemStack=" + itemStack + ", pickupHandler=" + pickupHandler + "]";
    }
    
}
