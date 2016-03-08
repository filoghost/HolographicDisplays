package com.gmail.filoghost.holographicdisplays.api.line;

import org.bukkit.inventory.ItemStack;

public interface ItemLine extends CollectableLine, TouchableLine {

	/**
	 * Returns the ItemStack of this ItemLine.
	 * 
	 * @return the ItemStack if this ItemLine.
	 */
	public ItemStack getItemStack();

	/**
	 * Sets the ItemStack for this ItemLine.
	 * 
	 * @param itemStack the new item, should not be null.
	 */
	public void setItemStack(ItemStack itemStack);
	
}
