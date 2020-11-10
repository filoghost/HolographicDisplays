/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package com.gmail.filoghost.holograms.api.adapter;

import com.gmail.filoghost.holograms.api.Hologram;
import com.gmail.filoghost.holograms.api.TouchHandler;
import org.bukkit.entity.Player;

@SuppressWarnings("deprecation")
public class HologramTouchHandlerAdapter implements me.filoghost.holographicdisplays.api.handler.TouchHandler {

    protected TouchHandler oldHandler;
    private Hologram hologram;
    
    public HologramTouchHandlerAdapter(Hologram hologram, TouchHandler oldHandler) {
        this.hologram = hologram;
        this.oldHandler = oldHandler;
    }
    
    @Override
    public void onTouch(Player player) {
        oldHandler.onTouch(hologram, player);
    }

}
