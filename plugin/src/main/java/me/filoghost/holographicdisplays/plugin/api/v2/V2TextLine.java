/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.api.v2;

import com.gmail.filoghost.holographicdisplays.api.handler.TouchHandler;
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;
import me.filoghost.holographicdisplays.api.hologram.line.ClickListener;
import me.filoghost.holographicdisplays.plugin.hologram.base.BaseTextHologramLine;

@SuppressWarnings("deprecation")
public class V2TextLine extends BaseTextHologramLine implements TextLine, V2HologramLine {

    private final V2Hologram parent;

    public V2TextLine(V2Hologram parent, String text) {
        super(parent, text);
        this.parent = parent;
    }

    @Override
    public V2Hologram getParent() {
        return parent;
    }

    @Override
    public boolean isAllowPlaceholders() {
        return parent.isAllowPlaceholders();
    }

    @Override
    public void setTouchHandler(TouchHandler touchHandler) {
        super.setClickListener(new V3ClickListenerAdapter(touchHandler));
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

}
