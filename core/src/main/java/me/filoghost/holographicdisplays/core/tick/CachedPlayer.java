/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.core.tick;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class CachedPlayer {

    private final Player player;

    private Location location;

    public CachedPlayer(Player player) {
        this.player = player;
    }

    boolean onTick() {
        Location newLocation = player.getLocation();
        boolean moved = isDifferentPosition(location, newLocation);
        location = newLocation;
        return moved;
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

    public Location getLocation() {
        return location;
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
