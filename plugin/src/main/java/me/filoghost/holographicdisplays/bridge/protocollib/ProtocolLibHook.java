/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.bridge.protocollib;

import me.filoghost.holographicdisplays.nms.interfaces.NMSManager;
import me.filoghost.holographicdisplays.object.CraftHologram;
import me.filoghost.holographicdisplays.object.line.CraftHologramLine;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public interface ProtocolLibHook {
    
    boolean hook(Plugin plugin, NMSManager nmsManager);
    
    void sendDestroyEntitiesPacket(Player player, CraftHologram hologram);
    
    void sendCreateEntitiesPacket(Player player, CraftHologram hologram);

}
