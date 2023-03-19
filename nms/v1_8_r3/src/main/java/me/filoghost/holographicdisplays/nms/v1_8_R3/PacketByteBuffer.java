/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.v1_8_R3;

import io.netty.buffer.Unpooled;
import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketDataSerializer;

import java.io.IOException;

class PacketByteBuffer {

    private static final PacketByteBuffer INSTANCE = new PacketByteBuffer();

    private final PacketDataSerializer serializer;

    static PacketByteBuffer get() {
        INSTANCE.clear();
        return INSTANCE;
    }

    private PacketByteBuffer() {
        this.serializer = new PacketDataSerializer(Unpooled.buffer());
    }

    int readableBytes() {
        return serializer.readableBytes();
    }

    void readBytes(byte[] bytes) {
        serializer.readBytes(bytes);
    }

    void writeBoolean(boolean flag) {
        serializer.writeBoolean(flag);
    }

    void writeByte(byte b) {
        serializer.writeByte(b);
    }

    void writeByte(int i) {
        serializer.writeByte(i);
    }

    void writeShort(int i) {
        serializer.writeShort(i);
    }

    void writeInt(int i) {
        serializer.writeInt(i);
    }

    void writeVarInt(int i) {
        serializer.b(i);
    }

    void writeString(String s) {
        serializer.a(s);
    }

    void writeItemStack(ItemStack itemStack) {
        serializer.a(itemStack);
    }

    <T> void writeDataWatcherEntry(DataWatcherKey<T> key, T value) {
        int typeIDAndIndex = (key.getSerializer().getTypeID() << 5 | key.getIndex() & 0x1F) & 0xFF;
        serializer.writeByte(typeIDAndIndex);
        key.getSerializer().write(this, value);
    }

    void writeDataWatcherEntriesEnd() {
        serializer.writeByte(0x7F);
    }

    void clear() {
        serializer.clear();
    }

    <T extends Packet<?>> T writeDataTo(T packet) {
        try {
            packet.a(serializer);
            return packet;
        } catch (IOException e) {
            // Never thrown by the implementations
            throw new RuntimeException(e);
        }
    }

}
