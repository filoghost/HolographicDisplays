/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.core;

import me.filoghost.fcommons.Strings;

public class Utils {
    
    public static double square(double num) {
        return num * num;
    }

    public static String formatExceptionMessage(Throwable t) {
        return formatExceptionMessage(t.getMessage());
    }
    
    public static String formatExceptionMessage(String message) {
        if (Strings.isEmpty(message)) {
            return message;
        }
        
        message = Strings.capitalizeFirst(message);
        char lastChar = message.charAt(message.length() - 1);
        if (Character.isLetterOrDigit(lastChar)) {
            message = message + ".";
        }
        return message;
    }

}
