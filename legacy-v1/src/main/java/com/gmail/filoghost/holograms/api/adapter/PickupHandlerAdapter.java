/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package com.gmail.filoghost.holograms.api.adapter;

import com.gmail.filoghost.holograms.api.FloatingItem;
import com.gmail.filoghost.holograms.api.PickupHandler;
import org.bukkit.entity.Player;

@SuppressWarnings("deprecation")
public class PickupHandlerAdapter implements me.filoghost.holographicdisplays.api.handler.PickupHandler {

    public PickupHandler oldHandler;
    private FloatingItem item;
    
    public PickupHandlerAdapter(FloatingItem item, PickupHandler oldPickupHandler) {
        this.item = item;
        this.oldHandler = oldPickupHandler;
    }
    
    @Override
    public void onPickup(Player player) {
        oldHandler.onPickup(item, player);
    }

}
