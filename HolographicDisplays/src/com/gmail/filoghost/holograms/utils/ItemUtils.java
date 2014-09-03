package com.gmail.filoghost.holograms.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemUtils {
	
	public static final String ANTISTACK_LORE = ChatColor.BLACK + "" + Math.random();

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
	
	// A map with formatter materials (lowercase and without dashes) for fast access.
	private static Map<String, Material> materialMap = new HashMap<String, Material>();
	private static Pattern stripSymbolsPattern = Pattern.compile("[_ \\-]+");
	
	static {
		for (Material mat : Material.values()) {
			materialMap.put(stripSpacingChars(mat.toString()).toLowerCase(), mat);
		}
	}
	
	public static String stripSpacingChars(String input) {
		return stripSymbolsPattern.matcher(input).replaceAll("");
	}
	
	public static Material matchMaterial(String input) {
		return materialMap.get(stripSpacingChars(input).toLowerCase());
	}
	
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
