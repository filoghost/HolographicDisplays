/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.hologram.internal;

import me.filoghost.holographicdisplays.common.hologram.StandardHologramLine;

public interface InternalHologramLine extends StandardHologramLine {

    String getSerializedConfigValue();

}
