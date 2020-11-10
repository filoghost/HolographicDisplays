/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.interfaces.entity;

import org.bukkit.inventory.ItemStack;

public interface NMSItem extends NMSEntityBase, NMSCanMount {
    
    // Sets the location through NMS.
    public void setLocationNMS(double x, double y, double z);
    
    // Sets the bukkit ItemStack for this item.
    public void setItemStackNMS(ItemStack stack);
    
    // Sets if this item can be picked up by players.
    public void allowPickup(boolean pickup);
    
    // The raw NMS ItemStack object.
    public Object getRawItemStack();
    
}
