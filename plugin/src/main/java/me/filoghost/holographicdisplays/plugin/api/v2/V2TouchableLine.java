/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.api.v2;

import com.gmail.filoghost.holographicdisplays.api.handler.TouchHandler;
import com.gmail.filoghost.holographicdisplays.api.line.TouchableLine;
import me.filoghost.holographicdisplays.plugin.hologram.base.Clickable;
import org.bukkit.entity.Player;

public interface V2TouchableLine extends TouchableLine, V2HologramLine, Clickable {

    @Override
    default boolean hasClickCallback() {
        return getTouchHandler() != null;
    }

    @Override
    default void invokeClickCallback(Player player) {
        try {
            TouchHandler touchHandler = getTouchHandler();
            if (touchHandler != null) {
                touchHandler.onTouch(player);
            }
        } catch (Throwable t) {
            logClickCallbackException(getCreatorPlugin(), player, t);
        }
    }

}
