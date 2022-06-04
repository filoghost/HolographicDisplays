/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.core.tracking;

import me.filoghost.holographicdisplays.common.PositionCoordinates;
import me.filoghost.holographicdisplays.core.base.BaseHologramLine;
import me.filoghost.holographicdisplays.core.tick.CachedPlayer;
import me.filoghost.holographicdisplays.core.tick.TickClock;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.MustBeInvokedByOverriders;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public abstract class LineTracker<T extends Viewer> {

    private static final int MODIFY_VIEWERS_INTERVAL_TICKS = 5;

    private final TickClock tickClock;
    private final Map<Player, T> viewers;
    private final Viewers<T> iterableViewers;

    protected PositionCoordinates position;
    private boolean positionChanged;

    /**
     * Flag to indicate that the line has changed in some way and there could be the need to send update packets.
     */
    private boolean lineChanged;

    protected LineTracker(TickClock tickClock) {
        this.tickClock = tickClock;
        this.viewers = new HashMap<>();
        this.iterableViewers = new DelegateViewers<>(viewers.values());
    }

    protected abstract BaseHologramLine getLine();

    final boolean shouldBeRemoved() {
        return getLine().isDeleted();
    }

    @MustBeInvokedByOverriders
    public void onRemoval() {
        resetViewersAndSendDestroyPackets();
    }

    public final void setLineChanged() {
        lineChanged = true;
    }

    @MustBeInvokedByOverriders
    protected void update(List<CachedPlayer> onlinePlayers) {
        boolean sendChangesPackets = false;

        // First, detect the changes if the flag is on and set it off
        if (lineChanged) {
            lineChanged = false;
            detectChanges();
            sendChangesPackets = true;
        }

        if (hasViewers()) {
            boolean textChanged = updatePlaceholders();
            if (textChanged) {
                sendChangesPackets = true;
            }
        }

        // Then, send the changes (if any) to already tracked players
        if (sendChangesPackets) {
            if (hasViewers()) {
                sendChangesPackets(iterableViewers);
            }
            clearDetectedChanges();
        }

        // Finally, add/remove tracked players sending them the full spawn/destroy packets
        modifyViewersAndSendPackets(onlinePlayers);
    }

    protected abstract boolean updatePlaceholders();

    private void modifyViewersAndSendPackets(List<CachedPlayer> onlinePlayers) {
        if (!getLine().isInLoadedChunk()) {
            resetViewersAndSendDestroyPackets();
            return;
        }

        // Add the identity hash code to avoid updating all the lines at the same time
        if ((tickClock.getCurrentTick() + hashCode()) % MODIFY_VIEWERS_INTERVAL_TICKS != 0) {
            return;
        }

        // Lazy initialization
        MutableViewers<T> addedPlayers = null;
        MutableViewers<T> removedPlayers = null;

        // Micro-optimization, don't use for-each loop to avoid creating a new Iterator (method called frequently)
        int size = onlinePlayers.size();
        for (int i = 0; i < size; i++) {
            CachedPlayer player = onlinePlayers.get(i);
            Player bukkitPlayer = player.getBukkitPlayer();
            if (shouldTrackPlayer(player)) {
                if (!viewers.containsKey(bukkitPlayer)) {
                    T viewer = createViewer(player);
                    viewers.put(bukkitPlayer, viewer);
                    if (addedPlayers == null) {
                        addedPlayers = new MutableViewers<>();
                    }
                    addedPlayers.add(viewer);
                }
            } else {
                if (viewers.containsKey(bukkitPlayer)) {
                    T viewer = viewers.remove(bukkitPlayer);
                    if (removedPlayers == null) {
                        removedPlayers = new MutableViewers<>();
                    }
                    removedPlayers.add(viewer);
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

    private boolean shouldTrackPlayer(CachedPlayer player) {
        Location playerLocation = player.getLocation();
        if (playerLocation.getWorld() != getLine().getWorldIfLoaded()) {
            return false;
        }

        double diffX = Math.abs(playerLocation.getX() - position.getX());
        double diffZ = Math.abs(playerLocation.getZ() - position.getZ());

        return diffX <= getViewRange()
                && diffZ <= getViewRange()
                && getLine().isVisibleTo(player.getBukkitPlayer());
    }

    protected abstract double getViewRange();

    protected abstract T createViewer(CachedPlayer cachedPlayer);

    protected final boolean hasViewers() {
        return !viewers.isEmpty();
    }

    protected final Collection<T> getViewers() {
        return viewers.values();
    }

    public final boolean isViewer(Player player) {
        return viewers.containsKey(player);
    }

    protected final void removeViewer(Player player) {
        viewers.remove(player);
    }

    @MustBeInvokedByOverriders
    protected void detectChanges() {
        PositionCoordinates position = getLine().getPosition();
        if (!Objects.equals(this.position, position)) {
            this.position = position;
            this.positionChanged = true;
        }
    }

    @MustBeInvokedByOverriders
    protected void clearDetectedChanges() {
        this.positionChanged = false;
    }

    protected final void resetViewersAndSendDestroyPackets() {
        if (!hasViewers()) {
            return;
        }

        sendDestroyPackets(iterableViewers);
        viewers.clear();
    }

    protected abstract void sendSpawnPackets(Viewers<T> viewers);

    protected abstract void sendDestroyPackets(Viewers<T> viewers);

    @MustBeInvokedByOverriders
    protected void sendChangesPackets(Viewers<T> viewers) {
        if (positionChanged) {
            sendPositionChangePackets(viewers);
        }
    }

    protected abstract void sendPositionChangePackets(Viewers<T> viewers);

}
