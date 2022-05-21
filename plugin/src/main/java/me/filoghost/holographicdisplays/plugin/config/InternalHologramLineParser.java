/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.config;

import me.filoghost.fcommons.Colors;
import me.filoghost.fcommons.MaterialsHelper;
import me.filoghost.fcommons.Strings;
import me.filoghost.holographicdisplays.plugin.format.DisplayFormat;
import me.filoghost.holographicdisplays.plugin.internal.hologram.InternalHologramLine;
import me.filoghost.holographicdisplays.plugin.internal.hologram.ItemInternalHologramLine;
import me.filoghost.holographicdisplays.plugin.internal.hologram.TextInternalHologramLine;
import me.filoghost.holographicdisplays.plugin.lib.nbt.parser.MojangsonParseException;
import me.filoghost.holographicdisplays.plugin.lib.nbt.parser.MojangsonParser;
import me.filoghost.holographicdisplays.core.placeholder.parsing.StringWithPlaceholders;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Locale;

public class InternalHologramLineParser {

    private static final String ICON_PREFIX = "icon:";

    public static InternalHologramLine parseLine(String serializedLine) throws InternalHologramLoadException {
        if (serializedLine.toLowerCase(Locale.ROOT).startsWith(ICON_PREFIX)) {
            String serializedIcon = serializedLine.substring(ICON_PREFIX.length());
            ItemStack icon = parseItemStack(serializedIcon);
            return new ItemInternalHologramLine(serializedLine, icon);

        } else {
            String displayText = DisplayFormat.apply(serializedLine, false);
            // Apply colors only outside placeholders
            displayText = StringWithPlaceholders.withEscapes(displayText).replaceStrings(Colors::colorize);
            return new TextInternalHologramLine(serializedLine, displayText);
        }
    }

    @SuppressWarnings("deprecation")
    private static ItemStack parseItemStack(String serializedItem) throws InternalHologramLoadException {
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
                throw new InternalHologramLoadException("data value \"" + materialAndDataValue[1] + "\" is not a valid number");
            }
            materialName = materialAndDataValue[0];
        } else {
            materialName = basicItemData;
        }

        Material material = MaterialsHelper.matchMaterial(materialName);
        if (material == null) {
            throw new InternalHologramLoadException("\"" + materialName + "\" is not a valid material");
        }

        ItemStack itemStack = new ItemStack(material, 1, dataValue);

        if (nbtString != null) {
            try {
                // Check NBT syntax validity before applying it
                MojangsonParser.parse(nbtString);
                Bukkit.getUnsafe().modifyItemStack(itemStack, nbtString);
            } catch (MojangsonParseException e) {
                throw new InternalHologramLoadException("invalid NBT data, " + e.getMessage());
            } catch (Exception e) {
                throw new InternalHologramLoadException("unexpected exception while parsing NBT data", e);
            }
        }

        return itemStack;
    }

}
