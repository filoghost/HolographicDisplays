/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.api.placeholder;

public interface PlaceholderReplacer {

    /**
     * Called to update a placeholder's replacement.
     * @return the replacement
     */
    String update();
    
}
