/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.hologram.tracking;

import me.filoghost.holographicdisplays.common.nms.NMSManager;
import me.filoghost.holographicdisplays.common.nms.NMSPacketList;
import me.filoghost.holographicdisplays.plugin.hologram.base.BaseHologramLine;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.MustBeInvokedByOverriders;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public abstract class LineTracker<T extends BaseHologramLine> {

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

    final boolean shouldBeRemoved() {
        return line.isDeleted();
    }

    @MustBeInvokedByOverriders
    public void onRemoval() {
        clearTrackedPlayersAndSendPackets();
    }

    public final void setLineChanged() {
        lineChanged = true;
    }

    @MustBeInvokedByOverriders
    protected void update(Collection<? extends Player> onlinePlayers) {
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
        updateTrackedPlayersAndSendPackets(onlinePlayers);
    }

    protected abstract void detectChanges();

    protected abstract void clearDetectedChanges();

    protected abstract boolean updatePlaceholders();

    private void updateTrackedPlayersAndSendPackets(Collection<? extends Player> onlinePlayers) {
        if (!line.isInLoadedChunk()) {
            clearTrackedPlayersAndSendPackets();
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

    protected abstract boolean shouldTrackPlayer(Player player);

    protected final boolean hasTrackedPlayers() {
        return !trackedPlayers.isEmpty();
    }

    protected final Set<Player> getTrackedPlayers() {
        return trackedPlayers;
    }

    public final boolean isTrackedPlayer(Player player) {
        return trackedPlayers.contains(player);
    }

    protected final void removeTrackedPlayer(Player player) {
        trackedPlayers.remove(player);
    }

    protected final void clearTrackedPlayersAndSendPackets() {
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

}
