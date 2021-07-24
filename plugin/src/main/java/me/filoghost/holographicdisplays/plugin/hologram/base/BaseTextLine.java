/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.hologram.base;

import me.filoghost.holographicdisplays.common.hologram.StandardTextLine;
import me.filoghost.holographicdisplays.plugin.hologram.tracking.LineTrackerManager;
import me.filoghost.holographicdisplays.plugin.hologram.tracking.TextLineTracker;
import org.jetbrains.annotations.Nullable;

public abstract class BaseTextLine extends BaseTouchableLine implements StandardTextLine {

    private String text;

    public BaseTextLine(BaseHologram<?> hologram, String text) {
        super(hologram);
        setText(text);
    }

    @Override
    protected TextLineTracker createTracker(LineTrackerManager trackerManager) {
        return trackerManager.startTracking(this);
    }

    @Override
    public @Nullable String getText() {
        return text;
    }

    public void setText(@Nullable String text) {
        this.text = text;
        setChanged();
    }

    @Override
    public double getHeight() {
        return 0.23;
    }

    @Override
    public String toString() {
        return "TextLine [text=" + text + "]";
    }

}
