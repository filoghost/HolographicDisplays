/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.v1_17_R1;

import me.filoghost.fcommons.Strings;
import me.filoghost.fcommons.logging.Log;
import me.filoghost.holographicdisplays.common.nms.AbstractNMSPacketList;
import me.filoghost.holographicdisplays.common.nms.EntityID;
import me.filoghost.holographicdisplays.common.nms.IndividualCustomName;
import me.filoghost.holographicdisplays.common.nms.IndividualNMSPacket;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.ints.IntList;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_17_R1.util.CraftChatMessage;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.util.Optional;

class VersionNMSPacketList extends AbstractNMSPacketList {

    private static final DataWatcherEntry<Byte> ENTITY_STATUS_INVISIBLE = new DataWatcherEntry<>(DataWatcherKey.ENTITY_STATUS, (byte) 0x20);
    private static final DataWatcherEntry<Boolean> CUSTOM_NAME_VISIBLE = new DataWatcherEntry<>(DataWatcherKey.CUSTOM_NAME_VISIBILITY, true);
    private static final DataWatcherEntry<Boolean> CUSTOM_NAME_INVISIBLE = new DataWatcherEntry<>(DataWatcherKey.CUSTOM_NAME_VISIBILITY, false);
    private static final DataWatcherEntry<Byte> ARMOR_STAND_STATUS_MARKER = new DataWatcherEntry<>(DataWatcherKey.ARMOR_STAND_STATUS, (byte) (0x01 | 0x08 | 0x10)); // Small, no base plate, marker

    private static final boolean USE_ENTITY_LIST_DESTROY_PACKET = useEntityListDestroyPacket();

    @Override
    public void addArmorStandSpawnPackets(EntityID entityID, double locationX, double locationY, double locationZ) {
        add(new EntityLivingSpawnNMSPacket(entityID, EntityTypeID.ARMOR_STAND, locationX, locationY, locationZ));
        add(new EntityMetadataNMSPacket(entityID,
                ENTITY_STATUS_INVISIBLE,
                ARMOR_STAND_STATUS_MARKER
        ));
    }

    @Override
    public void addArmorStandSpawnPackets(EntityID entityID, double locationX, double locationY, double locationZ, String customName) {
        add(new EntityLivingSpawnNMSPacket(entityID, EntityTypeID.ARMOR_STAND, locationX, locationY, locationZ));
        add(createFullArmorStandMetadataPacket(entityID, customName));
    }

    @Override
    public void addArmorStandSpawnPackets(EntityID entityID, double locationX, double locationY, double locationZ, IndividualCustomName individualCustomName) {
        add(new EntityLivingSpawnNMSPacket(entityID, EntityTypeID.ARMOR_STAND, locationX, locationY, locationZ));
        add(new IndividualNMSPacket(player -> createFullArmorStandMetadataPacket(entityID, individualCustomName.get(player))));
    }

    private EntityMetadataNMSPacket createFullArmorStandMetadataPacket(EntityID entityID, String customName) {
        return new EntityMetadataNMSPacket(entityID,
                ENTITY_STATUS_INVISIBLE,
                new DataWatcherEntry<>(DataWatcherKey.CUSTOM_NAME, getCustomNameDataWatcherValue(customName)),
                Strings.isEmpty(customName) ? CUSTOM_NAME_INVISIBLE : CUSTOM_NAME_VISIBLE,
                ARMOR_STAND_STATUS_MARKER
        );
    }

    @Override
    public void addArmorStandNameChangePackets(EntityID entityID, String customName) {
        add(createPartialArmorStandMetadataPacket(entityID, customName));
    }

    @Override
    public void addArmorStandNameChangePackets(EntityID entityID, IndividualCustomName individualCustomName) {
        add(new IndividualNMSPacket(player -> createPartialArmorStandMetadataPacket(entityID, individualCustomName.get(player))));
    }

    private EntityMetadataNMSPacket createPartialArmorStandMetadataPacket(EntityID entityID, String customName) {
        return new EntityMetadataNMSPacket(entityID,
                new DataWatcherEntry<>(DataWatcherKey.CUSTOM_NAME, getCustomNameDataWatcherValue(customName)),
                Strings.isEmpty(customName) ? CUSTOM_NAME_INVISIBLE : CUSTOM_NAME_VISIBLE
        );
    }

    private Optional<IChatBaseComponent> getCustomNameDataWatcherValue(String customName) {
        customName = Strings.truncate(customName, 300);
        if (!Strings.isEmpty(customName)) {
            return Optional.of(CraftChatMessage.fromString(customName, false, true)[0]);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public void addItemSpawnPackets(EntityID entityID, double locationX, double locationY, double locationZ, ItemStack itemStack) {
        add(new EntitySpawnNMSPacket(entityID, EntityTypeID.ITEM, locationX, locationY, locationZ));
        add(new EntityMetadataNMSPacket(entityID,
                new DataWatcherEntry<>(DataWatcherKey.ITEM_STACK, CraftItemStack.asNMSCopy(itemStack))
        ));
    }

    @Override
    public void addItemStackChangePackets(EntityID entityID, ItemStack itemStack) {
        add(new EntityMetadataNMSPacket(entityID,
                new DataWatcherEntry<>(DataWatcherKey.ITEM_STACK, CraftItemStack.asNMSCopy(itemStack))
        ));
    }

    @Override
    public void addSlimeSpawnPackets(EntityID entityID, double locationX, double locationY, double locationZ) {
        add(new EntityLivingSpawnNMSPacket(entityID, EntityTypeID.SLIME, locationX, locationY, locationZ));
        add(new EntityMetadataNMSPacket(entityID, ENTITY_STATUS_INVISIBLE));
    }

    @Override
    public void addEntityDestroyPackets(EntityID... entityIDs) {
        if (USE_ENTITY_LIST_DESTROY_PACKET) {
            add(new EntityListDestroyNMSPacket(entityIDs));
        } else {
            for (EntityID entityID : entityIDs) {
                add(new EntityDestroyNMSPacket(entityID));
            }
        }
    }

    @Override
    public void addTeleportPackets(EntityID entityID, double locationX, double locationY, double locationZ) {
        add(new EntityTeleportNMSPacket(entityID, locationX, locationY, locationZ));
    }

    @Override
    public void addMountPackets(EntityID vehicleEntityID, EntityID passengerEntityID) {
        add(new EntityMountNMSPacket(vehicleEntityID, passengerEntityID));
    }

    private static boolean useEntityListDestroyPacket() {
        try {
            for (Field field : PacketPlayOutEntityDestroy.class.getDeclaredFields()) {
                if (field.getType() == IntList.class) {
                    return true;
                }
            }
            return false;
        } catch (Throwable t) {
            Log.warning("Could not detect PacketPlayOutEntityDestroy details, error can be ignored if on Minecraft 1.17.1+", t);
            return true; // Assume newer Minecraft version
        }
    }

}
