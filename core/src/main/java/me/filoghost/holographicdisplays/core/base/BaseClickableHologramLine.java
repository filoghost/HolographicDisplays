/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.core.base;

import me.filoghost.fcommons.logging.Log;
import me.filoghost.holographicdisplays.common.PositionCoordinates;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public abstract class BaseClickableHologramLine extends BaseHologramLine {

    protected BaseClickableHologramLine(BaseHologram hologram) {
        super(hologram);
    }

    public void onClick(Player player) {
        if (hasClickCallback() && canInteract(player) && isInClickRange(player)) {
            try {
                invokeClickCallback(player);
            } catch (Throwable t) {
                Log.warning("The plugin " + getCreatorPlugin().getName() + " generated an exception"
                        + " when the player " + player.getName() + " clicked a hologram.", t);
            }
        }
    }

    public abstract boolean hasClickCallback();

    protected abstract void invokeClickCallback(Player player);

    private boolean isInClickRange(Player player) {
        Location playerLocation = player.getLocation();
        PositionCoordinates positionCoordinates = this.getCoordinates();

        double xDiff = playerLocation.getX() - positionCoordinates.getX();
        double yDiff = playerLocation.getY() + 1.3 - positionCoordinates.getY(); // Use shoulder height
        double zDiff = playerLocation.getZ() - positionCoordinates.getZ();

        double distanceSquared = (xDiff * xDiff) + (yDiff * yDiff) + (zDiff * zDiff);
        return distanceSquared < 5 * 5;
    }

}
