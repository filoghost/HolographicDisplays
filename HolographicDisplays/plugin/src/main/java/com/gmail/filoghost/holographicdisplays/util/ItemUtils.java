package com.gmail.filoghost.holographicdisplays.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import com.gmail.filoghost.holographicdisplays.commands.CommandValidator;

public class ItemUtils {
	
	// This is used on hologram icons, to prevent vanilla items from merging with them.
	public static final String ANTISTACK_LORE = ChatColor.BLACK.toString() + Math.random();
	
	// A map with formatter materials (lowercase and without dashes) for fast access.
	private static Map<String, Material> materialMap = new HashMap<String, Material>();
	
	private static Pattern stripSpacingSymbolsPattern = Pattern.compile("[_ \\-]+");
	
	static {
		// Default material names are ugly.
		Map<String, Material> tempMap = Utils.newMap();
			
		tempMap.put("iron bar",				Material.IRON_FENCE);
		tempMap.put("iron bars",			Material.IRON_FENCE);
		tempMap.put("glass pane",			Material.THIN_GLASS);
		tempMap.put("nether wart",			Material.NETHER_STALK);
		tempMap.put("nether warts",			Material.NETHER_STALK);
		tempMap.put("slab",					Material.STEP);
		tempMap.put("double slab",			Material.DOUBLE_STEP);
		tempMap.put("stone brick",			Material.SMOOTH_BRICK);
		tempMap.put("stone bricks",			Material.SMOOTH_BRICK);
		tempMap.put("stone stair",			Material.SMOOTH_STAIRS);
		tempMap.put("stone stairs",			Material.SMOOTH_STAIRS);
		tempMap.put("potato",				Material.POTATO_ITEM);
		tempMap.put("carrot",				Material.CARROT_ITEM);
		tempMap.put("brewing stand",		Material.BREWING_STAND_ITEM);
		tempMap.put("cauldron",				Material.CAULDRON_ITEM);
		tempMap.put("carrot on stick",		Material.CARROT_STICK);
		tempMap.put("carrot on a stick",	Material.CARROT_STICK);
		tempMap.put("cobblestone wall",		Material.COBBLE_WALL);
		tempMap.put("wood slab",			Material.WOOD_STEP);
		tempMap.put("double wood slab",		Material.WOOD_DOUBLE_STEP);
		tempMap.put("repeater",				Material.DIODE);
		tempMap.put("piston",				Material.PISTON_BASE);
		tempMap.put("sticky piston",		Material.PISTON_STICKY_BASE);
		tempMap.put("flower pot",			Material.FLOWER_POT_ITEM);
		tempMap.put("wood showel",			Material.WOOD_SPADE);
		tempMap.put("stone showel",			Material.STONE_SPADE);
		tempMap.put("gold showel",			Material.GOLD_SPADE);
		tempMap.put("iron showel",			Material.IRON_SPADE);
		tempMap.put("diamond showel",		Material.DIAMOND_SPADE);
		tempMap.put("steak",				Material.COOKED_BEEF);
		tempMap.put("cooked porkchop",		Material.GRILLED_PORK);
		tempMap.put("raw porkchop",			Material.PORK);
		tempMap.put("hardened clay",		Material.HARD_CLAY);
		tempMap.put("huge brown mushroom",	Material.HUGE_MUSHROOM_1);
		tempMap.put("huge red mushroom",	Material.HUGE_MUSHROOM_2);
		tempMap.put("mycelium",				Material.MYCEL);
		tempMap.put("poppy",				Material.RED_ROSE);
		tempMap.put("comparator",			Material.REDSTONE_COMPARATOR);
		tempMap.put("skull",				Material.SKULL_ITEM);
		tempMap.put("head",					Material.SKULL_ITEM);
		tempMap.put("redstone torch",		Material.REDSTONE_TORCH_ON);
		tempMap.put("redstone lamp",		Material.REDSTONE_LAMP_OFF);
		tempMap.put("glistering melon",		Material.SPECKLED_MELON);
		tempMap.put("gunpowder",			Material.SULPHUR);
		tempMap.put("lilypad",				Material.WATER_LILY);
		tempMap.put("command block",		Material.COMMAND);
		tempMap.put("dye",					Material.INK_SACK);
		
		for (Entry<String, Material> tempEntry : tempMap.entrySet()) {
			materialMap.put(stripSpacingChars(tempEntry.getKey()).toLowerCase(), tempEntry.getValue());
		}
		
		for (Material mat : Material.values()) {
			materialMap.put(stripSpacingChars(mat.toString()).toLowerCase(), mat);
		}
	}
	
	public static String stripSpacingChars(String input) {
		return stripSpacingSymbolsPattern.matcher(input).replaceAll("");
	}
	
	@SuppressWarnings("deprecation")
	public static Material matchMaterial(String input) {
		if (CommandValidator.isInteger(input)) {
			return Material.getMaterial(Integer.parseInt(input));
		}
		return materialMap.get(stripSpacingChars(input).toLowerCase());
	}

}
