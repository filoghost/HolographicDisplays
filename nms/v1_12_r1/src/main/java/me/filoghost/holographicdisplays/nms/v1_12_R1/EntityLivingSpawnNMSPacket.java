/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.v1_12_R1;

import me.filoghost.fcommons.reflection.ReflectField;
import me.filoghost.holographicdisplays.common.PositionCoordinates;
import me.filoghost.holographicdisplays.nms.common.EntityID;
import net.minecraft.server.v1_12_R1.DataWatcher;
import net.minecraft.server.v1_12_R1.Packet;
import net.minecraft.server.v1_12_R1.PacketDataSerializer;
import net.minecraft.server.v1_12_R1.PacketPlayOutSpawnEntityLiving;

class EntityLivingSpawnNMSPacket extends VersionNMSPacket {

    private static final ReflectField<DataWatcher> DATA_WATCHER_FIELD =
            ReflectField.lookup(DataWatcher.class, PacketPlayOutSpawnEntityLiving.class, "m");

    private final Packet<?> rawPacket;

    private EntityLivingSpawnNMSPacket(Packet<?> rawPacket, DataWatcher dataWatcher) {
        this.rawPacket = rawPacket;
        try {
            DATA_WATCHER_FIELD.set(rawPacket, dataWatcher);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    Packet<?> getRawPacket() {
        return rawPacket;
    }

    public static DataWatcherPacketBuilder<EntityLivingSpawnNMSPacket> builder(
            EntityID entityID, int entityTypeID, PositionCoordinates position, double positionOffsetY) {
        PacketByteBuffer packetByteBuffer = PacketByteBuffer.get();

        packetByteBuffer.writeVarInt(entityID.getNumericID());
        packetByteBuffer.writeUUID(entityID.getUUID());
        packetByteBuffer.writeVarInt(entityTypeID);

        // Position
        packetByteBuffer.writeDouble(position.getX());
        packetByteBuffer.writeDouble(position.getY() + positionOffsetY);
        packetByteBuffer.writeDouble(position.getZ());

        // Rotation
        packetByteBuffer.writeByte(0);
        packetByteBuffer.writeByte(0);

        // Head rotation
        packetByteBuffer.writeByte(0);

        // Velocity
        packetByteBuffer.writeShort(0);
        packetByteBuffer.writeShort(0);
        packetByteBuffer.writeShort(0);

        // Entries are deserialized and saved into a field which is not used during the re-serialization, set as empty
        packetByteBuffer.writeDataWatcherEntriesEnd();

        Packet<?> rawPacket = packetByteBuffer.writeDataTo(new PacketPlayOutSpawnEntityLiving());
        return new Builder(rawPacket, PacketByteBuffer.get());
    }


    private static class Builder extends DataWatcherPacketBuilder<EntityLivingSpawnNMSPacket> {

        private final Packet<?> rawPacket;

        private Builder(Packet<?> rawPacket, PacketByteBuffer packetByteBuffer) {
            super(packetByteBuffer);
            this.rawPacket = rawPacket;
        }

        @Override
        EntityLivingSpawnNMSPacket createPacket(PacketByteBuffer packetByteBuffer) {
            return new EntityLivingSpawnNMSPacket(rawPacket, new SerializedDataWatcher(packetByteBuffer));
        }

    }


    private static class SerializedDataWatcher extends DataWatcher {

        private final byte[] serializedValue;

        SerializedDataWatcher(PacketByteBuffer dataByteBuffer) {
            super(null);
            serializedValue = new byte[dataByteBuffer.readableBytes()];
            dataByteBuffer.readBytes(serializedValue);
        }

        @Override
        public void a(PacketDataSerializer packetDataSerializer) {
            packetDataSerializer.writeBytes(serializedValue);
        }

    }

}
