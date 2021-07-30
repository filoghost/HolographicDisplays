/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.hologram.api;

import me.filoghost.holographicdisplays.api.hologram.ClickListener;
import me.filoghost.holographicdisplays.api.hologram.TextLine;
import me.filoghost.holographicdisplays.plugin.api.v2.V2TextLineAdapter;
import me.filoghost.holographicdisplays.plugin.hologram.base.BaseTextLine;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class APITextLine extends BaseTextLine implements TextLine, APIClickableLine {

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
    public void setClickListener(@Nullable ClickListener clickListener) {
        ClickListener oldClickListener = getClickListener();
        super.setClickListener(clickListener);
        v2Adapter.onV3ClickListenerChange(oldClickListener, clickListener);
    }

    @Override
    public V2TextLineAdapter getV2Adapter() {
        return v2Adapter;
    }

}
