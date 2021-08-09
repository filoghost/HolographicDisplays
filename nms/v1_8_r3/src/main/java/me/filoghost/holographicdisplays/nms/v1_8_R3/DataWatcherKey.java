/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.v1_8_R3;

import net.minecraft.server.v1_8_R3.ItemStack;

class DataWatcherKey<T> {

    private static final DataWatcherSerializer<Byte> BYTE_SERIALIZER = new DataWatcherSerializer<>(0, PacketByteBuffer::writeByte);
    private static final DataWatcherSerializer<String> STRING_SERIALIZER = new DataWatcherSerializer<>(4, PacketByteBuffer::writeString);
    private static final DataWatcherSerializer<ItemStack> ITEM_STACK_SERIALIZER = new DataWatcherSerializer<>(5, PacketByteBuffer::writeItemStack);

    static final DataWatcherKey<Byte> ENTITY_STATUS = new DataWatcherKey<>(0, BYTE_SERIALIZER);
    static final DataWatcherKey<String> CUSTOM_NAME = new DataWatcherKey<>(2, STRING_SERIALIZER);
    static final DataWatcherKey<Byte> CUSTOM_NAME_VISIBILITY = new DataWatcherKey<>(3, BYTE_SERIALIZER);
    static final DataWatcherKey<ItemStack> ITEM_STACK = new DataWatcherKey<>(10, ITEM_STACK_SERIALIZER);
    static final DataWatcherKey<Byte> ARMOR_STAND_STATUS = new DataWatcherKey<>(10, BYTE_SERIALIZER);
    static final DataWatcherKey<Byte> SLIME_SIZE = new DataWatcherKey<>(16, BYTE_SERIALIZER);

    private final int index;
    private final DataWatcherSerializer<T> serializer;

    private DataWatcherKey(int index, DataWatcherSerializer<T> serializer) {
        this.index = index;
        this.serializer = serializer;
    }

    int getIndex() {
        return index;
    }

    DataWatcherSerializer<T> getSerializer() {
        return serializer;
    }

}
