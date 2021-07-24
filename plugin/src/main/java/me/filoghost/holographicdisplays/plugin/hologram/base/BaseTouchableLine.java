/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.hologram.base;

import me.filoghost.fcommons.logging.Log;
import me.filoghost.holographicdisplays.api.hologram.TouchHandler;
import me.filoghost.holographicdisplays.common.hologram.StandardTouchableLine;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.MustBeInvokedByOverriders;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * Useful class that implements StandardTouchableLine. The downside is that subclasses must extend this, and cannot extend other classes.
 * But all the current items are touchable.
 */
public abstract class BaseTouchableLine extends BaseHologramLine implements StandardTouchableLine {

    private static final Map<Player, Long> lastClickByPlayer = new WeakHashMap<>();

    private TouchHandler touchHandler;

    protected BaseTouchableLine(BaseHologram<?> hologram) {
        super(hologram);
    }

    @Override
    public void onTouch(Player player) {
        if (player.getGameMode() == GameMode.SPECTATOR) {
            return;
        }

        if (touchHandler == null || !getHologram().isVisibleTo(player)) {
            return;
        }

        Long lastClick = lastClickByPlayer.get(player);
        long now = System.currentTimeMillis();
        if (lastClick != null && now - lastClick < 100) {
            return;
        }

        lastClickByPlayer.put(player, now);

        try {
            touchHandler.onTouch(player);
        } catch (Throwable t) {
            Log.warning("The plugin " + getHologram().getCreatorPlugin().getName() + " generated an exception"
                    + " when the player " + player.getName() + " touched a hologram.", t);
        }
    }

    @Override
    public boolean hasTouchHandler() {
        return touchHandler != null;
    }

    @MustBeInvokedByOverriders
    public void setTouchHandler(@Nullable TouchHandler touchHandler) {
        this.touchHandler = touchHandler;
        setChanged();
    }

    public @Nullable TouchHandler getTouchHandler() {
        return touchHandler;
    }

}
