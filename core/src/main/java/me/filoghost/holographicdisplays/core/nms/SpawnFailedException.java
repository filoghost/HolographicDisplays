/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.core.nms;

public class SpawnFailedException extends Exception {

    public static final String REGISTER_ENTITY_FAIL = "failed to register entity";
    public static final String CHUNK_NOT_LOADED = "chunk was not loaded";
    public static final String ADD_ENTITY_FAILED = "failed to add entity with workaround";

    public SpawnFailedException(String message) {
        super(message);
    }

    public SpawnFailedException(String message, Throwable cause) {
        super(message, cause);
    }

}
