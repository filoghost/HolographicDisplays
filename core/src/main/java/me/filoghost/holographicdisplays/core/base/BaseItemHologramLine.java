/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.core.base;

import me.filoghost.fcommons.Preconditions;
import me.filoghost.fcommons.logging.Log;
import me.filoghost.holographicdisplays.core.CorePreconditions;
import me.filoghost.holographicdisplays.nms.common.entity.ItemNMSPacketEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public abstract class BaseItemHologramLine extends BaseClickableHologramLine {

    private ItemStack itemStack;

    public BaseItemHologramLine(BaseHologram hologram, ItemStack itemStack) {
        super(hologram);
        setItemStack(itemStack);
    }

    public void onPickup(Player player) {
        try {
            invokeExternalPickupCallback(player);
        } catch (Throwable t) {
            Log.warning("The plugin " + getCreatorPlugin().getName() + " generated an exception"
                    + " when the player " + player.getName() + " picked up an item from a hologram.", t);
        }
    }

    public abstract boolean hasPickupCallback();

    protected abstract void invokeExternalPickupCallback(Player player);

    public @Nullable ItemStack getItemStack() {
        return clone(itemStack);
    }

    public void setItemStack(@Nullable ItemStack itemStack) {
        CorePreconditions.checkMainThread();
        checkNotDeleted();

        if (itemStack != null) {
            Preconditions.checkArgument(0 < itemStack.getAmount() && itemStack.getAmount() <= 64,
                    "itemStack.getAmount() must be between 1 and 64");
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
