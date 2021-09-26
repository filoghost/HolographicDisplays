/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.api.current;

import me.filoghost.holographicdisplays.api.hologram.line.ClickListener;
import me.filoghost.holographicdisplays.api.hologram.line.ItemHologramLine;
import me.filoghost.holographicdisplays.api.hologram.line.PickupListener;
import me.filoghost.holographicdisplays.plugin.hologram.base.BaseItemHologramLine;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

class APIItemHologramLine extends BaseItemHologramLine implements ItemHologramLine, APIClickableHologramLine {

    private PickupListener pickupListener;
    private ClickListener clickListener;

    APIItemHologramLine(APIHologram hologram, ItemStack itemStack) {
        super(hologram, itemStack);
    }

    @Override
    public @Nullable PickupListener getPickupListener() {
        return pickupListener;
    }

    @Override
    public void setPickupListener(@Nullable PickupListener pickupListener) {
        checkNotDeleted();

        this.pickupListener = pickupListener;
    }

    @Override
    public void setClickListener(@Nullable ClickListener clickListener) {
        checkNotDeleted();

        this.clickListener = clickListener;
    }

    @Override
    public @Nullable ClickListener getClickListener() {
        return clickListener;
    }

    @Override
    public boolean hasPickupCallback() {
        return pickupListener != null;
    }

    @Override
    public void invokePickupCallback(Player player) {
        try {
            if (pickupListener != null) {
                pickupListener.onPickup(new SimpleHologramLinePickupEvent(player));
            }
        } catch (Throwable t) {
            logPickupCallbackException(getCreatorPlugin(), player, t);
        }
    }

}
