/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.core.hologram;

import me.filoghost.holographicdisplays.core.nms.entity.NMSArmorStand;
import me.filoghost.holographicdisplays.core.nms.entity.NMSSlime;
import org.bukkit.entity.Player;

public interface StandardTouchableLine extends StandardHologramLine {

    void onTouch(Player player);

    NMSSlime getNMSSlime();

    NMSArmorStand getNMSSlimeVehicle();

}
