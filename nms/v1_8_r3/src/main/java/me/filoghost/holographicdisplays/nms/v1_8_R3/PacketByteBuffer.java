/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.v1_8_R3;

import io.netty.buffer.Unpooled;
import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.PacketDataSerializer;

class PacketByteBuffer extends PacketDataSerializer {

    private static final PacketByteBuffer INSTANCE = new PacketByteBuffer();

    static PacketByteBuffer get() {
        INSTANCE.clear();
        return INSTANCE;
    }

    private PacketByteBuffer() {
        super(Unpooled.buffer());
    }

    void writeByte(byte b) {
        super.writeByte(b);
    }

    void writeVarInt(int i) {
        super.b(i);
    }

    void writeString(String s) {
        super.a(s);
    }

    void writeItemStack(ItemStack itemStack) {
        super.a(itemStack);
    }

    <T> void writeDataWatcherEntry(DataWatcherKey<T> key, T value) {
        int typeIDAndIndex = (key.getSerializer().getTypeID() << 5 | key.getIndex() & 0x1F) & 0xFF;
        writeByte(typeIDAndIndex);
        key.getSerializer().write(this, value);
    }

    void writeDataWatcherEntriesEnd() {
        writeByte(0x7F);
    }

}
