/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.api.v2;

import com.gmail.filoghost.holographicdisplays.api.handler.PickupHandler;
import com.gmail.filoghost.holographicdisplays.api.line.ItemLine;
import me.filoghost.holographicdisplays.api.hologram.PickupListener;
import me.filoghost.holographicdisplays.plugin.hologram.api.APIItemLine;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings("deprecation")
public class V2ItemLineAdapter extends V2TouchableLineAdapter implements ItemLine {

    private final APIItemLine v3Line;
    private PickupHandler v2PickupHandler;

    public V2ItemLineAdapter(APIItemLine v3Line) {
        super(v3Line);
        this.v3Line = v3Line;
    }

    public void onV3PickupListenerChange(PickupListener previous, PickupListener current) {
        if (previous != current) {
            v2PickupHandler = null; // Clear the field to force a new lazy initialization
        }
    }

    @Override
    public void setPickupHandler(PickupHandler v2PickupHandler) {
        if (v2PickupHandler == null) {
            v3Line.setPickupListener(null);
        } else if (v2PickupHandler instanceof V2PickupHandlerAdapter) {
            // Adapter created from the getter method, simply unwrap it
            v3Line.setPickupListener(((V2PickupHandlerAdapter) v2PickupHandler).getV3PickupListener());
        } else {
            PickupListener v3PickupListener = v3Line.getPickupListener();

            // Adapt the old v2 handler to the new API, creating a new instance only if the wrapped handler changed
            if (!(v3PickupListener instanceof V3PickupListenerAdapter)
                    || ((V3PickupListenerAdapter) v3PickupListener).getV2PickupHandler() != v2PickupHandler) {
                v3Line.setPickupListener(new V3PickupListenerAdapter(v2PickupHandler));
            }
        }
        this.v2PickupHandler = v2PickupHandler;
    }

    @Override
    public PickupHandler getPickupHandler() {
        // Lazy initialization
        if (v2PickupHandler == null) {
            PickupListener v3PickupListener = v3Line.getPickupListener();

            if (v3PickupListener == null) {
                // Keep it null
            } else if (v3PickupListener instanceof V3PickupListenerAdapter) {
                // Adapter created from the setter method, simply unwrap it
                v2PickupHandler = ((V3PickupListenerAdapter) v3PickupListener).getV2PickupHandler();
            } else {
                // Adapt the new handler to the old v2 API
                v2PickupHandler = new V2PickupHandlerAdapter(v3PickupListener);
            }
        }
        return v2PickupHandler;
    }

    @Override
    public ItemStack getItemStack() {
        return v3Line.getItemStack();
    }

    @Override
    public void setItemStack(ItemStack itemStack) {
        v3Line.setItemStack(itemStack);
    }

}
