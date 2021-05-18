/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.api.line;

import org.jetbrains.annotations.Nullable;

/**
 * @since 1
 */
public interface TextLine extends TouchableLine {

    /**
     * Returns the current text of this TextLine.
     *
     * @return the current text of this line.
     * @since 1
     */
    @Nullable String getText();
    
    /**
     * Sets the text of this TextLine.
     *
     * @param text the new text of this line.
     * @since 1
     */
    void setText(@Nullable String text);
    
}
