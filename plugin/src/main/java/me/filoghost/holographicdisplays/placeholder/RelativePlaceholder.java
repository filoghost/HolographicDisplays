/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.placeholder;

import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashSet;

public abstract class RelativePlaceholder {
    
    private static final Collection<RelativePlaceholder> registry = new HashSet<>();
    
    // The placeholder itself, something like {player}.
    private final String textPlaceholder;
    
    public RelativePlaceholder(String textPlaceholder) {
        this.textPlaceholder = textPlaceholder;
    }
    
    public String getTextPlaceholder() {
        return textPlaceholder;
    }
    
    public abstract String getReplacement(Player player);
    
    public static void register(RelativePlaceholder relativePlaceholder) {
        for (RelativePlaceholder existingPlaceholder : registry) {
            if (existingPlaceholder.getTextPlaceholder().equals(relativePlaceholder.getTextPlaceholder())) {
                throw new IllegalArgumentException("Relative placeholder already registered");
            }
        }
        
        registry.add(relativePlaceholder);
    }
    
    public static Collection<RelativePlaceholder> getRegistry() {
        return registry;
    }
    
    static {
        register(new RelativePlaceholder("{player}") {
            
            @Override
            public String getReplacement(Player player) {
                return player.getName();
            }
        });
        
        register(new RelativePlaceholder("{displayname}") {
            
            @Override
            public String getReplacement(Player player) {
                return player.getDisplayName();
            }
        });
    }

}
