/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.core.base;

import me.filoghost.fcommons.logging.Log;
import org.bukkit.entity.Player;

public abstract class BaseClickableHologramLine extends BaseHologramLine {

    protected BaseClickableHologramLine(BaseHologram hologram) {
        super(hologram);
    }

    public void onClick(Player player) {
        try {
            invokeExternalClickCallback(player);
        } catch (Throwable t) {
            Log.warning("The plugin " + getCreatorPlugin().getName() + " generated an exception"
                    + " when the player " + player.getName() + " clicked a hologram.", t);
        }
    }

    public abstract boolean hasClickCallback();

    protected abstract void invokeExternalClickCallback(Player player);

}
