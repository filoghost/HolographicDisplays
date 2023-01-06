/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.config;

import me.filoghost.fcommons.Colors;
import me.filoghost.fcommons.MaterialsHelper;
import me.filoghost.fcommons.Strings;
import me.filoghost.holographicdisplays.core.placeholder.parsing.StringWithPlaceholders;
import me.filoghost.holographicdisplays.plugin.format.DisplayFormat;
import me.filoghost.holographicdisplays.plugin.internal.hologram.InternalHologramLine;
import me.filoghost.holographicdisplays.plugin.internal.hologram.ItemInternalHologramLine;
import me.filoghost.holographicdisplays.plugin.internal.hologram.TextInternalHologramLine;
import me.filoghost.holographicdisplays.plugin.lib.nbt.parser.MojangsonParseException;
import me.filoghost.holographicdisplays.plugin.lib.nbt.parser.MojangsonParser;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InternalHologramLineParser {

    private static final String ICON_PREFIX = "icon:";
    private static final Pattern PLACEHOLDER_API_SHORT_FORMAT = Pattern.compile("%(.+?)%");

    public static InternalHologramLine parseLine(String serializedLine) throws InternalHologramLoadException {
        if (serializedLine.toLowerCase(Locale.ROOT).startsWith(ICON_PREFIX)) {
            String serializedIcon = serializedLine.substring(ICON_PREFIX.length());
            ItemStack icon = parseItemStack(serializedIcon);
            return new ItemInternalHologramLine(serializedLine, icon);

        } else {
            String displayText = parseText(serializedLine);
            return new TextInternalHologramLine(serializedLine, displayText);
        }
    }

    protected static String parseText(String serializedLine) {
        String displayText = DisplayFormat.apply(serializedLine, false);
        if (Settings.placeholderAPIExpandShortFormat) {
            displayText = expandPlaceholderAPIShortFormat(displayText);
        }
        // Apply colors only outside placeholders
        displayText = StringWithPlaceholders.withEscapes(displayText).replaceOutsidePlaceholders(Colors::colorize);
        return displayText;
    }

    private static String expandPlaceholderAPIShortFormat(String text) {
        Matcher matcher = PLACEHOLDER_API_SHORT_FORMAT.matcher(text);
        boolean foundMatch = matcher.find();

        if (!foundMatch) {
            return text;
        }

        StringBuffer result = new StringBuffer();

        while (foundMatch) {
            String placeholderContent = matcher.group(1);
            matcher.appendReplacement(result, "");
            result.append("{papi: ").append(StringWithPlaceholders.addEscapes(placeholderContent)).append("}");
            foundMatch = matcher.find();
        }

        matcher.appendTail(result);
        return result.toString();
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
