package com.gmail.filoghost.holographicdisplays.api.line;

import org.bukkit.inventory.ItemStack;

public interface ItemLine extends CollectableLine, TouchableLine {

	public ItemStack getItemStack();

	public void setItemStack(ItemStack itemStack);
	
}
