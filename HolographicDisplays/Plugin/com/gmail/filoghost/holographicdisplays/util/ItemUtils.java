package com.gmail.filoghost.holographicdisplays.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;
import org.bukkit.Material;

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
	
	public static Material matchMaterial(String input) {
		return materialMap.get(stripSpacingChars(input).toLowerCase());
	}
	
	
	// Blocks are smalled than items.
	@SuppressWarnings("deprecation")
	public static boolean appearsAsBlock(Material mat) {
		switch (mat.getId()) {
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
			case 7:
			case 12:
			case 13:
			case 14:
			case 15:
			case 16:
			case 17:
			case 18:
			case 19:
			case 20:
			case 21:
			case 22:
			case 23:
			case 24:
			case 25:
			case 26:
			case 29:
			case 33:
			case 34:
			case 35:
			case 36:
			case 41:
			case 42:
			case 43:
			case 44:
			case 45:
			case 46:
			case 47:
			case 48:
			case 49:
			case 52:
			case 53:
			case 54:
			case 55:
			case 56:
			case 57:
			case 58:
			case 59:
			case 63:
			case 64:
			case 67:
			case 68:
			case 70:
			case 71:
			case 72:
			case 73:
			case 74:
			case 75:
			case 77:
			case 78:
			case 79:
			case 80:
			case 81:
			case 82:
			case 83:
			case 84:
			case 85:
			case 86:
			case 87:
			case 88:
			case 89:
			case 90:
			case 91:
			case 92:
			case 93:
			case 94:
			case 95:
			case 96:
			case 97:
			case 98:
			case 99:
			case 100:
			case 103:
			case 104:
			case 105:
			case 107:
			case 108:
			case 109:
			case 110:
			case 112:
			case 113:
			case 114:
			case 115:
			case 116:
			case 117:
			case 118:
			case 120:
			case 121:
			case 122:
			case 123:
			case 124:
			case 125:
			case 126:
			case 128:
			case 129:
			case 130:
			case 132:
			case 133:
			case 134:
			case 135:
			case 136:
			case 137:
			case 138:
			case 139:
			case 140:
			case 143:
			case 144:
			case 145:
			case 146:
			case 147:
			case 148:
			case 149:
			case 150:
			case 151:
			case 152:
			case 153:
			case 155:
			case 156:
			case 157:
			case 158:
			case 159:
			case 161:
			case 162:
			case 163:
			case 164:
			case 165:
			case 167:
			case 168:
			case 169:
			case 170:
			case 171:
			case 172:
			case 173:
			case 174:
			case 176:
			case 177:
			case 178:
			case 179:
			case 180:
			case 181:
			case 182:
			case 183:
			case 184:
			case 185:
			case 186:
			case 187:
			case 188:
			case 189:
			case 190:
			case 191:
			case 192:
			case 193:
			case 194:
			case 195:
			case 196:
			case 197:
				return true;
			default:
				return false;
		}
	}

}
