/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.api.beta.placeholder;

import org.jetbrains.annotations.Nullable;

/**
 * @since 1
 */
public interface GlobalPlaceholderFactory {

    /**
     * @since 1
     */
    @Nullable GlobalPlaceholder getPlaceholder(@Nullable String argument);

}
