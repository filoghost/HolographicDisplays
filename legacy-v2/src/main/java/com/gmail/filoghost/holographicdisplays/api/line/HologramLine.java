/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package com.gmail.filoghost.holographicdisplays.api.line;

import com.gmail.filoghost.holographicdisplays.api.Hologram;

/**
 * @deprecated Please use the new API!
 */
@Deprecated
public interface HologramLine {

    @Deprecated
    public Hologram getParent();
    
    @Deprecated
    public void removeLine();

}
