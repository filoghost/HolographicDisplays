/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.core.api.current;

import me.filoghost.holographicdisplays.api.beta.hologram.line.ClickableHologramLine;
import me.filoghost.holographicdisplays.api.beta.hologram.line.HologramLineClickListener;
import me.filoghost.holographicdisplays.core.base.ClickCallbackProvider;
import org.bukkit.entity.Player;

interface APIClickableHologramLine extends ClickableHologramLine, APIHologramLine, ClickCallbackProvider {

    @Override
    default boolean hasClickCallback() {
        return getClickListener() != null;
    }

    @Override
    default void invokeClickCallback(Player player) {
        HologramLineClickListener clickListener = getClickListener();
        if (clickListener != null) {
            clickListener.onClick(new SimpleHologramLineClickEvent(player));
        }
    }

}
