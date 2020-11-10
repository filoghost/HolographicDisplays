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
    
    public boolean hook(Plugin plugin, NMSManager nmsManager);
    
    public void sendDestroyEntitiesPacket(Player player, CraftHologram hologram);
    
    public void sendDestroyEntitiesPacket(Player player, CraftHologramLine line);
    
    public void sendCreateEntitiesPacket(Player player, CraftHologram hologram);
    
    public void sendCreateEntitiesPacket(Player player, CraftHologramLine line);

}
