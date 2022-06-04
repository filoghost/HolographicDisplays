/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.api.placeholder;

import org.jetbrains.annotations.Nullable;

/**
 * @since 1
 */
public interface IndividualPlaceholderFactory {

    /**
     * @since 1
     */
    @Nullable IndividualPlaceholder getPlaceholder(@Nullable String argument);

}
