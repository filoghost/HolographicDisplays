package com.gmail.filoghost.holographicdisplays.nms.interfaces.entity;

import org.bukkit.inventory.ItemStack;

public interface NMSItem extends NMSEntityBase, NMSCanMount {
	
	// Sets the bukkit ItemStack for this item.
	public void setItemStackNMS(ItemStack stack);
	
	// Sets if this item can be picked up by players.
	public void allowPickup(boolean pickup);
	
	// The raw NMS ItemStack object.
	public Object getRawItemStack();
	
}
