/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.common;

public class Validator {

    public static void notNull(Object o, String name) {
        if (o == null) {
            throw new NullPointerException(name + " cannot be null");
        }
    }
    
    public static void isTrue(boolean statement, String message) {
        if (!statement) {
            throw new IllegalArgumentException(message);
        }
    }
    
}
