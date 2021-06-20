/*
 * Copyright (C) Jan Schultke
 *
 * SPDX-License-Identifier: MIT
 */
package me.filoghost.holographicdisplays.plugin.lib.nbt.parser;

import java.io.IOException;

public class MojangsonParseException extends IOException {

    public MojangsonParseException(String msg, String content, int index) {
        super(msg + " at character " + index + ": " + printErrorLoc(content, index));
    }

    private static String printErrorLoc(String content, int index) {
        StringBuilder builder = new StringBuilder();
        int i = Math.min(content.length(), index);
        if (i > 35) {
            builder.append("...");
        }
        builder.append(content, Math.max(0, i - 35), i);
        builder.append("<--[HERE]");

        return builder.toString();
    }

}
