/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.api.v2;

import com.gmail.filoghost.holographicdisplays.api.handler.PickupHandler;
import com.gmail.filoghost.holographicdisplays.api.handler.TouchHandler;
import com.gmail.filoghost.holographicdisplays.api.line.ItemLine;
import me.filoghost.holographicdisplays.api.hologram.line.ClickListener;
import me.filoghost.holographicdisplays.api.hologram.line.PickupListener;
import me.filoghost.holographicdisplays.plugin.hologram.base.BaseItemLine;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings("deprecation")
public class V2ItemLine extends BaseItemLine implements ItemLine, V2HologramLine {

    private final V2Hologram parent;

    public V2ItemLine(V2Hologram parent, ItemStack itemStack) {
        super(parent, itemStack);
        this.parent = parent;
    }

    @Override
    public V2Hologram getParent() {
        return parent;
    }

    @Override
    public void setTouchHandler(TouchHandler touchHandler) {
        if (touchHandler != null) {
            super.setClickListener(new V3ClickListenerAdapter(touchHandler));
        } else {
            super.setClickListener(null);
        }
    }

    @Override
    public TouchHandler getTouchHandler() {
        ClickListener clickListener = super.getClickListener();
        if (clickListener instanceof V3ClickListenerAdapter) {
            return ((V3ClickListenerAdapter) clickListener).getV2TouchHandler();
        } else {
            return null;
        }
    }

    @Override
    public void setPickupHandler(PickupHandler pickupHandler) {
        if (pickupHandler != null) {
            super.setPickupListener(new V3PickupListenerAdapter(pickupHandler));
        } else {
            super.setPickupListener(null);
        }
    }

    @Override
    public PickupHandler getPickupHandler() {
        PickupListener pickupListener = super.getPickupListener();
        if (pickupListener instanceof V3PickupListenerAdapter) {
            return ((V3PickupListenerAdapter) pickupListener).getV2PickupHandler();
        } else {
            return null;
        }
    }

}
