package com.gmail.filoghost.holograms.utils;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemUtils {
	
	public static ItemStack getStone(String title, List<String> lore, ChatColor defaultLoreColor) {
		return getItem(Material.STONE, title, lore, defaultLoreColor);
	}
	
	public static ItemStack getItem(Material material, String title, List<String> lore, ChatColor defaultLoreColor) {
		ItemStack item = new ItemStack(material);
		ItemMeta meta = item.getItemMeta();
		if (title != null) {
			meta.setDisplayName(title);
		}
		
		if (lore != null) {
			for (int i = 0; i < lore.size(); i++) {
				String line = lore.get(i);
				if (!line.startsWith("§")) {
					line = defaultLoreColor + line;
					lore.set(i, line);
				}

			}
			meta.setLore(lore);
		
		}
		item.setItemMeta(meta);
		return item;
	}

}
