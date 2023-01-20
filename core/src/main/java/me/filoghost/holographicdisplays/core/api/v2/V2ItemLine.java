/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.core.api.v2;

import com.gmail.filoghost.holographicdisplays.api.handler.PickupHandler;
import com.gmail.filoghost.holographicdisplays.api.handler.TouchHandler;
import com.gmail.filoghost.holographicdisplays.api.line.ItemLine;
import me.filoghost.holographicdisplays.api.hologram.line.HologramClickType;
import me.filoghost.holographicdisplays.core.base.BaseItemHologramLine;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings("deprecation")
class V2ItemLine extends BaseItemHologramLine implements ItemLine, V2HologramLine {

    private final V2Hologram hologram;

    private TouchHandler touchHandler;
    private PickupHandler pickupHandler;

    V2ItemLine(V2Hologram hologram, ItemStack itemStack) {
        super(hologram, itemStack);
        this.hologram = hologram;
    }

    @Override
    public V2Hologram getParent() {
        return hologram;
    }

    @Override
    public void setTouchHandler(TouchHandler touchHandler) {
        this.touchHandler = touchHandler;
    }

    @Override
    public TouchHandler getTouchHandler() {
        return touchHandler;
    }

    @Override
    public boolean hasClickCallback() {
        return touchHandler != null;
    }

    @Override
    protected void invokeExternalClickCallback(Player player, HologramClickType clickType) {
        if (touchHandler != null) {
            touchHandler.onTouch(player, clickType);
        }
    }

    @Override
    public void setPickupHandler(PickupHandler pickupHandler) {
        this.pickupHandler = pickupHandler;
    }

    @Override
    public PickupHandler getPickupHandler() {
        return pickupHandler;
    }

    @Override
    public boolean hasPickupCallback() {
        return pickupHandler != null;
    }

    @Override
    protected void invokeExternalPickupCallback(Player player) {
        if (pickupHandler != null) {
            pickupHandler.onPickup(player);
        }
    }

}
