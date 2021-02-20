/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.util;

public class VersionUtils {

    public static boolean isVersionGreaterEqual(String reference, String thanWhat) {
        return compare(reference, thanWhat) >= 0;
    }

    /**
     * @return 1 if version1 > version2, 0 if version1 == version2, -1 if version1 < version2
     */
    private static int compare(String version1, String version2) throws NumberFormatException {
        String[] version1Split = version1.split("\\.");
        String[] version2Split = version2.split("\\.");
        
        int longest = Math.max(version1Split.length, version2Split.length);
        
        // Default value is 0 (for elements not explicitly set)
        int[] version1NumbersArray = new int[longest];
        int[] version2NumbersArray = new int[longest];
        
        for (int i = 0; i < version1Split.length; i++) {
            version1NumbersArray[i] = Integer.parseInt(version1Split[i]);
        }
        
        for (int i = 0; i < version2Split.length; i++) {
            version2NumbersArray[i] = Integer.parseInt(version2Split[i]);
        }
        
        for (int i = 0; i < longest; i++) {
            int diff = version1NumbersArray[i] - version2NumbersArray[i];
            if (diff > 0) {
                return 1;
            } else if (diff < 0) {
                return -1;
            }
        }
        
        return 0;
    }
    
}
