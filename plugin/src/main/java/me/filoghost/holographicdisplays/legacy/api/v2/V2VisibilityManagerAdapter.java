/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.legacy.api.v2;

import com.gmail.filoghost.holographicdisplays.api.VisibilityManager;
import me.filoghost.holographicdisplays.object.api.DefaultVisibilityManager;
import org.bukkit.entity.Player;

@SuppressWarnings("deprecation")
public class V2VisibilityManagerAdapter implements VisibilityManager {

    private final DefaultVisibilityManager newVisibilityManager;

    public V2VisibilityManagerAdapter(DefaultVisibilityManager newVisibilityManager) {
        this.newVisibilityManager = newVisibilityManager;
    }

    @Override
    public boolean isVisibleByDefault() {
        return newVisibilityManager.isVisibleByDefault();
    }

    @Override
    public void setVisibleByDefault(boolean visibleByDefault) {
        newVisibilityManager.setVisibleByDefault(visibleByDefault);
    }

    @Override
    public void showTo(Player player) {
        newVisibilityManager.showTo(player);
    }

    @Override
    public void hideTo(Player player) {
        newVisibilityManager.hideTo(player);
    }

    @Override
    public boolean isVisibleTo(Player player) {
        return newVisibilityManager.isVisibleTo(player);
    }

    @Override
    public void resetVisibility(Player player) {
        newVisibilityManager.resetVisibility(player);
    }

    @Override
    public void resetVisibilityAll() {
        newVisibilityManager.resetVisibilityAll();
    }

    @Override
    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof V2VisibilityManagerAdapter)) {
            return false;
        }

        V2VisibilityManagerAdapter other = (V2VisibilityManagerAdapter) obj;
        return this.newVisibilityManager.equals(other.newVisibilityManager);
    }

    @Override
    public final int hashCode() {
        return newVisibilityManager.hashCode();
    }

    @Override
    public final String toString() {
        return newVisibilityManager.toString();
    }

}
