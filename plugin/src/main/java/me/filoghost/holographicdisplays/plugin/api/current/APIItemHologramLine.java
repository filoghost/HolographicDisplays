/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.api.current;

import me.filoghost.holographicdisplays.api.beta.hologram.line.HologramLineClickListener;
import me.filoghost.holographicdisplays.api.beta.hologram.line.HologramLinePickupListener;
import me.filoghost.holographicdisplays.api.beta.hologram.line.ItemHologramLine;
import me.filoghost.holographicdisplays.plugin.hologram.base.BaseItemHologramLine;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

class APIItemHologramLine extends BaseItemHologramLine implements ItemHologramLine, APIClickableHologramLine {

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
    public void setClickListener(@Nullable HologramLineClickListener clickListener) {
        checkNotDeleted();

        this.clickListener = clickListener;
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
