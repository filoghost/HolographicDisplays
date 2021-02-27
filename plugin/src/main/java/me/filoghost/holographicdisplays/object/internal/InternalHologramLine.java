/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.object.internal;

import me.filoghost.holographicdisplays.core.object.base.SpawnableHologramLine;

public interface InternalHologramLine extends SpawnableHologramLine {
    
    String getSerializedConfigValue();

}
