/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.object.line;

/**
 *  When spawning a hologram at a location, the top part of the first line should be exactly on that location.
 *  The second line is below the first, and so on.
 */
public class Offsets {
    
    // A single armor stand.
    public static final double ARMOR_STAND_ALONE = -0.29;
        
    // An armor stand holding an item.
    public static final double ARMOR_STAND_WITH_ITEM = 0;
    
    // An armor stand holding a slime.
    public static final double ARMOR_STAND_WITH_SLIME = 0;    

}
