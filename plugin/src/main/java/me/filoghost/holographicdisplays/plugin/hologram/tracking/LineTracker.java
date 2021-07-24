/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.hologram.tracking;

import me.filoghost.holographicdisplays.common.hologram.StandardHologramLine;
import me.filoghost.holographicdisplays.common.nms.NMSManager;
import me.filoghost.holographicdisplays.common.nms.NMSPacketList;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public abstract class LineTracker<T extends StandardHologramLine> {

    protected final T line;
    private final Set<Player> trackedPlayers;
    private final NMSManager nmsManager;

    /**
     * Flag to indicate that the line has changed in some way and there could be the need to send update packets.
     */
    private boolean lineChanged;

    LineTracker(T line, NMSManager nmsManager) {
        this.line = line;
        this.trackedPlayers = new HashSet<>();
        this.nmsManager = nmsManager;
    }

    public final void setLineChanged() {
        lineChanged = true;
    }

    final void updateAndSendChanges(Collection<? extends Player> onlinePlayers) {
        boolean sendChangesPackets = false;

        // First, detect the changes if the flag is on and set it off
        if (lineChanged) {
            lineChanged = false;
            detectChanges();
            sendChangesPackets = true;
        }

        if (updatePlaceholders()) {
            sendChangesPackets = true;
        }

        // Then, send the changes (if any) to already tracked players
        if (sendChangesPackets) {
            if (hasTrackedPlayers()) {
                NMSPacketList packetList = nmsManager.createPacketList();
                addChangesPackets(packetList);
                broadcastPackets(packetList);
            }
            clearDetectedChanges();
        }

        // Finally, add/remove tracked players sending them the full spawn/destroy packets
        updateTrackedPlayers(onlinePlayers);
    }

    protected abstract void detectChanges();

    protected abstract void clearDetectedChanges();

    protected abstract boolean updatePlaceholders();

    private void updateTrackedPlayers(Collection<? extends Player> onlinePlayers) {
        if (!isActive()) {
            clearTrackedPlayers();
            return;
        }

        // Lazy initialization
        NMSPacketList spawnPacketList = null;
        NMSPacketList destroyPacketList = null;

        for (Player player : onlinePlayers) {
            if (shouldTrackPlayer(player)) {
                if (trackedPlayers.add(player)) {
                    if (spawnPacketList == null) {
                        spawnPacketList = nmsManager.createPacketList();
                        addSpawnPackets(spawnPacketList);
                    }
                    spawnPacketList.sendTo(player);
                }
            } else {
                if (trackedPlayers.remove(player)) {
                    if (destroyPacketList == null) {
                        destroyPacketList = nmsManager.createPacketList();
                        addDestroyPackets(destroyPacketList);
                    }
                    destroyPacketList.sendTo(player);
                }
            }
        }
    }

    final boolean isDeleted() {
        return line.isDeleted() || line.getHologram().isDeleted();
    }

    protected abstract boolean isActive();

    protected abstract boolean shouldTrackPlayer(Player player);

    private boolean hasTrackedPlayers() {
        return !trackedPlayers.isEmpty();
    }

    protected final void removeTrackedPlayer(Player player) {
        trackedPlayers.remove(player);
    }

    protected final void clearTrackedPlayers() {
        if (!hasTrackedPlayers()) {
            return;
        }

        NMSPacketList destroyPacketList = nmsManager.createPacketList();
        addDestroyPackets(destroyPacketList);
        broadcastPackets(destroyPacketList);
        trackedPlayers.clear();
    }

    private void broadcastPackets(NMSPacketList packetList) {
        for (Player trackedPlayer : trackedPlayers) {
            packetList.sendTo(trackedPlayer);
        }
    }

    protected abstract void addSpawnPackets(NMSPacketList packetList);

    protected abstract void addDestroyPackets(NMSPacketList packetList);

    protected abstract void addChangesPackets(NMSPacketList packetList);

    protected abstract void onChunkLoad(Chunk chunk);

    protected abstract void onChunkUnload(Chunk chunk);

}
