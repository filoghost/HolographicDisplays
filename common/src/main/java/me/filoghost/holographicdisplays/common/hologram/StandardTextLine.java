/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.common.hologram;

public interface StandardTextLine extends StandardTouchableLine {

    String getText();

    boolean isAllowPlaceholders();

}
