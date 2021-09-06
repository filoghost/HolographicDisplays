/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.v1_17_R1;

import me.filoghost.fcommons.logging.Log;
import me.filoghost.holographicdisplays.nms.common.EntityID;
import me.filoghost.holographicdisplays.nms.common.PacketGroup;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.ints.IntList;

import java.lang.reflect.Field;

class PacketHelper {

    private static final boolean USE_ENTITY_LIST_DESTROY_PACKET = useEntityListDestroyPacket();

    static PacketGroup newDestroyPackets(EntityID entityID) {
        if (USE_ENTITY_LIST_DESTROY_PACKET) {
            return new EntityListDestroyNMSPacket(entityID);
        } else {
            return new EntityDestroyNMSPacket(entityID);
        }
    }

    static PacketGroup newDestroyPackets(EntityID entityID1, EntityID entityID2) {
        if (USE_ENTITY_LIST_DESTROY_PACKET) {
            return new EntityListDestroyNMSPacket(entityID1, entityID2);
        } else {
            return PacketGroup.of(
                    new EntityDestroyNMSPacket(entityID1),
                    new EntityDestroyNMSPacket(entityID2));
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
