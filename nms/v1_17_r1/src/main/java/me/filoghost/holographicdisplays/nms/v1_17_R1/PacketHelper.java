/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.v1_17_R1;

import me.filoghost.fcommons.logging.Log;
import me.filoghost.holographicdisplays.common.nms.EntityID;
import me.filoghost.holographicdisplays.common.nms.NMSPacketList;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.ints.IntList;

import java.lang.reflect.Field;

class PacketHelper {

    private static final boolean USE_ENTITY_LIST_DESTROY_PACKET = useEntityListDestroyPacket();

    static void addDestroyPackets(NMSPacketList packetList, EntityID... entityIDs) {
        if (USE_ENTITY_LIST_DESTROY_PACKET) {
            packetList.add(new EntityListDestroyNMSPacket(entityIDs));
        } else {
            for (EntityID entityID : entityIDs) {
                packetList.add(new EntityDestroyNMSPacket(entityID));
            }
        }
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
