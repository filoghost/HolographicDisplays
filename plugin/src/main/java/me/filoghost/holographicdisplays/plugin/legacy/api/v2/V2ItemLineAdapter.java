/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.legacy.api.v2;

import com.gmail.filoghost.holographicdisplays.api.handler.PickupHandler;
import com.gmail.filoghost.holographicdisplays.api.line.ItemLine;
import me.filoghost.holographicdisplays.plugin.object.api.APIItemLine;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings("deprecation")
public class V2ItemLineAdapter extends V2TouchableLineAdapter implements ItemLine {

    private final APIItemLine newItemLine;
    private PickupHandler v2PickupHandler;

    public V2ItemLineAdapter(APIItemLine newItemLine) {
        super(newItemLine);
        this.newItemLine = newItemLine;
    }

    public void onNewPickupHandlerChange(
            me.filoghost.holographicdisplays.api.handler.PickupHandler previous,
            me.filoghost.holographicdisplays.api.handler.PickupHandler current) {
        if (previous != current) {
            v2PickupHandler = null; // Clear the field to force a new lazy initialization
        }
    }

    @Override
    public void setPickupHandler(PickupHandler v2PickupHandler) {
        if (v2PickupHandler == null) {
            newItemLine.setPickupHandler(null);
        } else if (v2PickupHandler instanceof V2PickupHandlerAdapter) {
            // Adapter created from the getPickupHandler() method, simply unwrap it
            newItemLine.setPickupHandler(((V2PickupHandlerAdapter) v2PickupHandler).getNewPickupHandler());
        } else {
            me.filoghost.holographicdisplays.api.handler.PickupHandler newPickupHandler = newItemLine.getPickupHandler();

            // Adapt the old v2 handler to the new API, creating a new instance only if the wrapped handler changed
            if (!(newPickupHandler instanceof NewPickupHandlerAdapter)
                    || ((NewPickupHandlerAdapter) newPickupHandler).getV2PickupHandler() != v2PickupHandler) {
                newItemLine.setPickupHandler(new NewPickupHandlerAdapter(v2PickupHandler));
            }
        }
        this.v2PickupHandler = v2PickupHandler;
    }

    @Override
    public PickupHandler getPickupHandler() {
        // Lazy initialization
        if (v2PickupHandler == null) {
            me.filoghost.holographicdisplays.api.handler.PickupHandler newPickupHandler = newItemLine.getPickupHandler();

            if (newPickupHandler == null) {
                // Keep it null
            } else if (newPickupHandler instanceof NewPickupHandlerAdapter) {
                // Adapter created from the setPickupHandler() method, simply unwrap it
                v2PickupHandler = ((NewPickupHandlerAdapter) newPickupHandler).getV2PickupHandler();
            } else {
                // Adapt the new handler to the old v2 API
                v2PickupHandler = new V2PickupHandlerAdapter(newPickupHandler);
            }
        }
        return v2PickupHandler;
    }

    @Override
    public ItemStack getItemStack() {
        return newItemLine.getItemStack();
    }

    @Override
    public void setItemStack(ItemStack itemStack) {
        newItemLine.setItemStack(itemStack);
    }

}
