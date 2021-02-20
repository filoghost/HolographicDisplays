/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.object.line;

import me.filoghost.fcommons.Preconditions;
import me.filoghost.holographicdisplays.api.handler.PickupHandler;
import me.filoghost.holographicdisplays.api.handler.TouchHandler;
import me.filoghost.holographicdisplays.api.line.ItemLine;
import me.filoghost.holographicdisplays.nms.interfaces.entity.NMSArmorStand;
import me.filoghost.holographicdisplays.nms.interfaces.entity.NMSEntityBase;
import me.filoghost.holographicdisplays.nms.interfaces.entity.NMSItem;
import me.filoghost.holographicdisplays.object.BaseHologram;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

public class ItemLineImpl extends TouchableLineImpl implements ItemLine {
    
    private ItemStack itemStack;
    private PickupHandler pickupHandler;
    
    private NMSItem nmsItem;
    private NMSArmorStand nmsVehicle;
    
    public ItemLineImpl(BaseHologram parent, ItemStack itemStack) {
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
        
        if (nmsItem != null) {
            nmsItem.setItemStackNMS(itemStack);
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
    public void setTouchHandler(TouchHandler touchHandler) {
        if (nmsVehicle != null) {
            Location loc = nmsVehicle.getBukkitEntityNMS().getLocation();
            super.setTouchHandler(touchHandler, loc.getWorld(), loc.getX(), loc.getY() - getItemOffset(), loc.getZ());
        } else {
            super.setTouchHandler(touchHandler, null, 0, 0, 0);
        }
    }

    @Override
    public void spawnEntities(World world, double x, double y, double z) {
        super.spawnEntities(world, x, y, z);
        
        if (itemStack != null) {
            nmsItem = getNMSManager().spawnNMSItem(world, x, y + getItemOffset(), z, this, itemStack);
            nmsVehicle = getNMSManager().spawnNMSArmorStand(world, x, y + getItemOffset(), z, this);

            nmsItem.setPassengerOfNMS(nmsVehicle);
        }
    }

    
    @Override
    public void despawnEntities() {
        super.despawnEntities();
        
        if (nmsVehicle != null) {
            nmsVehicle.killEntityNMS();
            nmsVehicle = null;
        }
        
        if (nmsItem != null) {
            nmsItem.killEntityNMS();
            nmsItem = null;
        }
    }

    @Override
    public double getHeight() {
        return 0.7;
    }

    @Override
    public void teleport(double x, double y, double z) {
        super.teleport(x, y, z);

        if (nmsVehicle != null) {
            nmsVehicle.setLocationNMS(x, y + getItemOffset(), z);
        }
        if (nmsItem != null) {
            nmsItem.setLocationNMS(x, y + getItemOffset(), z);
        }
    }

    @Override
    public int[] getEntitiesIDs() {
        if (isSpawned()) {
            if (touchSlime != null) {
                return ArrayUtils.addAll(new int[] {nmsVehicle.getIdNMS(), nmsItem.getIdNMS()}, touchSlime.getEntitiesIDs());
            } else {
                return new int[] {nmsVehicle.getIdNMS(), nmsItem.getIdNMS()};
            }
        } else {
            return new int[0];
        }
    }

    public NMSItem getNmsItem() {
        return nmsItem;
    }

    public NMSEntityBase getNmsVehicle() {
        return nmsVehicle;
    }
    
    private double getItemOffset() {
        return Offsets.ARMOR_STAND_WITH_ITEM;
    }

    @Override
    public String toString() {
        return "ItemLine [itemStack=" + itemStack + ", pickupHandler=" + pickupHandler + "]";
    }
    
}
