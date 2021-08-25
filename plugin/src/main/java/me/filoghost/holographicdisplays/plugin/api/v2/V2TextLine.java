/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.api.v2;

import com.gmail.filoghost.holographicdisplays.api.handler.TouchHandler;
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;
import me.filoghost.holographicdisplays.plugin.hologram.base.BaseTextHologramLine;

@SuppressWarnings("deprecation")
public class V2TextLine extends BaseTextHologramLine implements TextLine, V2TouchableLine {

    private final V2Hologram hologram;

    private TouchHandler touchHandler;

    public V2TextLine(V2Hologram hologram, String text) {
        super(hologram, text);
        this.hologram = hologram;
    }

    @Override
    public V2Hologram getParent() {
        return hologram;
    }

    @Override
    public boolean isAllowPlaceholders() {
        return hologram.isAllowPlaceholders();
    }

    @Override
    public void setTouchHandler(TouchHandler touchHandler) {
        this.touchHandler = touchHandler;
    }

    @Override
    public TouchHandler getTouchHandler() {
        return touchHandler;
    }

}
