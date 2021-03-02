/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.object.internal;

import me.filoghost.holographicdisplays.core.hologram.StandardHologramLine;

public interface InternalHologramLine extends StandardHologramLine {
    
    String getSerializedConfigValue();

}
