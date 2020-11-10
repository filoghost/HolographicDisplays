/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.exception;

public class WorldNotFoundException extends Exception {

    private static final long serialVersionUID = 1L;
    
    public WorldNotFoundException(String message) {
        super(message);
    }

}
