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

    static PacketByteBuffer get() {
        return new PacketByteBuffer(); // TODO try to clear and re-use a single instance
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

    void writeDataWatcherEntries(DataWatcherEntry<?>... dataWatcherEntries) {
        for (DataWatcherEntry<?> dataWatcherItem : dataWatcherEntries) {
            writeDataWatcherEntry(dataWatcherItem);
        }
        writeByte(255); // End of data watcher entries
    }

    private <T> void writeDataWatcherEntry(DataWatcherEntry<T> dataWatcherItem) {
        writeByte(dataWatcherItem.getKey().getKeyIndex());
        writeVarInt(dataWatcherItem.getKey().getSerializerTypeID());
        dataWatcherItem.getKey().getSerializer().a(this, dataWatcherItem.getValue());
    }

}
