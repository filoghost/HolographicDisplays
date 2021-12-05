/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.tick;

import java.util.Map;
import java.util.function.BiPredicate;

public class TickExpiringMap<K, V extends TickExpiringValue> {

    private final Map<K, V> map;

    // Entries that are not used for this amount of ticks are removed
    private final long expirationTicks;

    public TickExpiringMap(Map<K, V> map, long expirationTicks) {
        this.map = map;
        this.expirationTicks = expirationTicks;
    }

    public void removeEntries(BiPredicate<? super K, ? super V> filter) {
        map.entrySet().removeIf(entry -> filter.test(entry.getKey(), entry.getValue()));
    }

    public void clearUnusedEntries(long currentTick) {
        map.values().removeIf(value -> currentTick - value.getLastUseTick() >= expirationTicks);
    }

    public V get(K key) {
        return map.get(key);
    }

    public void put(K key, V value) {
        map.put(key, value);
    }

}
