/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.disk;

import me.filoghost.fcommons.MaterialsHelper;
import me.filoghost.fcommons.Strings;
import me.filoghost.holographicdisplays.nbt.parser.MojangsonParseException;
import me.filoghost.holographicdisplays.nbt.parser.MojangsonParser;
import me.filoghost.holographicdisplays.object.InternalHologram;
import me.filoghost.holographicdisplays.object.line.HologramLineImpl;
import me.filoghost.holographicdisplays.object.line.ItemLineImpl;
import me.filoghost.holographicdisplays.object.line.TextLineImpl;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class HologramLineParser {

    private static final String ICON_PREFIX = "icon:";

    public static HologramLineImpl parseLine(InternalHologram hologram, String serializedLine, boolean checkMaterialValidity) throws HologramLoadException {
        HologramLineImpl hologramLine;
        
        if (serializedLine.toLowerCase().startsWith(ICON_PREFIX)) {
            String serializedIcon = serializedLine.substring(ICON_PREFIX.length());
            ItemStack icon = parseItemStack(serializedIcon, checkMaterialValidity);
            hologramLine = new ItemLineImpl(hologram, icon);
            
        } else {
            if (serializedLine.trim().equalsIgnoreCase("{empty}")) {
                hologramLine = new TextLineImpl(hologram, "");
            } else {
                hologramLine = new TextLineImpl(hologram, StringConverter.toReadableFormat(serializedLine));
            }
        }
        
        hologramLine.setSerializedConfigValue(serializedLine);
        return hologramLine;
    }
    
    
    @SuppressWarnings("deprecation")
    private static ItemStack parseItemStack(String serializedItem, boolean checkMaterialValidity) throws HologramLoadException {
        serializedItem = serializedItem.trim();
        
        // Parse json
        int nbtStart = serializedItem.indexOf('{');
        int nbtEnd = serializedItem.lastIndexOf('}');
        String nbtString = null;
        
        String basicItemData;
        
        if (nbtStart > 0 && nbtEnd > 0 && nbtEnd > nbtStart) {
            nbtString = serializedItem.substring(nbtStart, nbtEnd + 1);
            basicItemData = serializedItem.substring(0, nbtStart) + serializedItem.substring(nbtEnd + 1);
        } else {
            basicItemData = serializedItem;
        }
        
        basicItemData = Strings.stripChars(basicItemData, ' ');

        String materialName;
        short dataValue = 0;
        
        if (basicItemData.contains(":")) {
            String[] materialAndDataValue = Strings.split(basicItemData, ":", 2);
            try {
                dataValue = (short) Integer.parseInt(materialAndDataValue[1]);
            } catch (NumberFormatException e) {
                throw new HologramLoadException("data value \"" + materialAndDataValue[1] + "\" is not a valid number");
            }
            materialName = materialAndDataValue[0];
        } else {
            materialName = basicItemData;
        }
        
        Material material = MaterialsHelper.matchMaterial(materialName);
        if (material == null) {
            if (checkMaterialValidity) {
                throw new HologramLoadException("\"" + materialName + "\" is not a valid material");
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
                throw new HologramLoadException("invalid NBT data, " + e.getMessage());
            } catch (Throwable t) {
                throw new HologramLoadException("unexpected exception while parsing NBT data", t);
            }
        }
        
        return itemStack;
    }

}
