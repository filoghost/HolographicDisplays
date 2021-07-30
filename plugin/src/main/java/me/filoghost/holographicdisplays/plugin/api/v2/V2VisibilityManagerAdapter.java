/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.api.v2;

import com.gmail.filoghost.holographicdisplays.api.VisibilityManager;
import me.filoghost.holographicdisplays.api.hologram.VisibilitySettings.Visibility;
import me.filoghost.holographicdisplays.plugin.hologram.api.DefaultVisibilitySettings;
import org.bukkit.entity.Player;

@SuppressWarnings("deprecation")
public class V2VisibilityManagerAdapter implements VisibilityManager {

    private final DefaultVisibilitySettings v3VisibilitySettings;

    public V2VisibilityManagerAdapter(DefaultVisibilitySettings v3VisibilitySettings) {
        this.v3VisibilitySettings = v3VisibilitySettings;
    }

    @Override
    public boolean isVisibleByDefault() {
        return v3VisibilitySettings.getDefaultVisibility() == Visibility.VISIBLE;
    }

    @Override
    public void setVisibleByDefault(boolean visibleByDefault) {
        v3VisibilitySettings.setDefaultVisibility(visibleByDefault ? Visibility.VISIBLE : Visibility.HIDDEN);
    }

    @Override
    public void showTo(Player player) {
        v3VisibilitySettings.setIndividualVisibility(player, Visibility.VISIBLE);
    }

    @Override
    public void hideTo(Player player) {
        v3VisibilitySettings.setIndividualVisibility(player, Visibility.HIDDEN);
    }

    @Override
    public boolean isVisibleTo(Player player) {
        return v3VisibilitySettings.isVisibleTo(player);
    }

    @Override
    public void resetVisibility(Player player) {
        v3VisibilitySettings.resetIndividualVisibility(player);
    }

    @Override
    public void resetVisibilityAll() {
        v3VisibilitySettings.resetIndividualVisibilityAll();
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
        return this.v3VisibilitySettings.equals(other.v3VisibilitySettings);
    }

    @Override
    public final int hashCode() {
        return v3VisibilitySettings.hashCode();
    }

    @Override
    public final String toString() {
        return v3VisibilitySettings.toString();
    }

}
