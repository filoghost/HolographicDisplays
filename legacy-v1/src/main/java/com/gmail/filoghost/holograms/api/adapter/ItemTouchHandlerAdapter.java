/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package com.gmail.filoghost.holograms.api.adapter;

import com.gmail.filoghost.holograms.api.FloatingItem;
import com.gmail.filoghost.holograms.api.ItemTouchHandler;
import me.filoghost.holographicdisplays.api.handler.TouchHandler;
import org.bukkit.entity.Player;

@SuppressWarnings("deprecation")
public class ItemTouchHandlerAdapter implements TouchHandler {

    protected ItemTouchHandler oldHandler;
    private FloatingItem item;
    
    public ItemTouchHandlerAdapter(FloatingItem item, ItemTouchHandler oldHandler) {
        this.item = item;
        this.oldHandler = oldHandler;
    }

    @Override
    public void onTouch(Player player) {
        oldHandler.onTouch(item, player);
    }

}
