/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.v1_17_R1;

import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketDataSerializer;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.ints.IntList;

import java.util.UUID;

class PacketByteBuffer extends PacketDataSerializer {

    private static final PacketByteBuffer INSTANCE = new PacketByteBuffer();

    static PacketByteBuffer get() {
        INSTANCE.clear();
        return INSTANCE;
    }

    private PacketByteBuffer() {
        super(Unpooled.buffer());
    }

    void writeVarInt(int i) {
        super.d(i);
    }

    void writeUUID(UUID uuid) {
        super.a(uuid);
    }

    void writeIntList(IntList intList) {
        super.a(intList);
    }

    void writeIntArray(int... array) {
        super.a(array);
    }

    <T> void writeDataWatcherEntry(DataWatcherKey<T> key, T value) {
        writeByte(key.getIndex());
        writeVarInt(key.getSerializerTypeID());
        key.getSerializer().a(this, value);
    }

    void writeDataWatcherEntriesEnd() {
        writeByte(255);
    }

}
