/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.commands.subs;

import me.filoghost.fcommons.command.sub.SubCommandContext;
import me.filoghost.fcommons.logging.Log;
import me.filoghost.holographicdisplays.Colors;
import me.filoghost.holographicdisplays.bridge.bungeecord.BungeeServerTracker;
import me.filoghost.holographicdisplays.commands.HologramSubCommand;
import me.filoghost.holographicdisplays.commands.Messages;
import me.filoghost.holographicdisplays.common.Utils;
import me.filoghost.holographicdisplays.disk.ConfigManager;
import me.filoghost.holographicdisplays.disk.Configuration;
import me.filoghost.holographicdisplays.disk.HologramConfig;
import me.filoghost.holographicdisplays.disk.HologramLoadException;
import me.filoghost.holographicdisplays.event.HolographicDisplaysReloadEvent;
import me.filoghost.holographicdisplays.object.BaseHologram;
import me.filoghost.holographicdisplays.object.InternalHologramManager;
import me.filoghost.holographicdisplays.placeholder.AnimationsRegister;
import me.filoghost.holographicdisplays.placeholder.PlaceholdersManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class ReloadCommand extends HologramSubCommand {

    private final ConfigManager configManager;
    private final InternalHologramManager internalHologramManager;

    public ReloadCommand(ConfigManager configManager, InternalHologramManager internalHologramManager) {
        super("reload");
        setDescription("Reloads the holograms from the database.");

        this.configManager = configManager;
        this.internalHologramManager = internalHologramManager;
    }
    
    @Override
    public void execute(CommandSender sender, String[] args, SubCommandContext context) {
        configManager.reloadCustomPlaceholders();
        configManager.reloadMainConfig();
        
        BungeeServerTracker.resetTrackedServers();
        BungeeServerTracker.restartTask(Configuration.bungeeRefreshSeconds);
        
        configManager.reloadHologramDatabase();
        try {
            AnimationsRegister.loadAnimations(configManager);
        } catch (Exception e) {
            Log.warning("Failed to load animation files!", e);
        }
        
        PlaceholdersManager.untrackAll();
        internalHologramManager.clearAll();
        
        // Create all the holograms
        for (HologramConfig hologramConfig :  configManager.getHologramDatabase().getHolograms()) {
            try {
                hologramConfig.createHologram(internalHologramManager);
            } catch (HologramLoadException e) {
                Messages.sendWarning(sender, Utils.formatExceptionMessage(e));
            }
        }
        
        // Then trigger a refresh for all of them
        for (BaseHologram hologram : internalHologramManager.getHolograms()) {
            hologram.refreshAll();
        }
        
        sender.sendMessage(Colors.PRIMARY + "Configuration reloaded successfully.");
        Bukkit.getPluginManager().callEvent(new HolographicDisplaysReloadEvent());
    }

}
