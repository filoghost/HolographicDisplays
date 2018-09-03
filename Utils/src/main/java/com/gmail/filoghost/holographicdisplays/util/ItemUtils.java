/*
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package com.gmail.filoghost.holographicdisplays.util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public class ItemUtils {
	
	// This is used on hologram icons, to prevent vanilla items from merging with them.
	public static final String ANTISTACK_LORE = ChatColor.BLACK.toString() + Math.random();
	
	// A map with formatted materials (lowercase and without symbols) for fast access.
	private static final Map<String, Material> NAMES_TO_MATERIALS = new HashMap<String, Material>();
	
	// The chars that will be ignored when matching materials.
	private static final Pattern STRIP_SPACING_SYMBOLS_PATTERN = Pattern.compile("[_ \\-]+");
	
	static {
		// Add default materials.
		for (Material mat : Material.values()) {
			NAMES_TO_MATERIALS.put(stripSpacingChars(mat.toString()).toLowerCase(), mat);
		}
		
		// Only add aliases before 1.13.
		if (!NMSVersion.isGreaterEqualThan(NMSVersion.v1_13_R1)) {
			// Default material names are not intuitive and sometimes confusing.
			addAlias("iron bar",			"IRON_FENCE");
			addAlias("iron bars",			"IRON_FENCE");
			addAlias("glass pane",			"THIN_GLASS");
			addAlias("nether wart",			"NETHER_STALK");
			addAlias("nether warts",		"NETHER_STALK");
			addAlias("slab",				"STEP");
			addAlias("double slab",			"DOUBLE_STEP");
			addAlias("stone brick",			"SMOOTH_BRICK");
			addAlias("stone bricks",		"SMOOTH_BRICK");
			addAlias("stone stair",			"SMOOTH_STAIRS");
			addAlias("stone stairs",		"SMOOTH_STAIRS");
			addAlias("potato",				"POTATO_ITEM");
			addAlias("carrot",				"CARROT_ITEM");
			addAlias("brewing stand",		"BREWING_STAND_ITEM");
			addAlias("cauldron",			"CAULDRON_ITEM");
			addAlias("carrot on stick",		"CARROT_STICK");
			addAlias("carrot on a stick",	"CARROT_STICK");
			addAlias("cobblestone wall",	"COBBLE_WALL");
			addAlias("wood slab",			"WOOD_STEP");
			addAlias("double wood slab",	"WOOD_DOUBLE_STEP");
			addAlias("repeater",			"DIODE");
			addAlias("piston",				"PISTON_BASE");
			addAlias("sticky piston",		"PISTON_STICKY_BASE");
			addAlias("flower pot",			"FLOWER_POT_ITEM");
			addAlias("wood showel",			"WOOD_SPADE");
			addAlias("stone showel",		"STONE_SPADE");
			addAlias("gold showel",			"GOLD_SPADE");
			addAlias("iron showel",			"IRON_SPADE");
			addAlias("diamond showel",		"DIAMOND_SPADE");
			addAlias("steak",				"COOKED_BEEF");
			addAlias("cooked porkchop",		"GRILLED_PORK");
			addAlias("raw porkchop",		"PORK");
			addAlias("hardened clay",		"HARD_CLAY");
			addAlias("huge brown mushroom",	"HUGE_MUSHROOM_1");
			addAlias("huge red mushroom",	"HUGE_MUSHROOM_2");
			addAlias("mycelium",			"MYCEL");
			addAlias("poppy",				"RED_ROSE");
			addAlias("comparator",			"REDSTONE_COMPARATOR");
			addAlias("skull",				"SKULL_ITEM");
			addAlias("head",				"SKULL_ITEM");
			addAlias("redstone torch",		"REDSTONE_TORCH_ON");
			addAlias("redstone lamp",		"REDSTONE_LAMP_OFF");
			addAlias("glistering melon",	"SPECKLED_MELON");
			addAlias("gunpowder",			"SULPHUR");
			addAlias("lilypad",				"WATER_LILY");
			addAlias("command block",		"COMMAND");
			addAlias("dye",					"INK_SACK");
		}
	}
	
	private static void addAlias(String alias, String material) {
		try {
			NAMES_TO_MATERIALS.put(stripSpacingChars(alias).toLowerCase(), Material.valueOf(material));
		} catch (IllegalArgumentException e) {
			// Not found, do nothing.
		}
	}
	
	public static String stripSpacingChars(String input) {
		return STRIP_SPACING_SYMBOLS_PATTERN.matcher(input).replaceAll("");
	}
	
	@SuppressWarnings("deprecation")
	public static Material matchMaterial(String input) {
		if (!NMSVersion.isGreaterEqualThan(NMSVersion.v1_13_R1)) {
			// Before 1.13, allow IDs as materials.
			try {
				return Material.getMaterial(Integer.parseInt(input));
			} catch (NumberFormatException e) {
				// Not a number, ignore and go on.
			}
		}

		return NAMES_TO_MATERIALS.get(stripSpacingChars(input).toLowerCase());
	}

}
