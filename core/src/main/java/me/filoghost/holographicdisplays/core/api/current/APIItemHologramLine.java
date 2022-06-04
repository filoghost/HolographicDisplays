/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.core.api.current;

import me.filoghost.holographicdisplays.api.hologram.line.HologramLineClickListener;
import me.filoghost.holographicdisplays.api.hologram.line.HologramLinePickupListener;
import me.filoghost.holographicdisplays.api.hologram.line.ItemHologramLine;
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
        return pickupListener;
    }

    @Override
    public void setPickupListener(@Nullable HologramLinePickupListener pickupListener) {
        checkNotDeleted();

        this.pickupListener = pickupListener;
    }

    @Override
    public @Nullable HologramLineClickListener getClickListener() {
        return clickListener;
    }

    @Override
    public boolean hasPickupCallback() {
        return pickupListener != null;
    }

    @Override
    protected void invokePickupCallback(Player player) {
        if (pickupListener != null) {
            pickupListener.onPickup(new SimpleHologramLinePickupEvent(player));
        }
    }

    @Override
    public void setClickListener(@Nullable HologramLineClickListener clickListener) {
        checkNotDeleted();

        this.clickListener = clickListener;
    }

    @Override
    public boolean hasClickCallback() {
        return clickListener != null;
    }

    @Override
    protected void invokeClickCallback(Player player) {
        if (clickListener != null) {
            clickListener.onClick(new SimpleHologramLineClickEvent(player));
        }
    }

}
