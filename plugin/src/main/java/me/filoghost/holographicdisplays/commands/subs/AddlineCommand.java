/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.commands.subs;

import me.filoghost.fcommons.command.sub.SubCommandContext;
import me.filoghost.fcommons.command.validation.CommandException;
import me.filoghost.holographicdisplays.Colors;
import me.filoghost.holographicdisplays.commands.HologramCommandValidate;
import me.filoghost.holographicdisplays.common.Utils;
import me.filoghost.holographicdisplays.disk.ConfigManager;
import me.filoghost.holographicdisplays.event.NamedHologramEditedEvent;
import me.filoghost.holographicdisplays.object.NamedHologram;
import me.filoghost.holographicdisplays.object.line.CraftHologramLine;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class AddlineCommand extends LineEditingCommand {

    private final ConfigManager configManager;

    public AddlineCommand(ConfigManager configManager) {
        super("addline");
        setMinArgs(2);
        setUsageArgs("<hologram> <text>");
        setDescription("Adds a line to an existing hologram.");
        
        this.configManager = configManager;
    }

    @Override
    public void execute(CommandSender sender, String[] args, SubCommandContext context) throws CommandException {
        NamedHologram hologram = HologramCommandValidate.getNamedHologram(args[0]);
        String serializedLine = Utils.join(args, " ", 1, args.length);
        
        CraftHologramLine line = HologramCommandValidate.parseHologramLine(hologram, serializedLine, true);
        hologram.getLinesUnsafe().add(line);
        hologram.refreshAll();

        configManager.getHologramDatabase().addOrUpdate(hologram);
        configManager.saveHologramDatabase();
        Bukkit.getPluginManager().callEvent(new NamedHologramEditedEvent(hologram));
        
        sender.sendMessage(Colors.PRIMARY + "Line added!");
        EditCommand.sendQuickEditCommands(context, hologram);
    }
    
}
