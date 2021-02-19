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
import me.filoghost.holographicdisplays.commands.Messages;
import me.filoghost.holographicdisplays.common.Utils;
import me.filoghost.holographicdisplays.disk.ConfigManager;
import me.filoghost.holographicdisplays.event.NamedHologramEditedEvent;
import me.filoghost.holographicdisplays.object.NamedHologram;
import me.filoghost.holographicdisplays.object.line.CraftHologramLine;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class InsertlineCommand extends LineEditingCommand {

    private final ConfigManager configManager;
    
    public InsertlineCommand(ConfigManager configManager) {
        super("insertline");
        setMinArgs(3);
        setUsageArgs("<hologram> <lineNumber> <text>");
        setDescription(
                "Inserts a line after the specified index.",
                "If the index is 0, the line will be put before",
                "the first line of the hologram.");
        
        this.configManager = configManager;
    }

    @Override
    public void execute(CommandSender sender, String[] args, SubCommandContext context) throws CommandException {
        NamedHologram hologram = HologramCommandValidate.getNamedHologram(args[0]);
        int insertAfter = CommandValidate.parseInteger(args[1]);
        String serializedLine = Utils.join(args, " ", 2, args.length);
        
        int oldLinesAmount = hologram.size();
        
        CommandValidate.check(insertAfter >= 0 && insertAfter <= oldLinesAmount, "The number must be between 0 and " + hologram.size() + "(amount of lines of the hologram).");

        CraftHologramLine line = HologramCommandValidate.parseHologramLine(hologram, serializedLine, true);
        hologram.getLinesUnsafe().add(insertAfter, line);
        hologram.refreshAll();
            
        configManager.getHologramDatabase().addOrUpdate(hologram);
        configManager.saveHologramDatabase();
        
        Bukkit.getPluginManager().callEvent(new NamedHologramEditedEvent(hologram));
        
        if (insertAfter == 0) {
            sender.sendMessage(Colors.PRIMARY + "Line inserted before line n.1!");
        } else if (insertAfter == oldLinesAmount) {
            sender.sendMessage(Colors.PRIMARY + "Line appended at the end!");
            Messages.sendTip(sender, "You can use \"/" + context.getRootLabel() + " addline\" to add a line at the end.");
        } else {
            sender.sendMessage(Colors.PRIMARY + "Line inserted between lines " + insertAfter + " and " + (insertAfter + 1) + "!");
        }
        EditCommand.sendQuickEditCommands(context, hologram);
    }

}
