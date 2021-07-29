/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.hologram.base;

import me.filoghost.fcommons.Preconditions;
import me.filoghost.fcommons.logging.Log;
import me.filoghost.holographicdisplays.api.hologram.PickupHandler;
import me.filoghost.holographicdisplays.common.hologram.StandardItemLine;
import me.filoghost.holographicdisplays.plugin.hologram.tracking.ItemLineTracker;
import me.filoghost.holographicdisplays.plugin.hologram.tracking.LineTrackerManager;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.MustBeInvokedByOverriders;
import org.jetbrains.annotations.Nullable;

public abstract class BaseItemLine extends BaseTouchableLine implements StandardItemLine {

    private ItemStack itemStack;
    private PickupHandler pickupHandler;

    public BaseItemLine(BaseHologram<?> hologram, ItemStack itemStack) {
        super(hologram);
        setItemStack(itemStack);
    }

    @Override
    public ItemLineTracker createTracker(LineTrackerManager trackerManager) {
        return trackerManager.startTracking(this);
    }

    @Override
    public void onPickup(Player player) {
        if (isDeleted()
                || !player.isOnline()
                || !isTrackedPlayer(player)
                || player.getGameMode() == GameMode.SPECTATOR
                || pickupHandler == null
                || !isVisibleTo(player)) {
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

    @MustBeInvokedByOverriders
    public void setPickupHandler(@Nullable PickupHandler pickupHandler) {
        this.pickupHandler = pickupHandler;
    }

    @Override
    public @Nullable ItemStack getItemStack() {
        return clone(itemStack);
    }

    public void setItemStack(@Nullable ItemStack itemStack) {
        if (itemStack != null) {
            Preconditions.checkArgument(0 < itemStack.getAmount() && itemStack.getAmount() <= 64, "itemStack's amount must be between 1 and 64");
        }
        this.itemStack = clone(itemStack);
        setChanged();
    }

    private ItemStack clone(@Nullable ItemStack itemStack) {
        return itemStack != null ? itemStack.clone() : null;
    }

    @Override
    public double getHeight() {
        return 0.7;
    }

    @Override
    public String toString() {
        return "ItemLine [itemStack=" + itemStack + "]";
    }

}
