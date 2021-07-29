/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.hologram.api;

import me.filoghost.holographicdisplays.api.hologram.PickupHandler;
import me.filoghost.holographicdisplays.api.hologram.TouchHandler;
import me.filoghost.holographicdisplays.api.hologram.ItemLine;
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
        TouchHandler oldTouchHandler = getTouchHandler();
        super.setTouchHandler(touchHandler);
        v2Adapter.onNewTouchHandlerChange(oldTouchHandler, touchHandler);
    }

    @Override
    public void setPickupHandler(@Nullable PickupHandler pickupHandler) {
        PickupHandler oldPickupHandler = getPickupHandler();
        super.setPickupHandler(pickupHandler);
        v2Adapter.onNewPickupHandlerChange(oldPickupHandler, pickupHandler);
    }

    @Override
    public V2ItemLineAdapter getV2Adapter() {
        return v2Adapter;
    }

}
