/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.api.v2;

import com.gmail.filoghost.holographicdisplays.api.handler.PickupHandler;
import com.gmail.filoghost.holographicdisplays.api.handler.TouchHandler;
import com.gmail.filoghost.holographicdisplays.api.line.ItemLine;
import me.filoghost.holographicdisplays.plugin.hologram.base.BaseItemHologramLine;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings("deprecation")
public class V2ItemLine extends BaseItemHologramLine implements ItemLine, V2CollectableLine, V2TouchableLine {

    private final V2Hologram hologram;

    private TouchHandler touchHandler;
    private PickupHandler pickupHandler;

    public V2ItemLine(V2Hologram hologram, ItemStack itemStack) {
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
    public void setPickupHandler(PickupHandler pickupHandler) {
        this.pickupHandler = pickupHandler;
    }

    @Override
    public PickupHandler getPickupHandler() {
        return pickupHandler;
    }

}
