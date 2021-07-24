/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.v1_17_R1;

import io.netty.handler.codec.EncoderException;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.syncher.DataWatcherRegistry;
import net.minecraft.network.syncher.DataWatcherSerializer;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

class DataWatcherKey<T> {

    private static final DataWatcherSerializer<Byte> BYTE_SERIALIZER = DataWatcherRegistry.a;
    private static final DataWatcherSerializer<Boolean> BOOLEAN_SERIALIZER = DataWatcherRegistry.i;
    private static final DataWatcherSerializer<ItemStack> ITEM_STACK_SERIALIZER = DataWatcherRegistry.g;
    private static final DataWatcherSerializer<Optional<IChatBaseComponent>> OPTIONAL_CHAT_COMPONENT_SERIALIZER = DataWatcherRegistry.f;

    static DataWatcherKey<Byte> ENTITY_STATUS = new DataWatcherKey<>(0, BYTE_SERIALIZER);
    static DataWatcherKey<Optional<IChatBaseComponent>> CUSTOM_NAME = new DataWatcherKey<>(2, OPTIONAL_CHAT_COMPONENT_SERIALIZER);
    static DataWatcherKey<Boolean> CUSTOM_NAME_VISIBILITY = new DataWatcherKey<>(3, BOOLEAN_SERIALIZER);
    static DataWatcherKey<ItemStack> ITEM_STACK = new DataWatcherKey<>(8, ITEM_STACK_SERIALIZER);
    static DataWatcherKey<Byte> ARMOR_STAND_STATUS = new DataWatcherKey<>(15, BYTE_SERIALIZER);

    private final int keyIndex;
    private final DataWatcherSerializer<T> serializer;
    private final int serializerTypeID;

    private DataWatcherKey(int keyIndex, DataWatcherSerializer<T> serializer) {
        this.keyIndex = keyIndex;
        this.serializer = serializer;
        this.serializerTypeID = DataWatcherRegistry.b(serializer);
        if (serializerTypeID < 0) {
            throw new EncoderException("Could not find serializer ID of " + serializer);
        }
    }

    int getKeyIndex() {
        return keyIndex;
    }

    DataWatcherSerializer<T> getSerializer() {
        return serializer;
    }

    int getSerializerTypeID() {
        return serializerTypeID;
    }

}
