/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package com.gmail.filoghost.holographicdisplays.api.line;

/**
 * @deprecated Please use the new API!
 */
@Deprecated
public interface TextLine extends TouchableLine {

    @Deprecated
    String getText();

    @Deprecated
    void setText(String text);

}
