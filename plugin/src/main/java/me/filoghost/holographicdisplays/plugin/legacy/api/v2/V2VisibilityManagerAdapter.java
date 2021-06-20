/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.legacy.api.v2;

import com.gmail.filoghost.holographicdisplays.api.VisibilityManager;
import me.filoghost.holographicdisplays.api.VisibilitySettings.Visibility;
import me.filoghost.holographicdisplays.plugin.object.api.DefaultVisibilitySettings;
import org.bukkit.entity.Player;

@SuppressWarnings("deprecation")
public class V2VisibilityManagerAdapter implements VisibilityManager {

    private final DefaultVisibilitySettings newVisibilitySettings;

    public V2VisibilityManagerAdapter(DefaultVisibilitySettings newVisibilitySettings) {
        this.newVisibilitySettings = newVisibilitySettings;
    }

    @Override
    public boolean isVisibleByDefault() {
        return newVisibilitySettings.getDefaultVisibility() == Visibility.VISIBLE;
    }

    @Override
    public void setVisibleByDefault(boolean visibleByDefault) {
        newVisibilitySettings.setDefaultVisibility(visibleByDefault ? Visibility.VISIBLE : Visibility.HIDDEN);
    }

    @Override
    public void showTo(Player player) {
        newVisibilitySettings.setIndividualVisibility(player, Visibility.VISIBLE);
    }

    @Override
    public void hideTo(Player player) {
        newVisibilitySettings.setIndividualVisibility(player, Visibility.HIDDEN);
    }

    @Override
    public boolean isVisibleTo(Player player) {
        return newVisibilitySettings.isVisibleTo(player);
    }

    @Override
    public void resetVisibility(Player player) {
        newVisibilitySettings.resetIndividualVisibility(player);
    }

    @Override
    public void resetVisibilityAll() {
        newVisibilitySettings.resetIndividualVisibilityAll();
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
        return this.newVisibilitySettings.equals(other.newVisibilitySettings);
    }

    @Override
    public final int hashCode() {
        return newVisibilitySettings.hashCode();
    }

    @Override
    public final String toString() {
        return newVisibilitySettings.toString();
    }

}
