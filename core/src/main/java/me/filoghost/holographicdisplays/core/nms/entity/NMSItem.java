/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.core.nms.entity;

import org.bukkit.inventory.ItemStack;

public interface NMSItem extends NMSEntity {
    
    // Sets the bukkit ItemStack for this item.
    void setItemStackNMS(ItemStack stack);

    // The raw NMS ItemStack object.
    Object getRawItemStack();
    
}
