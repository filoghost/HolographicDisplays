/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.core.base;

import org.bukkit.entity.Player;

public interface ClickCallbackProvider {

    boolean hasClickCallback();

    void invokeClickCallback(Player player);

}
