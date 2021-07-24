/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.v1_17_R1;

class DataWatcherEntry<T> {

    private final DataWatcherKey<T> key;
    private final T value;

    DataWatcherEntry(DataWatcherKey<T> key, T value) {
        this.key = key;
        this.value = value;
    }

    DataWatcherKey<T> getKey() {
        return key;
    }

    T getValue() {
        return value;
    }

}
