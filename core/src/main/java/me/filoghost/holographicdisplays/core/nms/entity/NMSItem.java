/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.core.nms.entity;

import org.bukkit.inventory.ItemStack;

public interface NMSItem extends NMSEntity {
    
    void setItemStackNMS(ItemStack stack);

    /**
     * Returns the item stack NMS object.
     */
    Object getRawItemStack();
    
}
