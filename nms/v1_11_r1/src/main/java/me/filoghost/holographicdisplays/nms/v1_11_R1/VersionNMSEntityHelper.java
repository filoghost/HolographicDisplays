/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.v1_11_R1;

import me.filoghost.holographicdisplays.core.nms.entity.NMSEntityHelper;
import net.minecraft.server.v1_11_R1.Entity;
import net.minecraft.server.v1_11_R1.EntityTrackerEntry;
import net.minecraft.server.v1_11_R1.Packet;
import net.minecraft.server.v1_11_R1.WorldServer;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class VersionNMSEntityHelper extends NMSEntityHelper<EntityTrackerEntry> {

    private final Entity entity;

    public VersionNMSEntityHelper(Entity entity) {
        this.entity = entity;
    }
    
    @Override
    protected EntityTrackerEntry getTracker0() {
        return ((WorldServer) entity.world).tracker.trackedEntities.get(entity.getId());
    }

    @Override
    public boolean isTrackedBy(Player bukkitPlayer) {
        EntityTrackerEntry tracker = getTracker();
        if (tracker != null) {
            return tracker.trackedPlayers.contains(((CraftPlayer) bukkitPlayer).getHandle());
        } else {
            return false;
        }
    }

    public void broadcastPacket(Packet<?> packet) {
        EntityTrackerEntry tracker = getTracker();
        if (tracker != null) {
            tracker.broadcast(packet);
        }
    }

}
