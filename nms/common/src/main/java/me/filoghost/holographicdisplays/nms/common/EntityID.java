/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.common;

import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.function.Supplier;

public class EntityID {

    private final Supplier<Integer> numericIDGenerator;

    // Lazy initialization
    private @Nullable Integer numericID;
    private @Nullable UUID uuid;

    public EntityID(Supplier<Integer> numericIDGenerator) {
        this.numericIDGenerator = numericIDGenerator;
    }

    public int getNumericID() {
        if (numericID == null) {
            numericID = numericIDGenerator.get();
        }
        return numericID;
    }

    public UUID getUUID() {
        if (uuid == null) {
            uuid = UUID.randomUUID();
        }
        return uuid;
    }

    public boolean hasInitializedNumericID() {
        return numericID != null;
    }

}
