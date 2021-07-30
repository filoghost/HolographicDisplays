/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.hologram.api;

import me.filoghost.holographicdisplays.api.hologram.ItemLine;
import me.filoghost.holographicdisplays.api.hologram.PickupListener;
import me.filoghost.holographicdisplays.api.hologram.ClickListener;
import me.filoghost.holographicdisplays.plugin.api.v2.V2ItemLineAdapter;
import me.filoghost.holographicdisplays.plugin.hologram.base.BaseItemLine;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class APIItemLine extends BaseItemLine implements ItemLine, APIClickableLine {

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
    public void setClickListener(@Nullable ClickListener clickListener) {
        ClickListener oldClickListener = getClickListener();
        super.setClickListener(clickListener);
        v2Adapter.onV3ClickListenerChange(oldClickListener, clickListener);
    }

    @Override
    public void setPickupListener(@Nullable PickupListener pickupListener) {
        PickupListener oldPickupListener = getPickupListener();
        super.setPickupListener(pickupListener);
        v2Adapter.onV3PickupListenerChange(oldPickupListener, pickupListener);
    }

    @Override
    public V2ItemLineAdapter getV2Adapter() {
        return v2Adapter;
    }

}
