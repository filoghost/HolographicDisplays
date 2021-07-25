/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.v1_17_R1;

import me.filoghost.fcommons.Strings;
import me.filoghost.holographicdisplays.common.nms.EntityID;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_17_R1.util.CraftChatMessage;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

class EntityMetadataNMSPacket extends VersionNMSPacket {

    private final Packet<?> rawPacket;

    private EntityMetadataNMSPacket(PacketByteBuffer packetByteBuffer) {
        this.rawPacket = new PacketPlayOutEntityMetadata(packetByteBuffer);
    }

    @Override
    Packet<?> getRawPacket() {
        return rawPacket;
    }

    public static Builder builder(EntityID entityID) {
        return new Builder(entityID);
    }


    static class Builder {

        private final PacketByteBuffer packetByteBuffer;

        private Builder(EntityID entityID) {
            this.packetByteBuffer = PacketByteBuffer.get();

            packetByteBuffer.writeVarInt(entityID.getNumericID());
        }

        Builder setInvisible() {
            packetByteBuffer.writeDataWatcherEntry(DataWatcherKey.ENTITY_STATUS, (byte) 0x20); // Invisible
            return this;
        }

        Builder setMarkerArmorStand() {
            setInvisible();
            packetByteBuffer.writeDataWatcherEntry(DataWatcherKey.ARMOR_STAND_STATUS, (byte) (0x01 | 0x08 | 0x10)); // Small, no base plate, marker
            return this;
        }

        Builder setCustomName(String customName) {
            packetByteBuffer.writeDataWatcherEntry(DataWatcherKey.CUSTOM_NAME, getCustomNameDataWatcherValue(customName));
            packetByteBuffer.writeDataWatcherEntry(DataWatcherKey.CUSTOM_NAME_VISIBILITY, !Strings.isEmpty(customName));
            return this;
        }

        private Optional<IChatBaseComponent> getCustomNameDataWatcherValue(String customName) {
            customName = Strings.truncate(customName, 300);
            if (!Strings.isEmpty(customName)) {
                return Optional.of(CraftChatMessage.fromString(customName, false, true)[0]);
            } else {
                return Optional.empty();
            }
        }

        Builder setItemStack(ItemStack itemStack) {
            packetByteBuffer.writeDataWatcherEntry(DataWatcherKey.ITEM_STACK, CraftItemStack.asNMSCopy(itemStack));
            return this;
        }

        EntityMetadataNMSPacket build() {
            packetByteBuffer.writeDataWatcherEntriesEnd();
            return new EntityMetadataNMSPacket(packetByteBuffer);
        }

    }

}
