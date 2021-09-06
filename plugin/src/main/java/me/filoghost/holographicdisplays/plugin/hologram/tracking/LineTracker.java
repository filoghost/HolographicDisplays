/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.hologram.tracking;

import me.filoghost.holographicdisplays.plugin.hologram.base.BaseHologramLine;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.MustBeInvokedByOverriders;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class LineTracker<T extends TrackedPlayer> {

    private final Map<Player, T> trackedPlayers;
    private final Viewers<T> trackedPlayersIterableView;

    /**
     * Flag to indicate that the line has changed in some way and there could be the need to send update packets.
     */
    private boolean lineChanged;

    protected LineTracker() {
        this.trackedPlayers = new HashMap<>();
        this.trackedPlayersIterableView = action -> trackedPlayers.values().forEach(action);
    }

    protected abstract BaseHologramLine getLine();

    final boolean shouldBeRemoved() {
        return getLine().isDeleted();
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
                sendChangesPackets(trackedPlayersIterableView);
            }
            clearDetectedChanges();
        }

        // Finally, add/remove tracked players sending them the full spawn/destroy packets
        modifyTrackedPlayersAndSendPackets(onlinePlayers);
    }

    protected abstract void detectChanges();

    protected abstract void clearDetectedChanges();

    protected abstract boolean updatePlaceholders();

    private void modifyTrackedPlayersAndSendPackets(Collection<? extends Player> onlinePlayers) {
        if (!getLine().isInLoadedChunk()) {
            clearTrackedPlayersAndSendPackets();
            return;
        }

        // Lazy initialization
        MutableViewers<T> addedPlayers = null;
        MutableViewers<T> removedPlayers = null;

        for (Player player : onlinePlayers) {
            if (shouldTrackPlayer(player)) {
                if (!trackedPlayers.containsKey(player)) {
                    T trackedPlayer = createTrackedPlayer(player);
                    trackedPlayers.put(player, trackedPlayer);
                    if (addedPlayers == null) {
                        addedPlayers = new MutableViewers<>();
                    }
                    addedPlayers.add(trackedPlayer);
                }
            } else {
                if (trackedPlayers.containsKey(player)) {
                    T trackedPlayer = trackedPlayers.remove(player);
                    if (removedPlayers == null) {
                        removedPlayers = new MutableViewers<>();
                    }
                    removedPlayers.add(trackedPlayer);
                }
            }
        }

        if (addedPlayers != null) {
            sendSpawnPackets(addedPlayers);
        }
        if (removedPlayers != null) {
            sendDestroyPackets(removedPlayers);
        }
    }

    protected abstract T createTrackedPlayer(Player player);

    protected abstract boolean shouldTrackPlayer(Player player);

    protected final boolean hasTrackedPlayers() {
        return !trackedPlayers.isEmpty();
    }

    protected final Collection<T> getTrackedPlayers() {
        return trackedPlayers.values();
    }

    public final boolean isTrackedPlayer(Player player) {
        return trackedPlayers.containsKey(player);
    }

    protected final void removeTrackedPlayer(Player player) {
        trackedPlayers.remove(player);
    }

    protected final void clearTrackedPlayersAndSendPackets() {
        if (!hasTrackedPlayers()) {
            return;
        }

        sendDestroyPackets(trackedPlayersIterableView);
        trackedPlayers.clear();
    }

    protected abstract void sendSpawnPackets(Viewers<T> viewers);

    protected abstract void sendDestroyPackets(Viewers<T> viewers);

    protected abstract void sendChangesPackets(Viewers<T> viewers);

}
