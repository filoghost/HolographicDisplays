/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.hologram.base;

import me.filoghost.fcommons.logging.Log;
import me.filoghost.holographicdisplays.api.hologram.TouchHandler;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.MustBeInvokedByOverriders;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.WeakHashMap;

public abstract class BaseTouchableLine extends BaseHologramLine {

    private static final Map<Player, Long> lastClickByPlayer = new WeakHashMap<>();

    private TouchHandler touchHandler;

    protected BaseTouchableLine(BaseHologram<?> hologram) {
        super(hologram);
    }

    public void onTouch(Player player) {
        if (touchHandler == null || !canInteract(player)) {
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
            Log.warning("The plugin " + getCreatorPlugin().getName() + " generated an exception"
                    + " when the player " + player.getName() + " touched a hologram.", t);
        }
    }

    public @Nullable TouchHandler getTouchHandler() {
        return touchHandler;
    }

    @MustBeInvokedByOverriders
    public void setTouchHandler(@Nullable TouchHandler touchHandler) {
        this.touchHandler = touchHandler;
        setChanged();
    }

}
