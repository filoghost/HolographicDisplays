/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.hologram.base;

import me.filoghost.fcommons.logging.Log;
import me.filoghost.holographicdisplays.api.hologram.ClickListener;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.MustBeInvokedByOverriders;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.WeakHashMap;

public abstract class BaseClickableLine extends BaseHologramLine {

    private static final Map<Player, Long> lastClickByPlayer = new WeakHashMap<>();

    private ClickListener clickListener;

    protected BaseClickableLine(BaseHologram hologram) {
        super(hologram);
    }

    public void onClick(Player player) {
        if (clickListener == null || !canInteract(player) || !isInClickRange(player)) {
            return;
        }

        Long lastClick = lastClickByPlayer.get(player);
        long now = System.currentTimeMillis();
        if (lastClick != null && now - lastClick < 100) {
            return;
        }

        lastClickByPlayer.put(player, now);

        try {
            clickListener.onClick(player);
        } catch (Throwable t) {
            Log.warning("The plugin " + getCreatorPlugin().getName() + " generated an exception"
                    + " when the player " + player.getName() + " clicked a hologram.", t);
        }
    }

    private boolean isInClickRange(Player player) {
        Location playerLocation = player.getLocation();
        BaseLinePosition position = this.getPosition();

        double xDiff = playerLocation.getX() - position.getX();
        double yDiff = playerLocation.getY() + 1.3 - position.getY(); // Use shoulder height
        double zDiff = playerLocation.getZ() - position.getZ();

        double distanceSquared = (xDiff * xDiff) + (yDiff * yDiff) + (zDiff * zDiff);
        return distanceSquared < 5 * 5;
    }

    public @Nullable ClickListener getClickListener() {
        return clickListener;
    }

    @MustBeInvokedByOverriders
    public void setClickListener(@Nullable ClickListener clickListener) {
        checkNotDeleted();

        this.clickListener = clickListener;
        setChanged();
    }

}
