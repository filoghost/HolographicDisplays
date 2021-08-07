/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.common.nms;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface NMSPacketList {

    void addArmorStandSpawnPackets(EntityID entityID, double positionX, double positionY, double positionZ);

    void addArmorStandSpawnPackets(EntityID entityID, double positionX, double positionY, double positionZ, String customName);

    void addArmorStandSpawnPackets(EntityID entityID, double positionX, double positionY, double positionZ, IndividualCustomName individualCustomName);

    void addArmorStandNameChangePackets(EntityID entityID, String customName);

    void addArmorStandNameChangePackets(EntityID entityID, IndividualCustomName individualCustomName);

    void addItemSpawnPackets(EntityID entityID, double positionX, double positionY, double positionZ, ItemStack itemStack);

    void addItemStackChangePackets(EntityID entityID, ItemStack itemStack);

    void addSlimeSpawnPackets(EntityID entityID, double positionX, double positionY, double positionZ);

    void addEntityDestroyPackets(EntityID... entityIDs);

    void addTeleportPackets(EntityID entityID, double positionX, double positionY, double positionZ);

    void addMountPackets(EntityID vehicleEntityID, EntityID passengerEntityID);

    void sendTo(Player player);

}
