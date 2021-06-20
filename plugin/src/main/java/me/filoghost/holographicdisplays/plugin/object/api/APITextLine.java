/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.object.api;

import me.filoghost.holographicdisplays.api.handler.TouchHandler;
import me.filoghost.holographicdisplays.api.line.TextLine;
import me.filoghost.holographicdisplays.plugin.api.v2.V2TextLineAdapter;
import me.filoghost.holographicdisplays.plugin.object.base.BaseTextLine;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class APITextLine extends BaseTextLine implements TextLine, APITouchableLine {

    private final APIHologram parent;
    private final V2TextLineAdapter v2Adapter;

    public APITextLine(APIHologram parent, String text) {
        super(parent, text);
        this.parent = parent;
        this.v2Adapter = new V2TextLineAdapter(this);
    }

    @Override
    public @NotNull APIHologram getParent() {
        return parent;
    }

    @Override
    public boolean isAllowPlaceholders() {
        return parent.isAllowPlaceholders();
    }

    @Override
    public void setTouchHandler(@Nullable TouchHandler touchHandler) {
        v2Adapter.onNewTouchHandlerChange(getTouchHandler(), touchHandler);
        super.setTouchHandler(touchHandler);
    }
    
    @Override
    public V2TextLineAdapter getV2Adapter() {
        return v2Adapter;
    }

}
