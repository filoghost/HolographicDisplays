/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.hologram.base;

import me.filoghost.fcommons.Preconditions;
import me.filoghost.fcommons.logging.Log;
import me.filoghost.holographicdisplays.api.handler.PickupHandler;
import me.filoghost.holographicdisplays.common.DebugLogger;
import me.filoghost.holographicdisplays.common.hologram.StandardItemLine;
import me.filoghost.holographicdisplays.common.nms.SpawnFailedException;
import me.filoghost.holographicdisplays.common.nms.entity.NMSArmorStand;
import me.filoghost.holographicdisplays.common.nms.entity.NMSItem;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public abstract class BaseItemLine extends BaseTouchableLine implements StandardItemLine {

    private ItemStack itemStack;

    private NMSItem itemEntity;
    private NMSArmorStand itemVehicleEntity;
    private PickupHandler pickupHandler;

    public BaseItemLine(BaseHologram<?> hologram, ItemStack itemStack) {
        super(hologram);
        setItemStack(itemStack);
    }

    @Override
    public void onPickup(Player player) {
        if (pickupHandler == null || !getHologram().isVisibleTo(player)) {
            return;
        }

        try {
            pickupHandler.onPickup(player);
        } catch (Throwable t) {
            Log.warning("The plugin " + getHologram().getCreatorPlugin().getName() + " generated an exception"
                    + " when the player " + player.getName() + " picked up an item from a hologram.", t);
        }
    }

    public @Nullable PickupHandler getPickupHandler() {
        return pickupHandler;
    }

    public void setPickupHandler(@Nullable PickupHandler pickupHandler) {
        this.pickupHandler = pickupHandler;
    }

    public @Nullable ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(@Nullable ItemStack itemStack) {
        if (itemStack != null) {
            Preconditions.checkArgument(0 < itemStack.getAmount() && itemStack.getAmount() <= 64, "itemStack's amount must be between 1 and 64");
        }
        this.itemStack = itemStack;

        if (itemStack == null) {
            // Despawn the entity (if existing) since the item shouldn't be visible
            despawnItem();
            return;
        }

        if (itemEntity != null) {
            // Simply update the existing entity
            itemEntity.setItemStackNMS(itemStack);
        } else {
            // Spawn the entity, if it needs to be spawned
            if (super.isSpawned()) {
                spawnItem(getWorld(), getX(), getY(), getZ());
            }
        }
    }

    @Override
    public void spawnEntities(World world, double x, double y, double z) throws SpawnFailedException {
        super.spawnEntities(world, x, y, z);

        if (itemStack != null) {
            spawnItem(world, x, y, z);
        }
    }

    @Override
    public void teleportEntities(double x, double y, double z) {
        super.teleportEntities(x, y, z);

        if (itemVehicleEntity != null) {
            itemVehicleEntity.setLocationNMS(x, y + getItemSpawnOffset(), z);
        }
        if (itemEntity != null) {
            itemEntity.setLocationNMS(x, y + getItemSpawnOffset(), z);
        }
    }

    @Override
    public void despawnEntities() {
        super.despawnEntities();

        despawnItem();
    }

    private void spawnItem(World world, double x, double y, double z) {
        if (world != null) {
            try {
                itemEntity = getNMSManager().spawnNMSItem(world, x, y + getItemSpawnOffset(), z, this, itemStack);
                itemVehicleEntity = getNMSManager().spawnNMSArmorStand(world, x, y + getItemSpawnOffset(), z, this);
                itemVehicleEntity.setPassengerNMS(itemEntity);
            } catch (SpawnFailedException e) {
                DebugLogger.handleSpawnFail(e, this);
            }
        }
    }

    private void despawnItem() {
        if (itemVehicleEntity != null) {
            itemVehicleEntity.killEntityNMS();
            itemVehicleEntity = null;
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
    public void collectTrackedEntityIDs(Player player, Collection<Integer> collector) {
        super.collectTrackedEntityIDs(player, collector);

        if (itemVehicleEntity != null && itemVehicleEntity.isTrackedBy(player)) {
            collector.add(itemVehicleEntity.getIdNMS());
        }
        if (itemEntity != null && itemEntity.isTrackedBy(player)) {
            collector.add(itemEntity.getIdNMS());
        }
    }

    @Override
    public NMSItem getNMSItem() {
        return itemEntity;
    }

    @Override
    public NMSArmorStand getNMSItemVehicle() {
        return itemVehicleEntity;
    }

    private double getItemSpawnOffset() {
        return 0;
    }

    @Override
    public String toString() {
        return "ItemLine [itemStack=" + itemStack + "]";
    }

}
