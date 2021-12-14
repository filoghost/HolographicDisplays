/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.internal.placeholder;

import me.filoghost.holographicdisplays.api.beta.placeholder.IndividualPlaceholder;
import me.filoghost.holographicdisplays.api.beta.placeholder.IndividualPlaceholderFactory;
import me.filoghost.holographicdisplays.plugin.bridge.placeholderapi.PlaceholderAPIHook;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlaceholderAPIPlaceholderFactory implements IndividualPlaceholderFactory {

    @Override
    public @Nullable IndividualPlaceholder getPlaceholder(@Nullable String argument) {
        if (argument == null) {
            return null;
        }
        return new PlaceholderAPIPlaceholder("%" + argument + "%");
    }


    private static class PlaceholderAPIPlaceholder implements IndividualPlaceholder {

        private final String content;

        PlaceholderAPIPlaceholder(String content) {
            this.content = content;
        }

        @Override
        public int getRefreshIntervalTicks() {
            return 1;
        }

        @Override
        public @Nullable String getReplacement(@NotNull Player player, @Nullable String argument) {
            if (!PlaceholderAPIHook.isEnabled()) {
                return null;
            }
            return PlaceholderAPIHook.replacePlaceholders(player, content);
        }

    }

}
