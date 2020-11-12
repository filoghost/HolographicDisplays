/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.placeholder;

import me.filoghost.holographicdisplays.api.placeholder.PlaceholderReplacer;
import org.bukkit.plugin.Plugin;

public class Placeholder {
    
    // The plugin that owns this placeholder.
    private final Plugin owner;
    
    // The placeholder itself, something like {onlinePlayers}. Case sensitive!
    private final String textPlaceholder;
    
    // How many tenths of second between each refresh.
    private int tenthsToRefresh;
    
    // This is the current replacement for this placeholder.
    private String currentReplacement;
    
    private PlaceholderReplacer replacer;
    
    public Placeholder(Plugin owner, String textPlaceholder, double refreshRate, PlaceholderReplacer replacer) {
        this.owner = owner;
        this.textPlaceholder = textPlaceholder;
        this.tenthsToRefresh = refreshRate <= 0.1 ? 1 : (int) (refreshRate * 10.0);
        this.replacer = replacer;
        this.currentReplacement = "";
    }
    
    public Plugin getOwner() {
        return owner;
    }
    
    public int getTenthsToRefresh() {
        return tenthsToRefresh;
    }

    public String getTextPlaceholder() {
        return textPlaceholder;
    }
    
    public String getCurrentReplacement() {
        return currentReplacement;
    }
    
    public void setCurrentReplacement(String replacement) {
        this.currentReplacement = replacement != null ? replacement : "null";
    }

    public void update() {
        setCurrentReplacement(replacer.update());
    }
    
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        
        if (obj instanceof Placeholder) {
            return ((Placeholder) obj).textPlaceholder.equals(this.textPlaceholder);
        }
        
        return false;
    }
    
    
    @Override
    public int hashCode() {
        return textPlaceholder.hashCode();
    }
    
    
}
