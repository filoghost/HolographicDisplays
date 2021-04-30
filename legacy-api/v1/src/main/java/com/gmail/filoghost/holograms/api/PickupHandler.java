/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package com.gmail.filoghost.holograms.api;

import org.bukkit.entity.Player;

/**
 * @deprecated Please use the new API!
 */
@Deprecated
public interface PickupHandler {

    @Deprecated
    void onPickup(FloatingItem floatingItem, Player player);
    
}
