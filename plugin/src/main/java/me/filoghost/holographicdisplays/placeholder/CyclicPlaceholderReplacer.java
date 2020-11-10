/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.placeholder;

import me.filoghost.holographicdisplays.api.placeholder.PlaceholderReplacer;

public class CyclicPlaceholderReplacer implements PlaceholderReplacer {

    String[] frames;
    private int index;
    
    public CyclicPlaceholderReplacer(String[] frames) {
        this.frames = frames;
        index = 0;
    }

    @Override
    public String update() {
        String result = frames[index];
        
        index++;
        if (index >= frames.length) {
            index = 0;
        }
        
        return result;
    }

}
