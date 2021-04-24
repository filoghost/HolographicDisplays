/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.core.hologram;

import me.filoghost.holographicdisplays.core.nms.entity.NMSArmorStand;
import me.filoghost.holographicdisplays.core.placeholder.RelativePlaceholder;

import java.util.Collection;

public interface StandardTextLine extends StandardTouchableLine {

    String getText();

    boolean isAllowPlaceholders();

    Collection<RelativePlaceholder> getRelativePlaceholders();

    NMSArmorStand getNMSArmorStand();

}
