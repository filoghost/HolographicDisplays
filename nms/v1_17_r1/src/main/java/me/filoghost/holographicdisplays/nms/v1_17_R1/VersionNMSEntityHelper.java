/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.v1_17_R1;

import me.filoghost.holographicdisplays.common.nms.entity.NMSEntityHelper;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.PlayerChunkMap.EntityTracker;
import net.minecraft.server.level.WorldServer;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.world.entity.Entity;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class VersionNMSEntityHelper extends NMSEntityHelper<EntityTracker> {

    private final Entity entity;

    public VersionNMSEntityHelper(Entity entity) {
        this.entity = entity;
    }

    @Override
    protected EntityTracker getTracker0() {
        return ((WorldServer) entity.getWorld()).getChunkProvider().a /* playerChunkMap */.G /* trackedEntities */.get(entity.getId());
    }

    @Override
    public boolean isTrackedBy(Player bukkitPlayer) {
        EntityTracker tracker = getTracker();
        if (tracker != null) {
            PlayerConnection playerConnection = ((CraftPlayer) bukkitPlayer).getHandle().b /* playerConnection */;
            return tracker.f /* trackedPlayerConnections */.contains(playerConnection);
        } else {
            return false;
        }
    }

    public void broadcastPacket(Packet<?> packet) {
        EntityTracker tracker = getTracker();
        if (tracker != null) {
            tracker.broadcast(packet);
        }
    }

}
