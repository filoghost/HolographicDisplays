/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package com.gmail.filoghost.holographicdisplays.api.line;

import org.bukkit.inventory.ItemStack;

/**
 * @deprecated Please use the new API!
 */
@Deprecated
public interface ItemLine extends CollectableLine, TouchableLine {

    @Deprecated
    public ItemStack getItemStack();

    @Deprecated
    public void setItemStack(ItemStack itemStack);
    
}
