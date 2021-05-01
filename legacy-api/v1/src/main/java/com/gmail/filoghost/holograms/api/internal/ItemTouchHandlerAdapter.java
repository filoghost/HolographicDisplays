/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package com.gmail.filoghost.holograms.api.internal;

import com.gmail.filoghost.holograms.api.FloatingItem;
import com.gmail.filoghost.holograms.api.ItemTouchHandler;
import org.bukkit.entity.Player;

@SuppressWarnings("deprecation")
class ItemTouchHandlerAdapter implements me.filoghost.holographicdisplays.api.handler.TouchHandler {

    private final ItemTouchHandler oldHandler;
    private final FloatingItem item;
    
    ItemTouchHandlerAdapter(FloatingItem item, ItemTouchHandler oldHandler) {
        this.item = item;
        this.oldHandler = oldHandler;
    }

    @Override
    public void onTouch(Player player) {
        oldHandler.onTouch(item, player);
    }

}
