/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.object.line;

import me.filoghost.holographicdisplays.HolographicDisplays;
import me.filoghost.holographicdisplays.api.handler.PickupHandler;
import me.filoghost.holographicdisplays.api.handler.TouchHandler;
import me.filoghost.holographicdisplays.api.line.ItemLine;
import me.filoghost.holographicdisplays.nms.interfaces.entity.NMSArmorStand;
import me.filoghost.holographicdisplays.nms.interfaces.entity.NMSEntityBase;
import me.filoghost.holographicdisplays.nms.interfaces.entity.NMSItem;
import me.filoghost.holographicdisplays.object.CraftHologram;
import me.filoghost.holographicdisplays.util.Offsets;
import me.filoghost.holographicdisplays.util.Validator;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

public class CraftItemLine extends CraftTouchableLine implements ItemLine {

    private ItemStack itemStack;
    private PickupHandler pickupHandler;
    
    private NMSItem nmsItem;
    private NMSArmorStand nmsVehicle;
    
    public CraftItemLine(CraftHologram parent, ItemStack itemStack) {
        super(0.7, parent);
        setItemStack(itemStack);
    }
    
    @Override
    public ItemStack getItemStack() {
        return itemStack;
    }

    @Override
    public void setItemStack(ItemStack itemStack) {
        Validator.notNull(itemStack, "itemStack");
        Validator.isTrue(0 < itemStack.getAmount() && itemStack.getAmount() <= 64, "Item must have amount between 1 and 64");
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
    public void spawn(World world, double x, double y, double z) {
        super.spawn(world, x, y, z);
        
        if (itemStack != null) {
            double offset = getItemOffset();
            
            nmsItem = HolographicDisplays.getNMSManager().spawnNMSItem(world, x, y + offset, z, this, itemStack, HolographicDisplays.getMainListener());
            nmsVehicle = HolographicDisplays.getNMSManager().spawnNMSArmorStand(world, x, y + offset, z, this, HolographicDisplays.hasProtocolLibHook());

            nmsItem.setPassengerOfNMS(nmsVehicle);
        }
    }

    
    @Override
    public void despawn() {
        super.despawn();
        
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
    public void teleport(double x, double y, double z) {
        super.teleport(x, y, z);
        
        double offset = getItemOffset();
        
        if (nmsVehicle != null) {
            nmsVehicle.setLocationNMS(x, y + offset, z, HolographicDisplays.hasProtocolLibHook());
        }
        
        if (nmsItem != null) {
            nmsItem.setLocationNMS(x, y + offset, z);
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
        return "CraftItemLine [itemStack=" + itemStack + ", pickupHandler=" + pickupHandler + "]";
    }
    
}
