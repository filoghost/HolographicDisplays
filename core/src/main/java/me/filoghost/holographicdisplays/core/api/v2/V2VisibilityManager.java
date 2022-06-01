/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.core.api.v2;

import com.gmail.filoghost.holographicdisplays.api.VisibilityManager;
import me.filoghost.holographicdisplays.api.beta.hologram.VisibilitySettings;
import me.filoghost.holographicdisplays.api.beta.hologram.VisibilitySettings.Visibility;
import me.filoghost.holographicdisplays.core.api.current.DefaultVisibilitySettings;
import org.bukkit.entity.Player;

@SuppressWarnings("deprecation")
class V2VisibilityManager implements VisibilityManager {

    private final VisibilitySettings v3VisibilitySettings;

    V2VisibilityManager(VisibilitySettings visibilitySettings) {
        this.v3VisibilitySettings = new DefaultVisibilitySettings();
    }

    @Override
    public boolean isVisibleByDefault() {
        return v3VisibilitySettings.getGlobalVisibility() == Visibility.VISIBLE;
    }

    @Override
    public void setVisibleByDefault(boolean visibleByDefault) {
        v3VisibilitySettings.setGlobalVisibility(visibleByDefault ? Visibility.VISIBLE : Visibility.HIDDEN);
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
        v3VisibilitySettings.removeIndividualVisibility(player);
    }

    @Override
    public void resetVisibilityAll() {
        v3VisibilitySettings.clearIndividualVisibilities();
    }

}
