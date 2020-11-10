/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.exception;

public class HologramLineParseException extends Exception {

    private static final long serialVersionUID = 1L;

    public HologramLineParseException(String message) {
        super(message);
    }

    public HologramLineParseException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
