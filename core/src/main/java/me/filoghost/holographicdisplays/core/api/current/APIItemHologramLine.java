/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.core.api.current;

import me.filoghost.holographicdisplays.api.hologram.line.HologramLineClickListener;
import me.filoghost.holographicdisplays.api.hologram.line.HologramLinePickupListener;
import me.filoghost.holographicdisplays.api.hologram.line.ItemHologramLine;
import me.filoghost.holographicdisplays.core.CorePreconditions;
import me.filoghost.holographicdisplays.core.base.BaseItemHologramLine;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

class APIItemHologramLine extends BaseItemHologramLine implements ItemHologramLine, APIHologramLine {

    private HologramLinePickupListener pickupListener;
    private HologramLineClickListener clickListener;

    APIItemHologramLine(APIHologram hologram, ItemStack itemStack) {
        super(hologram, itemStack);
    }

    @Override
    public @Nullable HologramLinePickupListener getPickupListener() {
        CorePreconditions.checkMainThread();
        return pickupListener;
    }

    @Override
    public void setPickupListener(@Nullable HologramLinePickupListener pickupListener) {
        CorePreconditions.checkMainThread();
        checkNotDeleted();

        this.pickupListener = pickupListener;
    }

    @Override
    public boolean hasPickupCallback() {
        return pickupListener != null;
    }

    @Override
    protected void invokeExternalPickupCallback(Player player) {
        if (pickupListener != null) {
            pickupListener.onPickup(new SimpleHologramLinePickupEvent(player));
        }
    }

    @Override
    public @Nullable HologramLineClickListener getClickListener() {
        CorePreconditions.checkMainThread();
        return clickListener;
    }

    @Override
    public void setClickListener(@Nullable HologramLineClickListener clickListener) {
        CorePreconditions.checkMainThread();
        checkNotDeleted();

        this.clickListener = clickListener;
        setChanged();
    }

    @Override
    public boolean hasClickCallback() {
        return clickListener != null;
    }

    @Override
    protected void invokeExternalClickCallback(Player player) {
        if (clickListener != null) {
            clickListener.onClick(new SimpleHologramLineClickEvent(player));
        }
    }

}
