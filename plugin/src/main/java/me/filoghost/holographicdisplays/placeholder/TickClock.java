/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.placeholder;

public class TickClock {

    private long currentTick;
    
    public void incrementTick() {
        currentTick++;
    }

    public long getCurrentTick() {
        return currentTick;
    }
    
}
