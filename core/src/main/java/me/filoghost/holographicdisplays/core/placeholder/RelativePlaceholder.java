/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.core.placeholder;

import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashSet;

public class RelativePlaceholder implements RelativePlaceholderReplacer {
    
    private static final Collection<RelativePlaceholder> registry = new HashSet<>();
    
    // The placeholder itself, something like {player}
    private final String textPlaceholder;
    
    private final RelativePlaceholderReplacer replacer;
    
    public RelativePlaceholder(String textPlaceholder, RelativePlaceholderReplacer replacer) {
        this.textPlaceholder = textPlaceholder;
        this.replacer = replacer;
    }
    
    public String getTextPlaceholder() {
        return textPlaceholder;
    }
    
    @Override
    public String getReplacement(Player player) {
        return replacer.getReplacement(player);
    }
    
    public static void register(String textPlaceholder, RelativePlaceholderReplacer replacer) {
        for (RelativePlaceholder existingPlaceholder : registry) {
            if (existingPlaceholder.getTextPlaceholder().equals(textPlaceholder)) {
                throw new IllegalArgumentException("Relative placeholder already registered");
            }
        }
        
        registry.add(new RelativePlaceholder(textPlaceholder, replacer));
    }
    
    public static Collection<RelativePlaceholder> getRegistry() {
        return registry;
    }
    
    static {
        register("{player}", Player::getName);
        register("{displayname}", Player::getDisplayName);
    }

}
