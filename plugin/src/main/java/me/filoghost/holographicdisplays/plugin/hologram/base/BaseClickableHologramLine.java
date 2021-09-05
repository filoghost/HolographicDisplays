/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.hologram.base;

import me.filoghost.holographicdisplays.common.PositionCoordinates;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public abstract class BaseClickableHologramLine extends BaseHologramLine implements ClickCallbackProvider {

    protected BaseClickableHologramLine(BaseHologram hologram) {
        super(hologram);
    }

    public void onClick(Player player) {
        if (hasClickCallback() && canInteract(player) && isInClickRange(player)) {
            invokeClickCallback(player);
        }
    }

    private boolean isInClickRange(Player player) {
        Location playerLocation = player.getLocation();
        PositionCoordinates position = this.getPosition();

        double xDiff = playerLocation.getX() - position.getX();
        double yDiff = playerLocation.getY() + 1.3 - position.getY(); // Use shoulder height
        double zDiff = playerLocation.getZ() - position.getZ();

        double distanceSquared = (xDiff * xDiff) + (yDiff * yDiff) + (zDiff * zDiff);
        return distanceSquared < 5 * 5;
    }

}
