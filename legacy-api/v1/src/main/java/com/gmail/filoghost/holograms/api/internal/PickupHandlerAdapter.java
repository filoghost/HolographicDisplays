/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package com.gmail.filoghost.holograms.api.internal;

import com.gmail.filoghost.holograms.api.FloatingItem;
import com.gmail.filoghost.holograms.api.PickupHandler;
import org.bukkit.entity.Player;

@SuppressWarnings("deprecation")
class PickupHandlerAdapter implements me.filoghost.holographicdisplays.api.handler.PickupHandler {

    private final PickupHandler oldHandler;
    private final FloatingItem item;
    
    PickupHandlerAdapter(FloatingItem item, PickupHandler oldHandler) {
        this.item = item;
        this.oldHandler = oldHandler;
    }
    
    @Override
    public void onPickup(Player player) {
        oldHandler.onPickup(item, player);
    }

}
