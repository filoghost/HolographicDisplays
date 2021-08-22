/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.api.v2;

import com.gmail.filoghost.holographicdisplays.api.handler.TouchHandler;
import com.gmail.filoghost.holographicdisplays.api.line.TouchableLine;
import me.filoghost.holographicdisplays.api.hologram.line.ClickListener;
import me.filoghost.holographicdisplays.plugin.hologram.api.APIClickableLine;

@SuppressWarnings("deprecation")
abstract class V2TouchableLineAdapter extends V2HologramLineAdapter implements TouchableLine {

    private final APIClickableLine v3Line;
    private TouchHandler v2TouchHandler;

    V2TouchableLineAdapter(APIClickableLine v3Line) {
        super(v3Line);
        this.v3Line = v3Line;
    }

    public void onV3ClickListenerChange(ClickListener previous, ClickListener current) {
        if (previous != current) {
            v2TouchHandler = null; // Clear the field to force a new lazy initialization
        }
    }

    @Override
    public void setTouchHandler(TouchHandler v2TouchHandler) {
        if (v2TouchHandler == null) {
            v3Line.setClickListener(null);
        } else if (v2TouchHandler instanceof V2TouchHandlerAdapter) {
            // Adapter created from the getter method, simply unwrap it
            v3Line.setClickListener(((V2TouchHandlerAdapter) v2TouchHandler).getV3ClickListener());
        } else {
            ClickListener v3ClickListener = v3Line.getClickListener();

            // Adapt the old v2 handler to the new API, creating a new instance only if the wrapped handler changed
            if (!(v3ClickListener instanceof V3ClickListenerAdapter)
                    || ((V3ClickListenerAdapter) v3ClickListener).getV2TouchHandler() != v2TouchHandler) {
                v3Line.setClickListener(new V3ClickListenerAdapter(v2TouchHandler));
            }
        }
        this.v2TouchHandler = v2TouchHandler;
    }

    @Override
    public TouchHandler getTouchHandler() {
        // Lazy initialization
        if (v2TouchHandler == null) {
            ClickListener v3ClickListener = v3Line.getClickListener();

            if (v3ClickListener == null) {
                // Keep it null
            } else if (v3ClickListener instanceof V3ClickListenerAdapter) {
                // Adapter created from the setter method, simply unwrap it
                v2TouchHandler = ((V3ClickListenerAdapter) v3ClickListener).getV2TouchHandler();
            } else {
                // Adapt the new handler to the old v2 API
                v2TouchHandler = new V2TouchHandlerAdapter(v3ClickListener);
            }
        }
        return v2TouchHandler;
    }

}
