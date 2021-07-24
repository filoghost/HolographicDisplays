/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.common.nms;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface NMSPacketList {

    void addArmorStandSpawnPackets(EntityID entityID, double locationX, double locationY, double locationZ);

    void addArmorStandSpawnPackets(EntityID entityID, double locationX, double locationY, double locationZ, String customName);

    void addArmorStandSpawnPackets(EntityID entityID, double locationX, double locationY, double locationZ, IndividualCustomName individualCustomName);

    void addArmorStandNameChangePackets(EntityID entityID, String customName);

    void addArmorStandNameChangePackets(EntityID entityID, IndividualCustomName individualCustomName);

    void addItemSpawnPackets(EntityID entityID, double locationX, double locationY, double locationZ, ItemStack itemStack);

    void addItemStackChangePackets(EntityID entityID, ItemStack itemStack);

    void addSlimeSpawnPackets(EntityID entityID, double locationX, double locationY, double locationZ);

    void addEntityDestroyPackets(EntityID... entityIDs);

    void addTeleportPackets(EntityID entityID, double locationX, double locationY, double locationZ);

    void addMountPackets(EntityID vehicleEntityID, EntityID passengerEntityID);

    void sendTo(Player player);

}
