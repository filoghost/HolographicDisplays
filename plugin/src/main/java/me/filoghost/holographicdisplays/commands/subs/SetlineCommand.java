/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.commands.subs;

import me.filoghost.fcommons.command.sub.SubCommandContext;
import me.filoghost.fcommons.command.validation.CommandException;
import me.filoghost.fcommons.command.validation.CommandValidate;
import me.filoghost.holographicdisplays.Colors;
import me.filoghost.holographicdisplays.commands.HologramCommandValidate;
import me.filoghost.holographicdisplays.common.Utils;
import me.filoghost.holographicdisplays.disk.ConfigManager;
import me.filoghost.holographicdisplays.event.NamedHologramEditedEvent;
import me.filoghost.holographicdisplays.object.NamedHologram;
import me.filoghost.holographicdisplays.object.line.CraftHologramLine;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class SetlineCommand extends LineEditingCommand {

    private final ConfigManager configManager;

    public SetlineCommand(ConfigManager configManager) {
        super("setline");
        setMinArgs(3);
        setUsageArgs("<hologram> <lineNumber> <newText>");
        setDescription("Changes a line of a hologram.");
        
        this.configManager = configManager;
    }

    @Override
    public void execute(CommandSender sender, String[] args, SubCommandContext context) throws CommandException {
        NamedHologram hologram = HologramCommandValidate.getNamedHologram(args[0]);
        String serializedLine = Utils.join(args, " ", 2, args.length);
        
        int lineNumber = CommandValidate.parseInteger(args[1]);
        CommandValidate.check(lineNumber >= 1 && lineNumber <= hologram.size(), "The line number must be between 1 and " + hologram.size() + ".");
        int index = lineNumber - 1;
        
        CraftHologramLine line = HologramCommandValidate.parseHologramLine(hologram, serializedLine, true);
        
        hologram.getLinesUnsafe().get(index).despawn();
        hologram.getLinesUnsafe().set(index, line);
        hologram.refreshAll();

        configManager.getHologramDatabase().addOrUpdate(hologram);
        configManager.saveHologramDatabase();
        Bukkit.getPluginManager().callEvent(new NamedHologramEditedEvent(hologram));
        
        sender.sendMessage(Colors.PRIMARY + "Line " + lineNumber + " changed!");
        EditCommand.sendQuickEditCommands(context, hologram);
        
    }
    
}
