/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.core.tick;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class CachedPlayer {

    private final Player player;

    private Location location;
    private boolean movedLastTick;

    public CachedPlayer(Player player) {
        this.player = player;
    }

    void onTick() {
        Location newLocation = player.getLocation();
        movedLastTick = isDifferentPosition(location, newLocation);
        location = newLocation;
    }

    private boolean isDifferentPosition(Location oldLocation, Location newLocation) {
        if (oldLocation == null) {
            return true;
        }

        return newLocation.getWorld() != oldLocation.getWorld()
                || newLocation.getX() != oldLocation.getX()
                || newLocation.getY() != oldLocation.getY()
                || newLocation.getZ() != oldLocation.getZ();
    }

    public Player getBukkitPlayer() {
        return player;
    }

    public @Nullable Location getLocation() {
        return location;
    }

    public boolean isMovedLastTick() {
        return movedLastTick;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        CachedPlayer other = (CachedPlayer) obj;
        return player.equals(other.player);
    }

    @Override
    public int hashCode() {
        return player.hashCode();
    }

}
