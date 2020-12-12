/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.disk;

import me.filoghost.fcommons.Colors;

public class StringConverter {
    
    public static String toReadableFormat(String input) {
        if (input == null) {
            return null;
        }

        input = UnicodeSymbols.placeholdersToSymbols(input);
        input = Colors.addColors(input);
        return input;
    }
    
}
