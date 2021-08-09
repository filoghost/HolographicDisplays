/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.v1_12_R1;

import io.netty.handler.codec.EncoderException;
import net.minecraft.server.v1_12_R1.DataWatcherRegistry;
import net.minecraft.server.v1_12_R1.DataWatcherSerializer;
import net.minecraft.server.v1_12_R1.ItemStack;

class DataWatcherKey<T> {

    private static final DataWatcherSerializer<Byte> BYTE_SERIALIZER = DataWatcherRegistry.a;
    private static final DataWatcherSerializer<Integer> INT_SERIALIZER = DataWatcherRegistry.b;
    private static final DataWatcherSerializer<Boolean> BOOLEAN_SERIALIZER = DataWatcherRegistry.h;
    private static final DataWatcherSerializer<ItemStack> ITEM_STACK_SERIALIZER = DataWatcherRegistry.f;
    private static final DataWatcherSerializer<String> STRING_SERIALIZER = DataWatcherRegistry.d;

    static final DataWatcherKey<Byte> ENTITY_STATUS = new DataWatcherKey<>(0, BYTE_SERIALIZER);
    static final DataWatcherKey<String> CUSTOM_NAME = new DataWatcherKey<>(2, STRING_SERIALIZER);
    static final DataWatcherKey<Boolean> CUSTOM_NAME_VISIBILITY = new DataWatcherKey<>(3, BOOLEAN_SERIALIZER);
    static final DataWatcherKey<ItemStack> ITEM_STACK = new DataWatcherKey<>(6, ITEM_STACK_SERIALIZER);
    static final DataWatcherKey<Byte> ARMOR_STAND_STATUS = new DataWatcherKey<>(11, BYTE_SERIALIZER);
    static final DataWatcherKey<Integer> SLIME_SIZE = new DataWatcherKey<>(12, INT_SERIALIZER);

    private final int index;
    private final DataWatcherSerializer<T> serializer;
    private final int serializerTypeID;

    private DataWatcherKey(int index, DataWatcherSerializer<T> serializer) {
        this.index = index;
        this.serializer = serializer;
        this.serializerTypeID = DataWatcherRegistry.b(serializer);
        if (serializerTypeID < 0) {
            throw new EncoderException("Could not find serializer ID of " + serializer);
        }
    }

    int getIndex() {
        return index;
    }

    DataWatcherSerializer<T> getSerializer() {
        return serializer;
    }

    int getSerializerTypeID() {
        return serializerTypeID;
    }

}
