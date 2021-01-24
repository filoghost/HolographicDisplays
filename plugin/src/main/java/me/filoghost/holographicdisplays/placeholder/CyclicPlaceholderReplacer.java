/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.placeholder;

import me.filoghost.holographicdisplays.api.placeholder.PlaceholderReplacer;

import java.util.List;

public class CyclicPlaceholderReplacer implements PlaceholderReplacer {

    List<String> frames;
    private int index;
    
    public CyclicPlaceholderReplacer(List<String> frames) {
        this.frames = frames;
        index = 0;
    }

    @Override
    public String update() {
        String result = frames.get(index);
        
        index++;
        if (index >= frames.size()) {
            index = 0;
        }
        
        return result;
    }

}
