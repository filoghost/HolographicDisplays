/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.api.v2;

import com.gmail.filoghost.holographicdisplays.api.handler.TouchHandler;
import com.gmail.filoghost.holographicdisplays.api.line.TouchableLine;
import me.filoghost.holographicdisplays.plugin.hologram.api.APITouchableLine;

@SuppressWarnings("deprecation")
abstract class V2TouchableLineAdapter extends V2HologramLineAdapter implements TouchableLine {

    private final APITouchableLine v3TouchableLine;
    private TouchHandler v2TouchHandler;

    V2TouchableLineAdapter(APITouchableLine v3TouchableLine) {
        super(v3TouchableLine);
        this.v3TouchableLine = v3TouchableLine;
    }

    public void onV3TouchHandlerChange(
            me.filoghost.holographicdisplays.api.hologram.TouchHandler previous,
            me.filoghost.holographicdisplays.api.hologram.TouchHandler current) {
        if (previous != current) {
            v2TouchHandler = null; // Clear the field to force a new lazy initialization
        }
    }

    @Override
    public void setTouchHandler(TouchHandler v2TouchHandler) {
        if (v2TouchHandler == null) {
            v3TouchableLine.setTouchHandler(null);
        } else if (v2TouchHandler instanceof V2TouchHandlerAdapter) {
            // Adapter created from the getter method, simply unwrap it
            v3TouchableLine.setTouchHandler(((V2TouchHandlerAdapter) v2TouchHandler).getV3TouchHandler());
        } else {
            me.filoghost.holographicdisplays.api.hologram.TouchHandler v3TouchHandler = v3TouchableLine.getTouchHandler();

            // Adapt the old v2 handler to the new API, creating a new instance only if the wrapped handler changed
            if (!(v3TouchHandler instanceof V3TouchHandlerAdapter)
                    || ((V3TouchHandlerAdapter) v3TouchHandler).getV2TouchHandler() != v2TouchHandler) {
                v3TouchableLine.setTouchHandler(new V3TouchHandlerAdapter(v2TouchHandler));
            }
        }
        this.v2TouchHandler = v2TouchHandler;
    }

    @Override
    public TouchHandler getTouchHandler() {
        // Lazy initialization
        if (v2TouchHandler == null) {
            me.filoghost.holographicdisplays.api.hologram.TouchHandler v3TouchHandler = v3TouchableLine.getTouchHandler();

            if (v3TouchHandler == null) {
                // Keep it null
            } else if (v3TouchHandler instanceof V3TouchHandlerAdapter) {
                // Adapter created from the setter method, simply unwrap it
                v2TouchHandler = ((V3TouchHandlerAdapter) v3TouchHandler).getV2TouchHandler();
            } else {
                // Adapt the new handler to the old v2 API
                v2TouchHandler = new V2TouchHandlerAdapter(v3TouchHandler);
            }
        }
        return v2TouchHandler;
    }

}
