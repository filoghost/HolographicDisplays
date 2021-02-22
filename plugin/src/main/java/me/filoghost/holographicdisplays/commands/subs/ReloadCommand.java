/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.commands.subs;

import me.filoghost.fcommons.command.sub.SubCommandContext;
import me.filoghost.holographicdisplays.Colors;
import me.filoghost.holographicdisplays.HolographicDisplays;
import me.filoghost.holographicdisplays.commands.HologramSubCommand;
import me.filoghost.holographicdisplays.event.HolographicDisplaysReloadEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class ReloadCommand extends HologramSubCommand {

    public ReloadCommand() {
        super("reload");
        setDescription("Reloads the holograms from the database.");
    }
    
    @Override
    public void execute(CommandSender sender, String[] args, SubCommandContext context) {
        HolographicDisplays.getInstance().load(sender, false);
        
        sender.sendMessage(Colors.PRIMARY + "Configuration reloaded successfully.");
        Bukkit.getPluginManager().callEvent(new HolographicDisplaysReloadEvent());
    }

}
