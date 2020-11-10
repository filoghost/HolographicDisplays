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
    public String getText();
    
    @Deprecated
    public void setText(String text);
    
}
