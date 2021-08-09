/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.v1_8_R3;

import java.util.function.BiConsumer;

class DataWatcherSerializer<T> {

    private final int typeID;
    private final BiConsumer<PacketByteBuffer, T> serializeFunction;

    DataWatcherSerializer(int typeID, BiConsumer<PacketByteBuffer, T> serializeFunction) {
        this.typeID = typeID;
        this.serializeFunction = serializeFunction;
    }

    int getTypeID() {
        return typeID;
    }

    void write(PacketByteBuffer packetByteBuffer, T value) {
        serializeFunction.accept(packetByteBuffer, value);
    }

}
