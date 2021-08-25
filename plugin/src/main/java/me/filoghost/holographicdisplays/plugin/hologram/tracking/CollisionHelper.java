/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.hologram.tracking;

import me.filoghost.holographicdisplays.common.PositionCoordinates;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class CollisionHelper {

    private static final double PLAYER_WIDTH_RADIUS = 0.6 / 2;
    private static final double PLAYER_HEIGHT_RADIUS = 1.8 / 2;

    private static final double ITEM_WIDTH_RADIUS = 0.25 / 2;
    private static final double ITEM_HEIGHT_RADIUS = 0.25 / 2;

    // The server increases the player's bounding box in all directions before checking if it intersects with an item
    private static final double PLAYER_GROW_WIDTH = 1;
    private static final double PLAYER_GROW_HEIGHT = 0.5;

    private static final double REQUIRED_DISTANCE_X = PLAYER_WIDTH_RADIUS + PLAYER_GROW_WIDTH + ITEM_WIDTH_RADIUS;
    private static final double REQUIRED_DISTANCE_Z = REQUIRED_DISTANCE_X;
    private static final double REQUIRED_DISTANCE_Y = PLAYER_HEIGHT_RADIUS + PLAYER_GROW_HEIGHT + ITEM_HEIGHT_RADIUS;

    public static boolean isInPickupRange(Player player, PositionCoordinates itemPosition) {
        /*
         * Normally, the server determines if a player is picking up a dropped item
         * by checking if the bounding boxes of the player and the item intersect.
         *
         * This can be simplified: for each direction (X, Y, Z), check if the distance between the centers of the bounding boxes
         * is lower than the sum of the radius (half the length) of each box.
         *
         * If the condition is satisfied for all directions, the bounding boxes intersect. Visual example:
         *
         *     Box #1
         * ._____________.
         * |             |
         * |             |   Box #2
         * |      *  .___|_______________.
         * |         |   |               |
         * |_________|___|     *         |
         *           |                   |
         *           |___________________|
         *
         *        *------>
         *        Radius #1
         *
         *           <---------*
         *             Radius #2
         *
         *        *------------*
         *   Distance between centers
         *
         *
         * In the example above, the distance between the centers is lower than the sum of the two radii,
         * so the boxes collide in that direction.
         */
        Location playerLocation = player.getLocation();

        // The Y position is on lowest point of the bounding box, not in the center
        double playerCenterY = playerLocation.getY() + PLAYER_HEIGHT_RADIUS;
        double itemCenterY = itemPosition.getY() + ITEM_HEIGHT_RADIUS;

        double xDiff = Math.abs(playerLocation.getX() - itemPosition.getX());
        double zDiff = Math.abs(playerLocation.getZ() - itemPosition.getZ());
        double yDiff = Math.abs(playerCenterY - itemCenterY);

        return xDiff <= REQUIRED_DISTANCE_X
                && yDiff <= REQUIRED_DISTANCE_Y
                && zDiff <= REQUIRED_DISTANCE_Z;
    }

}
