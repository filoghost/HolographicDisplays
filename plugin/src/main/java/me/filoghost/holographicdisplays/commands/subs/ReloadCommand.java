/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.commands.subs;

import me.filoghost.fcommons.command.sub.SubCommandContext;
import me.filoghost.fcommons.logging.Log;
import me.filoghost.holographicdisplays.Colors;
import me.filoghost.holographicdisplays.HolographicDisplays;
import me.filoghost.holographicdisplays.bridge.bungeecord.BungeeServerTracker;
import me.filoghost.holographicdisplays.commands.HologramSubCommand;
import me.filoghost.holographicdisplays.commands.Messages;
import me.filoghost.holographicdisplays.common.Utils;
import me.filoghost.holographicdisplays.disk.ConfigManager;
import me.filoghost.holographicdisplays.disk.Configuration;
import me.filoghost.holographicdisplays.disk.HologramConfig;
import me.filoghost.holographicdisplays.disk.HologramLoadException;
import me.filoghost.holographicdisplays.event.HolographicDisplaysReloadEvent;
import me.filoghost.holographicdisplays.object.CraftHologram;
import me.filoghost.holographicdisplays.object.NamedHologram;
import me.filoghost.holographicdisplays.object.NamedHologramManager;
import me.filoghost.holographicdisplays.placeholder.AnimationsRegister;
import me.filoghost.holographicdisplays.placeholder.PlaceholdersManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class ReloadCommand extends HologramSubCommand {

    private final ConfigManager configManager;

    public ReloadCommand(ConfigManager configManager) {
        super("reload");
        setDescription("Reloads the holograms from the database.");
        
        this.configManager = configManager;
    }
    
    @Override
    public void execute(CommandSender sender, String[] args, SubCommandContext context) {            
        long startMillis = System.currentTimeMillis();

        configManager.reloadCustomPlaceholders();
        configManager.reloadMainConfig();
        
        BungeeServerTracker.resetTrackedServers();
        BungeeServerTracker.restartTask(Configuration.bungeeRefreshSeconds);
        
        configManager.reloadHologramDatabase();
        try {
            AnimationsRegister.loadAnimations(HolographicDisplays.getInstance());
        } catch (Exception e) {
            Log.warning("Failed to load animation files!", e);
        }
        
        PlaceholdersManager.untrackAll();
        NamedHologramManager.clearAll();
        
        for (HologramConfig hologramConfig :  configManager.getHologramDatabase().getHolograms()) {
            try {
                NamedHologram hologram = hologramConfig.createHologram();
                NamedHologramManager.addHologram(hologram);
            } catch (HologramLoadException e) {
                Messages.sendWarning(sender, Utils.formatExceptionMessage(e));
            }
        }
        
        for (CraftHologram hologram : NamedHologramManager.getHolograms()) {
            hologram.refreshAll();
        }
        
        long endMillis = System.currentTimeMillis();
        
        sender.sendMessage(Colors.PRIMARY + "Configuration reloaded successfully in " + (endMillis - startMillis) + "ms!");
        
        Bukkit.getPluginManager().callEvent(new HolographicDisplaysReloadEvent());
    }

}
