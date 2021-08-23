/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.commands.subs;

import me.filoghost.fcommons.Strings;
import me.filoghost.fcommons.command.sub.SubCommandContext;
import me.filoghost.fcommons.command.validation.CommandException;
import me.filoghost.fcommons.command.validation.CommandValidate;
import me.filoghost.holographicdisplays.plugin.commands.HologramCommandManager;
import me.filoghost.holographicdisplays.plugin.commands.InternalHologramEditor;
import me.filoghost.holographicdisplays.plugin.event.InternalHologramChangeEvent.ChangeType;
import me.filoghost.holographicdisplays.plugin.format.ColorScheme;
import me.filoghost.holographicdisplays.plugin.format.DisplayFormat;
import me.filoghost.holographicdisplays.plugin.hologram.internal.InternalHologram;
import me.filoghost.holographicdisplays.plugin.hologram.internal.InternalHologramLine;
import org.bukkit.command.CommandSender;

public class InsertLineCommand extends LineEditingCommand implements QuickEditCommand {

    private final HologramCommandManager commandManager;
    private final InternalHologramEditor hologramEditor;

    public InsertLineCommand(HologramCommandManager commandManager, InternalHologramEditor hologramEditor) {
        super("insertLine");
        setMinArgs(3);
        setUsageArgs("<hologram> <lineNumber> <text>");
        setDescription(
                "Inserts a line after the specified line number.",
                "To insert at the top of the hologram, use \"0\" for the line number.");

        this.commandManager = commandManager;
        this.hologramEditor = hologramEditor;
    }

    @Override
    public void execute(CommandSender sender, String[] args, SubCommandContext context) throws CommandException {
        InternalHologram hologram = hologramEditor.getExistingHologram(args[0]);
        int insertAfterIndex = CommandValidate.parseInteger(args[1]);
        String serializedLine = Strings.joinFrom(" ", args, 2);

        int oldLinesAmount = hologram.lines().size();

        CommandValidate.check(insertAfterIndex >= 0 && insertAfterIndex <= oldLinesAmount,
                "The line number must be between 0 and " + oldLinesAmount + ".");

        InternalHologramLine line = hologramEditor.parseHologramLine(hologram, serializedLine);

        hologram.lines().insert(insertAfterIndex, line);
        hologramEditor.saveChanges(hologram, ChangeType.EDIT_LINES);

        if (insertAfterIndex == 0) {
            sender.sendMessage(ColorScheme.PRIMARY + "Line inserted before the first line.");
        } else if (insertAfterIndex == oldLinesAmount) {
            sender.sendMessage(ColorScheme.PRIMARY + "Line appended at the end.");
            DisplayFormat.sendTip(sender, "You can use \"/" + context.getRootLabel() + " addLine\" to append a line at the end.");
        } else {
            sender.sendMessage(ColorScheme.PRIMARY + "Line inserted between the lines " + insertAfterIndex
                    + " and " + (insertAfterIndex + 1) + ".");
        }
        commandManager.sendQuickEditCommands(context, hologram);
    }

    @Override
    public String getActionName() {
        return "Insert";
    }

}
