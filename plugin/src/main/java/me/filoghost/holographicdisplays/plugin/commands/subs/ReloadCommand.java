/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.commands.subs;

import me.filoghost.fcommons.command.sub.SubCommandContext;
import me.filoghost.holographicdisplays.plugin.Colors;
import me.filoghost.holographicdisplays.plugin.HolographicDisplays;
import me.filoghost.holographicdisplays.plugin.commands.HologramSubCommand;
import me.filoghost.holographicdisplays.plugin.event.HolographicDisplaysReloadEvent;
import me.filoghost.holographicdisplays.plugin.log.PrintableErrorCollector;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

public class ReloadCommand extends HologramSubCommand {

    public ReloadCommand() {
        super("reload");
        setDescription("Reloads the holograms from the database.");
    }
    
    @Override
    public void execute(CommandSender sender, String[] args, SubCommandContext context) {
        PrintableErrorCollector errorCollector = new PrintableErrorCollector();
        HolographicDisplays.getInstance().load(false, errorCollector);

        if (!errorCollector.hasErrors()) {
            sender.sendMessage(Colors.PRIMARY + "Configuration reloaded successfully.");
        } else {
            errorCollector.logToConsole();
            sender.sendMessage(Colors.ERROR + "Plugin reloaded with " + errorCollector.getErrorsCount() + " error(s).");
            if (!(sender instanceof ConsoleCommandSender)) {
                sender.sendMessage(Colors.ERROR + "Check the console for the details.");
            }
        }
        
        Bukkit.getPluginManager().callEvent(new HolographicDisplaysReloadEvent());
    }

}
