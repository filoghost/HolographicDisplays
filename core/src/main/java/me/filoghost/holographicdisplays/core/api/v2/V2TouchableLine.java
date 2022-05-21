/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.core.api.v2;

import com.gmail.filoghost.holographicdisplays.api.handler.TouchHandler;
import com.gmail.filoghost.holographicdisplays.api.line.TouchableLine;
import me.filoghost.holographicdisplays.core.base.ClickCallbackProvider;
import org.bukkit.entity.Player;

interface V2TouchableLine extends TouchableLine, V2HologramLine, ClickCallbackProvider {

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
