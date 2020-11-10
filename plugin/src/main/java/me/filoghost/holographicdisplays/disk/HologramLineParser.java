/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.disk;

import me.filoghost.holographicdisplays.exception.HologramLineParseException;
import me.filoghost.holographicdisplays.nbt.parser.MojangsonParseException;
import me.filoghost.holographicdisplays.nbt.parser.MojangsonParser;
import me.filoghost.holographicdisplays.object.NamedHologram;
import me.filoghost.holographicdisplays.object.line.CraftHologramLine;
import me.filoghost.holographicdisplays.object.line.CraftItemLine;
import me.filoghost.holographicdisplays.object.line.CraftTextLine;
import me.filoghost.holographicdisplays.util.ItemUtils;
import me.filoghost.holographicdisplays.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class HologramLineParser {
    
    
    public static CraftHologramLine parseLine(NamedHologram hologram, String serializedLine, boolean checkMaterialValidity) throws HologramLineParseException {
        CraftHologramLine hologramLine;
        
        if (serializedLine.toLowerCase().startsWith("icon:")) {
            String serializedIcon = serializedLine.substring("icon:".length(), serializedLine.length());
            ItemStack icon = parseItemStack(serializedIcon, checkMaterialValidity);
            hologramLine = new CraftItemLine(hologram, icon);
            
        } else {
            if (serializedLine.trim().equalsIgnoreCase("{empty}")) {
                hologramLine = new CraftTextLine(hologram, "");
            } else {
                hologramLine = new CraftTextLine(hologram, StringConverter.toReadableFormat(serializedLine));
            }
        }
        
        hologramLine.setSerializedConfigValue(serializedLine);
        return hologramLine;
    }
    
    
    @SuppressWarnings("deprecation")
    private static ItemStack parseItemStack(String serializedItem, boolean checkMaterialValidity) throws HologramLineParseException {
        serializedItem = serializedItem.trim();
        
        // Parse json
        int nbtStart = serializedItem.indexOf('{');
        int nbtEnd = serializedItem.lastIndexOf('}');
        String nbtString = null;
        
        String basicItemData;
        
        if (nbtStart > 0 && nbtEnd > 0 && nbtEnd > nbtStart) {
            nbtString = serializedItem.substring(nbtStart, nbtEnd + 1);
            basicItemData = serializedItem.substring(0, nbtStart) + serializedItem.substring(nbtEnd + 1, serializedItem.length());
        } else {
            basicItemData = serializedItem;
        }
        
        basicItemData = ItemUtils.stripSpacingChars(basicItemData);

        String materialName;
        short dataValue = 0;
        
        if (basicItemData.contains(":")) {
            String[] materialAndDataValue = basicItemData.split(":", -1);
            try {
                dataValue = (short) Integer.parseInt(materialAndDataValue[1]);
            } catch (NumberFormatException e) {
                throw new HologramLineParseException("Data value \"" + materialAndDataValue[1] + "\" is not a valid number.");
            }
            materialName = materialAndDataValue[0];
        } else {
            materialName = basicItemData;
        }
        
        Material material = ItemUtils.matchMaterial(materialName);
        if (material == null) {
            if (checkMaterialValidity) {
                throw new HologramLineParseException("\"" + materialName + "\" is not a valid material.");
            }
            material = Material.BEDROCK;
        }
        
        ItemStack itemStack = new ItemStack(material, 1, dataValue);
        
        if (nbtString != null) {
            try {
                // Check NBT syntax validity before applying it.
                MojangsonParser.parse(nbtString);
                Bukkit.getUnsafe().modifyItemStack(itemStack, nbtString);
            } catch (MojangsonParseException e) {
                throw new HologramLineParseException("Invalid NBT data, " + Utils.uncapitalize(ChatColor.stripColor(e.getMessage())));
            } catch (Throwable t) {
                throw new HologramLineParseException("Unexpected exception while parsing NBT data.", t);
            }
        }
        
        return itemStack;
    }

}
