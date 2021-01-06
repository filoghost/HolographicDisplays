/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.util;

public class VersionUtils {

    /**
     * @return 1 if reference > comparison, 0 if reference == comparison, -1 if reference < comparison
     */
    private static int compare(String reference, String comparison) throws NumberFormatException {
        String[] referenceSplit = reference.split("\\.");
        String[] comparisonSplit = comparison.split("\\.");
        
        int longest = Math.max(referenceSplit.length, comparisonSplit.length);
        
        // Default value is 0
        int[] referenceNumbersArray = new int[longest];
        int[] comparisonNumbersArray = new int[longest];
        
        for (int i = 0; i < referenceSplit.length; i++) {
            referenceNumbersArray[i] = Integer.parseInt(referenceSplit[i]);
        }
        
        for (int i = 0; i < comparisonSplit.length; i++) {
            comparisonNumbersArray[i] = Integer.parseInt(comparisonSplit[i]);
        }
        
        for (int i = 0; i < longest; i++) {
            int diff = referenceNumbersArray[i] - comparisonNumbersArray[i];
            if (diff > 0) {
                return 1;
            } else if (diff < 0) {
                return -1;
            }
        }
        
        return 0;
    }
    
    
    public static boolean isVersionGreaterEqual(String reference, String thanWhat) {
        return compare(reference, thanWhat) >= 0;
    }
    
}
