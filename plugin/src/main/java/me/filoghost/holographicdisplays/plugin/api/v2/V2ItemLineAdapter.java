/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.api.v2;

import com.gmail.filoghost.holographicdisplays.api.handler.PickupHandler;
import com.gmail.filoghost.holographicdisplays.api.line.ItemLine;
import me.filoghost.holographicdisplays.plugin.hologram.api.APIItemLine;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings("deprecation")
public class V2ItemLineAdapter extends V2TouchableLineAdapter implements ItemLine {

    private final APIItemLine v3ItemLine;
    private PickupHandler v2PickupHandler;

    public V2ItemLineAdapter(APIItemLine v3ItemLine) {
        super(v3ItemLine);
        this.v3ItemLine = v3ItemLine;
    }

    public void onV3PickupHandlerChange(
            me.filoghost.holographicdisplays.api.hologram.PickupHandler previous,
            me.filoghost.holographicdisplays.api.hologram.PickupHandler current) {
        if (previous != current) {
            v2PickupHandler = null; // Clear the field to force a new lazy initialization
        }
    }

    @Override
    public void setPickupHandler(PickupHandler v2PickupHandler) {
        if (v2PickupHandler == null) {
            v3ItemLine.setPickupHandler(null);
        } else if (v2PickupHandler instanceof V2PickupHandlerAdapter) {
            // Adapter created from the getter method, simply unwrap it
            v3ItemLine.setPickupHandler(((V2PickupHandlerAdapter) v2PickupHandler).getV3PickupHandler());
        } else {
            me.filoghost.holographicdisplays.api.hologram.PickupHandler v3PickupHandler = v3ItemLine.getPickupHandler();

            // Adapt the old v2 handler to the new API, creating a new instance only if the wrapped handler changed
            if (!(v3PickupHandler instanceof V3PickupHandlerAdapter)
                    || ((V3PickupHandlerAdapter) v3PickupHandler).getV2PickupHandler() != v2PickupHandler) {
                v3ItemLine.setPickupHandler(new V3PickupHandlerAdapter(v2PickupHandler));
            }
        }
        this.v2PickupHandler = v2PickupHandler;
    }

    @Override
    public PickupHandler getPickupHandler() {
        // Lazy initialization
        if (v2PickupHandler == null) {
            me.filoghost.holographicdisplays.api.hologram.PickupHandler v3PickupHandler = v3ItemLine.getPickupHandler();

            if (v3PickupHandler == null) {
                // Keep it null
            } else if (v3PickupHandler instanceof V3PickupHandlerAdapter) {
                // Adapter created from the setter method, simply unwrap it
                v2PickupHandler = ((V3PickupHandlerAdapter) v3PickupHandler).getV2PickupHandler();
            } else {
                // Adapt the new handler to the old v2 API
                v2PickupHandler = new V2PickupHandlerAdapter(v3PickupHandler);
            }
        }
        return v2PickupHandler;
    }

    @Override
    public ItemStack getItemStack() {
        return v3ItemLine.getItemStack();
    }

    @Override
    public void setItemStack(ItemStack itemStack) {
        v3ItemLine.setItemStack(itemStack);
    }

}
