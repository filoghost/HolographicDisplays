/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.commands.subs;

import me.filoghost.fcommons.command.sub.SubCommandContext;
import me.filoghost.holographicdisplays.plugin.HolographicDisplays;
import me.filoghost.holographicdisplays.plugin.commands.HologramSubCommand;
import me.filoghost.holographicdisplays.plugin.event.HolographicDisplaysReloadEvent;
import me.filoghost.holographicdisplays.plugin.format.ColorScheme;
import me.filoghost.holographicdisplays.plugin.log.PrintableErrorCollector;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

public class ReloadCommand extends HologramSubCommand {

    private final HolographicDisplays holographicDisplays;

    public ReloadCommand(HolographicDisplays holographicDisplays) {
        super("reload");
        setDescription("Reloads the plugin, including the configuration and the holograms.");

        this.holographicDisplays = holographicDisplays;
    }

    @Override
    public void execute(CommandSender sender, String[] args, SubCommandContext context) {
        PrintableErrorCollector errorCollector = new PrintableErrorCollector();
        holographicDisplays.load(errorCollector);

        if (!errorCollector.hasErrors()) {
            sender.sendMessage(ColorScheme.PRIMARY + "Plugin reloaded successfully.");
        } else {
            errorCollector.logToConsole();
            sender.sendMessage(ColorScheme.ERROR + "Plugin reloaded with " + errorCollector.getErrorsCount() + " error(s).");
            if (!(sender instanceof ConsoleCommandSender)) {
                sender.sendMessage(ColorScheme.ERROR + "Check the console for the details.");
            }
        }

        Bukkit.getPluginManager().callEvent(new HolographicDisplaysReloadEvent());
    }

}
