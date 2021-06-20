/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.common.hologram;

import me.filoghost.holographicdisplays.common.nms.entity.NMSArmorStand;
import me.filoghost.holographicdisplays.common.nms.entity.NMSSlime;
import org.bukkit.entity.Player;

public interface StandardTouchableLine extends StandardHologramLine {

    void onTouch(Player player);

    NMSSlime getNMSSlime();

    NMSArmorStand getNMSSlimeVehicle();

}
