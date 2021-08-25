/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.internal.hologram;

import me.filoghost.holographicdisplays.plugin.hologram.base.EditableHologramLine;

public interface InternalHologramLine extends EditableHologramLine {

    String getSerializedConfigValue();

}
