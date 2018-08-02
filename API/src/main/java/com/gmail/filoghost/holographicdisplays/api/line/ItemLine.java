package com.gmail.filoghost.holographicdisplays.api.line;

import org.bukkit.inventory.ItemStack;

public interface ItemLine extends CollectableLine, TouchableLine {

	/**
	 * Returns the ItemStack of this ItemLine.
	 *
	 * @return the ItemStack if this ItemLine.
	 */
	ItemStack getItemStack();

	/**
	 * Sets the ItemStack for this ItemLine.
	 *
	 * @param itemStack the new item, should not be null.
	 */
	void setItemStack(ItemStack itemStack);

}
