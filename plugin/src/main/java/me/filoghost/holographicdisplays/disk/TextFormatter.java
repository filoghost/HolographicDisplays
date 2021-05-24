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

public class TextFormatter {
    
    public static String toDisplayFormat(String input) {
        if (input == null) {
            return null;
        }

        input = StaticReplacements.searchAndReplace(input);
        input = Colors.addColors(input);
        return input;
    }
    
}
