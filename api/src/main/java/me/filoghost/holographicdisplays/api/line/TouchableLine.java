/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.api.line;

import me.filoghost.holographicdisplays.api.handler.TouchHandler;

/**
 * A line of a Hologram that can be touched (right click).
 */
public interface TouchableLine extends HologramLine {

    /**
     * Sets the TouchHandler for this line.
     * 
     * @param touchHandler the new TouchHandler, can be null.
     */
    public void setTouchHandler(TouchHandler touchHandler);
    
    /**
     * Returns the current TouchHandler of this line.
     * 
     * @return the current TouchHandler, can be null.
     */
    public TouchHandler getTouchHandler();
    
}
