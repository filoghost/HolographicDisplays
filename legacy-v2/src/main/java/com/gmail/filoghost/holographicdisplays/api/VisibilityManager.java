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
    public boolean isVisibleByDefault();
    
    @Deprecated
    public void setVisibleByDefault(boolean visibleByDefault);
    
    @Deprecated
    public void showTo(Player player);
    
    @Deprecated
    public void hideTo(Player player);
    
    @Deprecated
    public boolean isVisibleTo(Player player);
    
    @Deprecated
    public void resetVisibility(Player player);
    
    @Deprecated
    public void resetVisibilityAll();
    
}
