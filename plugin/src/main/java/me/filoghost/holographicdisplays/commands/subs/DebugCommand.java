/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.commands.subs;

import me.filoghost.fcommons.command.sub.SubCommandContext;
import me.filoghost.holographicdisplays.Colors;
import me.filoghost.holographicdisplays.api.Hologram;
import me.filoghost.holographicdisplays.commands.HologramSubCommand;
import me.filoghost.holographicdisplays.nms.interfaces.NMSManager;
import me.filoghost.holographicdisplays.nms.interfaces.entity.NMSEntityBase;
import me.filoghost.holographicdisplays.object.api.APIHologram;
import me.filoghost.holographicdisplays.object.internal.InternalHologram;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class DebugCommand extends HologramSubCommand {

    private final NMSManager nmsManager;
    
    public DebugCommand(NMSManager nmsManager) {
        super("debug");
        setShowInHelpCommand(false);
        setDescription("Displays information useful for debugging.");
        
        this.nmsManager = nmsManager;
    }

    @Override
    public void execute(CommandSender sender, String[] args, SubCommandContext context) {
        boolean foundAnyHologram = false;
        
        for (World world : Bukkit.getWorlds()) {
            Map<Hologram, HologramDebugInfo> hologramsDebugInfo = new HashMap<>();
            
            for (Chunk chunk : world.getLoadedChunks()) {
                for (Entity entity : chunk.getEntities()) {
                    NMSEntityBase nmsEntity = nmsManager.getNMSEntityBase(entity);
                    
                    if (nmsEntity == null) {
                        continue;
                    }
                    
                    Hologram ownerHologram = nmsEntity.getHologramLine().getParent();
                    HologramDebugInfo hologramDebugInfo = hologramsDebugInfo.computeIfAbsent(ownerHologram, mapKey -> new HologramDebugInfo());
                    
                    if (nmsEntity.isDeadNMS()) {
                        hologramDebugInfo.deadEntities++;
                    } else {
                        hologramDebugInfo.aliveEntities++;
                    }
                }
            }
            
            if (!hologramsDebugInfo.isEmpty()) {
                foundAnyHologram = true;
                sender.sendMessage(Colors.PRIMARY + "Holograms in world '" + world.getName() + "':");
                
                for (Entry<Hologram, HologramDebugInfo> entry : hologramsDebugInfo.entrySet()) {
                    Hologram hologram = entry.getKey();
                    String displayName = getHologramDisplayName(hologram);
                    HologramDebugInfo debugInfo = entry.getValue();
                    sender.sendMessage(Colors.PRIMARY_SHADOW + "- '" + displayName + "': " + hologram.size() + " lines, "
                            + debugInfo.getTotalEntities() + " entities (" + debugInfo.aliveEntities + " alive, " + debugInfo.deadEntities + " dead)");
                }
            }
        }
        
        if (!foundAnyHologram) {
            sender.sendMessage(Colors.ERROR + "Couldn't find any loaded hologram (holograms may be in unloaded chunks).");
        }
        
    }

    private String getHologramDisplayName(Hologram hologram) {
        if (hologram instanceof InternalHologram) {
            return ((InternalHologram) hologram).getName();
        } else if (hologram instanceof APIHologram) {
            return ((APIHologram) hologram).getOwner().getName() + "@" + Integer.toHexString(hologram.hashCode());
        } else {
            return hologram.toString();
        }
    }
    
    private static class HologramDebugInfo {
        
        private int aliveEntities;
        private int deadEntities;
        
        public int getTotalEntities() {
            return aliveEntities + deadEntities;
        }
        
    }
    

}
