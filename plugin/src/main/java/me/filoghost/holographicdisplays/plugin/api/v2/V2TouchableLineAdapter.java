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

    private final APITouchableLine newTouchableLine;
    private TouchHandler v2TouchHandler;

    V2TouchableLineAdapter(APITouchableLine newTouchableLine) {
        super(newTouchableLine);
        this.newTouchableLine = newTouchableLine;
    }

    public void onNewTouchHandlerChange(
            me.filoghost.holographicdisplays.api.handler.TouchHandler previous,
            me.filoghost.holographicdisplays.api.handler.TouchHandler current) {
        if (previous != current) {
            v2TouchHandler = null; // Clear the field to force a new lazy initialization
        }
    }

    @Override
    public void setTouchHandler(TouchHandler v2TouchHandler) {
        if (v2TouchHandler == null) {
            newTouchableLine.setTouchHandler(null);
        } else if (v2TouchHandler instanceof V2TouchHandlerAdapter) {
            // Adapter created from the getTouchHandler() method, simply unwrap it
            newTouchableLine.setTouchHandler(((V2TouchHandlerAdapter) v2TouchHandler).getNewTouchHandler());
        } else {
            me.filoghost.holographicdisplays.api.handler.TouchHandler newTouchHandler = newTouchableLine.getTouchHandler();

            // Adapt the old v2 handler to the new API, creating a new instance only if the wrapped handler changed
            if (!(newTouchHandler instanceof NewTouchHandlerAdapter)
                    || ((NewTouchHandlerAdapter) newTouchHandler).getV2TouchHandler() != v2TouchHandler) {
                newTouchableLine.setTouchHandler(new NewTouchHandlerAdapter(v2TouchHandler));
            }
        }
        this.v2TouchHandler = v2TouchHandler;
    }

    @Override
    public TouchHandler getTouchHandler() {
        // Lazy initialization
        if (v2TouchHandler == null) {
            me.filoghost.holographicdisplays.api.handler.TouchHandler newTouchHandler = newTouchableLine.getTouchHandler();
            
            if (newTouchHandler == null) {
                // Keep it null
            } else if (newTouchHandler instanceof NewTouchHandlerAdapter) {
                // Adapter created from the setTouchHandler() method, simply unwrap it
                v2TouchHandler = ((NewTouchHandlerAdapter) newTouchHandler).getV2TouchHandler();
            } else {
                // Adapt the new handler to the old v2 API
                v2TouchHandler = new V2TouchHandlerAdapter(newTouchHandler);
            }
        }
        return v2TouchHandler;
    }

}
