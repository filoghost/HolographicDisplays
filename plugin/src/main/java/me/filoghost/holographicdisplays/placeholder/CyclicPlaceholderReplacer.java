/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.placeholder;

import me.filoghost.holographicdisplays.api.placeholder.PlaceholderReplacer;

import java.util.List;

public class CyclicPlaceholderReplacer implements PlaceholderReplacer {

    private final List<String> frames;
    private int currentIndex;
    
    public CyclicPlaceholderReplacer(List<String> frames) {
        this.frames = frames;
        currentIndex = 0;
    }

    @Override
    public String update() {
        String result = frames.get(currentIndex);
        
        currentIndex++;
        if (currentIndex >= frames.size()) {
            currentIndex = 0;
        }
        
        return result;
    }

}
