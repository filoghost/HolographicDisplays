/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.common.hologram;

import me.filoghost.holographicdisplays.common.nms.entity.NMSArmorStand;

public interface StandardTextLine extends StandardTouchableLine {

    String getText();

    boolean isAllowPlaceholders();

    NMSArmorStand getNMSArmorStand();

}
