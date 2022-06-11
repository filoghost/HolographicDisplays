/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.v1_19_r1;

import io.netty.handler.codec.EncoderException;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

class DataWatcherKey<T> {

    private static final EntityDataSerializer<Byte> BYTE_SERIALIZER = EntityDataSerializers.BYTE;
    private static final EntityDataSerializer<Integer> INT_SERIALIZER = EntityDataSerializers.INT;
    private static final EntityDataSerializer<Boolean> BOOLEAN_SERIALIZER = EntityDataSerializers.BOOLEAN;
    private static final EntityDataSerializer<ItemStack> ITEM_STACK_SERIALIZER = EntityDataSerializers.ITEM_STACK;
    private static final EntityDataSerializer<Optional<Component>> OPTIONAL_CHAT_COMPONENT_SERIALIZER = EntityDataSerializers.OPTIONAL_COMPONENT;

    static final DataWatcherKey<Byte> ENTITY_STATUS = new DataWatcherKey<>(0, BYTE_SERIALIZER);
    static final DataWatcherKey<Optional<Component>> CUSTOM_NAME = new DataWatcherKey<>(2, OPTIONAL_CHAT_COMPONENT_SERIALIZER);
    static final DataWatcherKey<Boolean> CUSTOM_NAME_VISIBILITY = new DataWatcherKey<>(3, BOOLEAN_SERIALIZER);
    static final DataWatcherKey<ItemStack> ITEM_STACK = new DataWatcherKey<>(8, ITEM_STACK_SERIALIZER);
    static final DataWatcherKey<Byte> ARMOR_STAND_STATUS = new DataWatcherKey<>(15, BYTE_SERIALIZER);
    static final DataWatcherKey<Integer> SLIME_SIZE = new DataWatcherKey<>(16, INT_SERIALIZER);

    private final int index;
    private final EntityDataSerializer<T> serializer;
    private final int serializerTypeID;

    private DataWatcherKey(int index, EntityDataSerializer<T> serializer) {
        this.index = index;
        this.serializer = serializer;
        this.serializerTypeID = EntityDataSerializers.getSerializedId(serializer);
        if (serializerTypeID < 0) {
            throw new EncoderException("Could not find serializer ID of " + serializer);
        }
    }

    int getIndex() {
        return index;
    }

    EntityDataSerializer<T> getSerializer() {
        return serializer;
    }

    int getSerializerTypeID() {
        return serializerTypeID;
    }

}
