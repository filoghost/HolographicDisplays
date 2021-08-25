/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.common;

import java.util.function.Supplier;

/**
 * Fallback entity ID generator that counts down from <code>Integer.MIN_VALUE / 2</code> to <code>Integer.MIN_VALUE</code>, looping over
 * when reaching the end.
 * <p>
 * It does not start from zero to avoid conflicts with fake entities created by other plugins, and it counts down to minimize the time
 * window where the real NMS counter and this one may return the same numbers.
 * <p>
 * The Minecraft client can disconnect if the server sends bad packets for two entities with the same ID, which may have different types.
 */
public class FallbackEntityIDGenerator implements Supplier<Integer> {

    private static final int COUNTER_START_VALUE = Integer.MIN_VALUE / 2;
    private static final int COUNTER_END_VALUE = Integer.MIN_VALUE;

    private int counter = COUNTER_START_VALUE;

    @Override
    public Integer get() {
        // Loop over on allowed range values
        if (counter == COUNTER_END_VALUE) {
            counter = COUNTER_START_VALUE;
        }

        counter--;
        return counter;
    }

}
