/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.commands.main.subs;

import me.filoghost.holographicdisplays.HolographicDisplays;
import me.filoghost.holographicdisplays.api.Hologram;
import me.filoghost.holographicdisplays.commands.Colors;
import me.filoghost.holographicdisplays.commands.Strings;
import me.filoghost.holographicdisplays.commands.main.HologramSubCommand;
import me.filoghost.holographicdisplays.exception.CommandException;
import me.filoghost.holographicdisplays.nms.interfaces.entity.NMSEntityBase;
import me.filoghost.holographicdisplays.object.NamedHologram;
import me.filoghost.holographicdisplays.object.PluginHologram;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class DebugCommand extends HologramSubCommand {

    public DebugCommand() {
        super("debug");
        setPermission(Strings.BASE_PERM + "debug");
    }

    @Override
    public String getPossibleArguments() {
        return "";
    }

    @Override
    public int getMinimumArguments() {
        return 0;
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) throws CommandException {
        boolean foundAnyHologram = false;
        
        for (World world : Bukkit.getWorlds()) {
            Map<Hologram, HologramDebugInfo> hologramsDebugInfo = new HashMap<>();
            
            for (Chunk chunk : world.getLoadedChunks()) {
                for (Entity entity : chunk.getEntities()) {
                    NMSEntityBase nmsEntity = HolographicDisplays.getNMSManager().getNMSEntityBase(entity);
                    
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
        if (hologram instanceof NamedHologram) {
            return ((NamedHologram) hologram).getName();
        } else if (hologram instanceof PluginHologram) {
            return ((PluginHologram) hologram).getOwner().getName() + "@" + Integer.toHexString(hologram.hashCode());
        } else {
            return hologram.toString();
        }
    }

    @Override
    public List<String> getTutorial() {
        return Arrays.asList("Displays information useful for debugging.");
    }
    
    @Override
    public SubCommandType getType() {
        return SubCommandType.HIDDEN;
    }
    
    
    private static class HologramDebugInfo {
        
        private int aliveEntities;
        private int deadEntities;
        
        public int getTotalEntities() {
            return aliveEntities + deadEntities;
        }
        
    }
    

}
