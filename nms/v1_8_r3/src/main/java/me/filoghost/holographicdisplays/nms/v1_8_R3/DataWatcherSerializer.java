/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.v1_8_R3;

import net.minecraft.server.v1_8_R3.ItemStack;

import java.util.function.BiConsumer;

class DataWatcherSerializer<T> {

    static final DataWatcherSerializer<Byte> BYTE = new DataWatcherSerializer<>(0, PacketByteBuffer::writeByte);
    static final DataWatcherSerializer<String> STRING = new DataWatcherSerializer<>(4, PacketByteBuffer::writeString);
    static final DataWatcherSerializer<ItemStack> ITEM_STACK = new DataWatcherSerializer<>(5, PacketByteBuffer::writeItemStack);

    private final int typeID;
    private final BiConsumer<PacketByteBuffer, T> serializeFunction;

    private DataWatcherSerializer(int typeID, BiConsumer<PacketByteBuffer, T> serializeFunction) {
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
