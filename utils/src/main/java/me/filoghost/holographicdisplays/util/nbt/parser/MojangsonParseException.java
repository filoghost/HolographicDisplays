/*
 * Copyright (C) Jan Schultke
 *
 * SPDX-License-Identifier: MIT
 */
package me.filoghost.holographicdisplays.util.nbt.parser;

import org.bukkit.ChatColor;

import java.io.IOException;

public class MojangsonParseException extends IOException {

    private static final long serialVersionUID = 1L;

    public MojangsonParseException(String msg, String content, int index) {
        super(msg + " at character " + index + ": " + printErrorLoc(content, index));
    }

    private static String printErrorLoc(String content, int index) {
        StringBuilder builder = new StringBuilder();
        int i = Math.min(content.length(), index);
        if (i > 35) {
            builder.append("...");
        }
        builder.append(content.substring(Math.max(0, i - 35), i));
        builder.append(ChatColor.GOLD + "<--[HERE]");

        return builder.toString();
    }

}
