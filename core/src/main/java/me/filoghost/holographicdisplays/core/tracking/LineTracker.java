/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.core.tracking;

import me.filoghost.holographicdisplays.common.PositionCoordinates;
import me.filoghost.holographicdisplays.core.base.BaseHologramLine;
import me.filoghost.holographicdisplays.core.tick.CachedPlayer;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.MustBeInvokedByOverriders;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public abstract class LineTracker<T extends Viewer> {

    private final Map<Player, T> viewers;
    private final Viewers<T> iterableViewers;

    private String positionWorldName;
    protected PositionCoordinates positionCoordinates;
    private boolean positionChanged;

    private boolean inLoadedChunk;
    private int lastVisibilitySettingsVersion;

    protected LineTracker() {
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

    @MustBeInvokedByOverriders
    protected void update(List<CachedPlayer> onlinePlayers, List<CachedPlayer> movedPlayers, int maxViewRange) {
        boolean sendChangesPackets = false;

        // First, detect the changes if the flag is on and set it off
        if (getLine().hasChanged()) {
            getLine().clearChanged();
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
        if (sendChangesPackets && hasViewers()) {
            sendChangesPackets(iterableViewers);
        }

        // Finally, add/remove viewers sending them the full spawn/destroy packets
        modifyViewersAndSendPackets(onlinePlayers, movedPlayers, maxViewRange);

        if (sendChangesPackets) {
            clearDetectedChanges();
        }
    }

    protected abstract boolean updatePlaceholders();

    private void modifyViewersAndSendPackets(List<CachedPlayer> onlinePlayers, List<CachedPlayer> movedPlayers, int maxViewRange) {
        if (!getLine().isInLoadedChunk()) {
            if (inLoadedChunk) {
                inLoadedChunk = false;
                resetViewersAndSendDestroyPackets();
            }
            return;
        }

        boolean checkAllPlayers = false;

        if (!inLoadedChunk) {
            // The chunk was just loaded, check all players
            inLoadedChunk = true;
            checkAllPlayers = true;
        }

        int visibilitySettingsVersion = getLine().getVisibilitySettings().getVersion();
        if (visibilitySettingsVersion != lastVisibilitySettingsVersion) {
            lastVisibilitySettingsVersion = visibilitySettingsVersion;
            checkAllPlayers = true;
        }

        if (positionChanged) {
            checkAllPlayers = true;
        }

        List<CachedPlayer> playersToCheck;
        if (checkAllPlayers) {
            playersToCheck = onlinePlayers;
        } else {
            playersToCheck = movedPlayers;
        }

        // Lazy initialization
        MutableViewers<T> addedPlayers = null;
        MutableViewers<T> removedPlayers = null;

        // Micro-optimization, don't use for-each loop to avoid creating a new Iterator (method called frequently)
        int size = playersToCheck.size();
        for (int i = 0; i < size; i++) {
            CachedPlayer player = playersToCheck.get(i);
            Player bukkitPlayer = player.getBukkitPlayer();
            if (shouldTrackPlayer(player, maxViewRange)) {
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

    private boolean shouldTrackPlayer(CachedPlayer player, int maxViewRange) {
        Location playerLocation = player.getLocation();
        if (playerLocation == null || playerLocation.getWorld() != getLine().getWorldIfLoaded()) {
            return false;
        }

        double viewRange = getViewRange();
        if (viewRange > maxViewRange) {
            viewRange = maxViewRange;
        }

        double diffX = Math.abs(playerLocation.getX() - positionCoordinates.getX());
        double diffZ = Math.abs(playerLocation.getZ() - positionCoordinates.getZ());

        return diffX <= viewRange
                && diffZ <= viewRange
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

    protected final boolean isViewer(Player player) {
        return viewers.containsKey(player);
    }

    protected final void removeViewer(Player player) {
        viewers.remove(player);
    }

    protected boolean canInteract(Player player) {
        return !getLine().isDeleted()
                && player.isOnline()
                && player.getGameMode() != GameMode.SPECTATOR
                && isViewer(player)
                && getLine().isVisibleTo(player);
    }

    @MustBeInvokedByOverriders
    protected void detectChanges() {
        PositionCoordinates positionCoordinates = getLine().getCoordinates();
        if (!Objects.equals(this.positionCoordinates, positionCoordinates)) {
            this.positionCoordinates = positionCoordinates;
            this.positionChanged = true;
        }

        String positionWorldName = getLine().getWorldName();
        if (!Objects.equals(this.positionWorldName, positionWorldName)) {
            this.positionWorldName = positionWorldName;
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
