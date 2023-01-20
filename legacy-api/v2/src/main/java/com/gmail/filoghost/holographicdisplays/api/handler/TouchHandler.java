/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package com.gmail.filoghost.holographicdisplays.api.handler;

import me.filoghost.holographicdisplays.api.hologram.line.HologramClickType;
import org.bukkit.entity.Player;

/**
 * @deprecated Please use the new API!
 */
@Deprecated
public interface TouchHandler {

    @Deprecated
    void onTouch(Player player, HologramClickType clickType);

}
