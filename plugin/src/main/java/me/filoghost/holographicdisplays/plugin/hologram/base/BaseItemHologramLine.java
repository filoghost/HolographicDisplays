/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.hologram.base;

import me.filoghost.fcommons.Preconditions;
import me.filoghost.holographicdisplays.common.nms.entity.ItemNMSPacketEntity;
import me.filoghost.holographicdisplays.plugin.hologram.tracking.ItemLineTracker;
import me.filoghost.holographicdisplays.plugin.hologram.tracking.LineTrackerManager;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public abstract class BaseItemHologramLine extends BaseClickableHologramLine implements Collectable {

    private ItemStack itemStack;

    public BaseItemHologramLine(BaseHologram hologram, ItemStack itemStack) {
        super(hologram);
        setItemStack(itemStack);
    }

    @Override
    public ItemLineTracker createTracker(LineTrackerManager trackerManager) {
        return trackerManager.startTracking(this);
    }

    public void onPickup(Player player) {
        if (hasPickupCallback() && canInteract(player)) {
            invokePickupCallback(player);
        }
    }

    public @Nullable ItemStack getItemStack() {
        return clone(itemStack);
    }

    public void setItemStack(@Nullable ItemStack itemStack) {
        checkNotDeleted();

        if (itemStack != null) {
            Preconditions.checkArgument(0 < itemStack.getAmount() && itemStack.getAmount() <= 64,
                    "itemStack's amount must be between 1 and 64");
        }
        this.itemStack = clone(itemStack);
        setChanged();
    }

    private ItemStack clone(@Nullable ItemStack itemStack) {
        return itemStack != null ? itemStack.clone() : null;
    }

    @Override
    public double getHeight() {
        return ItemNMSPacketEntity.ITEM_HEIGHT;
    }

    @Override
    public String toString() {
        return "ItemLine{"
                + "itemStack=" + itemStack
                + "}";
    }

}
