package com.gmail.filoghost.holographicdisplays.api.placeholder;

import org.bukkit.entity.Player;

public interface PlayerRelativePlaceholderReplacer {

    /**
     * Called to update a placeholder's replacement.
     * @param player the player the placeholder is being sent to
     * @return the replacement
     */
    String update(Player player);
}
