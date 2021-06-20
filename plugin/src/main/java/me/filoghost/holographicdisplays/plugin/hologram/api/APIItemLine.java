/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.hologram.api;

import me.filoghost.holographicdisplays.api.handler.PickupHandler;
import me.filoghost.holographicdisplays.api.handler.TouchHandler;
import me.filoghost.holographicdisplays.api.line.ItemLine;
import me.filoghost.holographicdisplays.plugin.api.v2.V2ItemLineAdapter;
import me.filoghost.holographicdisplays.plugin.hologram.base.BaseItemLine;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class APIItemLine extends BaseItemLine implements ItemLine, APITouchableLine {

    private final APIHologram parent;
    private final V2ItemLineAdapter v2Adapter;

    public APIItemLine(APIHologram parent, ItemStack itemStack) {
        super(parent, itemStack);
        this.parent = parent;
        this.v2Adapter = new V2ItemLineAdapter(this);
    }

    @Override
    public @NotNull APIHologram getParent() {
        return parent;
    }

    @Override
    public void setTouchHandler(@Nullable TouchHandler touchHandler) {
        v2Adapter.onNewTouchHandlerChange(getTouchHandler(), touchHandler);
        super.setTouchHandler(touchHandler);
    }

    @Override
    public void setPickupHandler(@Nullable PickupHandler pickupHandler) {
        v2Adapter.onNewPickupHandlerChange(getPickupHandler(), pickupHandler);
        super.setPickupHandler(pickupHandler);
    }

    @Override
    public V2ItemLineAdapter getV2Adapter() {
        return v2Adapter;
    }

}
