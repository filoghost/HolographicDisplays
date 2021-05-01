/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package com.gmail.filoghost.holograms.api.internal;

import com.gmail.filoghost.holograms.api.Hologram;
import com.gmail.filoghost.holograms.api.TouchHandler;
import org.bukkit.entity.Player;

@SuppressWarnings("deprecation")
class HologramTouchHandlerAdapter implements me.filoghost.holographicdisplays.api.handler.TouchHandler {

    private final TouchHandler oldHandler;
    private final Hologram hologram;
    
    HologramTouchHandlerAdapter(Hologram hologram, TouchHandler oldHandler) {
        this.hologram = hologram;
        this.oldHandler = oldHandler;
    }
    
    @Override
    public void onTouch(Player player) {
        oldHandler.onTouch(hologram, player);
    }

}
