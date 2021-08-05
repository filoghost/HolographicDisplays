/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.util;

import java.util.function.BooleanSupplier;

public class CachedBoolean {

    private final BooleanSupplier valueGetter;
    private boolean value;
    private boolean valid;

    public CachedBoolean(BooleanSupplier valueGetter) {
        this.valueGetter = valueGetter;
    }

    public void set(boolean value) {
        this.valid = true;
        this.value = value;
    }

    public boolean get() {
        if (!valid) {
            value = valueGetter.getAsBoolean();
            valid = true;
        }

        return value;
    }

    public void invalidate() {
        valid = false;
    }

}
