/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.core.nms.entity;

public abstract class NMSEntityHelper<T> {

    private T tracker;

    protected final T getTracker() {
        if (tracker == null) {
            // Cache it the first time it's available
            tracker = getTracker0();
        }

        return tracker;
    }

    protected abstract T getTracker0();

}
