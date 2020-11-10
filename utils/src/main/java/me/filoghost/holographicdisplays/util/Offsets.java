/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.util;

/**
 *  When spawning a hologram at a location, the top part of the first line should be exactly on that location.
 *  The second line is below the first, and so on.
 */
public class Offsets {
    
    public static final double
        
        // A single armor stand.
        ARMOR_STAND_ALONE = -0.29,
        
        // An armor stand holding an item.
        ARMOR_STAND_WITH_ITEM = 0,
    
        // An armor stand holding a slime.
        ARMOR_STAND_WITH_SLIME = 0;    

}
