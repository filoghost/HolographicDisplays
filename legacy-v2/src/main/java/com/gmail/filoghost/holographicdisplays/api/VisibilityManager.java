/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package com.gmail.filoghost.holographicdisplays.api;

import org.bukkit.entity.Player;

/**
 * @deprecated Please use the new API!
 */
@Deprecated
public interface VisibilityManager {

    @Deprecated
    boolean isVisibleByDefault();
    
    @Deprecated
    void setVisibleByDefault(boolean visibleByDefault);
    
    @Deprecated
    void showTo(Player player);
    
    @Deprecated
    void hideTo(Player player);
    
    @Deprecated
    boolean isVisibleTo(Player player);
    
    @Deprecated
    void resetVisibility(Player player);
    
    @Deprecated
    void resetVisibilityAll();
    
}
