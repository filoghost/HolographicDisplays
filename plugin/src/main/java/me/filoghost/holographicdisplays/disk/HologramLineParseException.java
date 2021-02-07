/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.disk;

public class HologramLineParseException extends HologramLoadException {

    public HologramLineParseException(String message) {
        super(message);
    }

    public HologramLineParseException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
