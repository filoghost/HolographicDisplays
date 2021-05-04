/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.core;

import me.filoghost.fcommons.Preconditions;
import me.filoghost.fcommons.Strings;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Utils {

    /**
     * Converts a generic array to a list of Strings using the method toString().
     */
    public static List<String> toStringList(Object... array) {
        List<String> result = new ArrayList<>(array.length);
        for (Object obj : array) {
            result.add(Objects.toString(obj));
        }
        return result;
    }
    
    public static int floor(double num) {
        int floor = (int) num;
        return floor == num ? floor : floor - (int) (Double.doubleToRawLongBits(num) >>> 63);
    }
    
    
    public static double square(double num) {
        return num * num;
    }
    
    
    public static String join(String[] elements, String separator, int startIndex, int endIndex) {
        Preconditions.checkArgument(startIndex >= 0 && startIndex < elements.length, "startIndex out of bounds");
        Preconditions.checkArgument(endIndex >= 0 && endIndex <= elements.length, "endIndex out of bounds");
        Preconditions.checkArgument(startIndex <= endIndex, "startIndex lower than endIndex");
        
        StringBuilder result = new StringBuilder();
        
        while (startIndex < endIndex) {
            if (result.length() != 0) {
                result.append(separator);
            }
            
            if (elements[startIndex] != null) {
                result.append(elements[startIndex]);
            }
            startIndex++;
        }
        
        return result.toString();
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
