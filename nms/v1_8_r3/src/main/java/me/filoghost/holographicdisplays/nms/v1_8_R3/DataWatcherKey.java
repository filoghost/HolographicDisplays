/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.v1_8_R3;

import net.minecraft.server.v1_8_R3.ItemStack;

class DataWatcherKey<T> {

    static final DataWatcherKey<Byte> ENTITY_STATUS = new DataWatcherKey<>(0, DataWatcherSerializer.BYTE);
    static final DataWatcherKey<String> CUSTOM_NAME = new DataWatcherKey<>(2, DataWatcherSerializer.STRING);
    static final DataWatcherKey<Byte> CUSTOM_NAME_VISIBILITY = new DataWatcherKey<>(3, DataWatcherSerializer.BYTE);
    static final DataWatcherKey<ItemStack> ITEM_STACK = new DataWatcherKey<>(10, DataWatcherSerializer.ITEM_STACK);
    static final DataWatcherKey<Byte> ARMOR_STAND_STATUS = new DataWatcherKey<>(10, DataWatcherSerializer.BYTE);
    static final DataWatcherKey<Byte> SLIME_SIZE = new DataWatcherKey<>(16, DataWatcherSerializer.BYTE);

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
