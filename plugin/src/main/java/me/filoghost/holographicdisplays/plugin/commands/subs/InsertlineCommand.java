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

public class InsertlineCommand extends LineEditingCommand implements QuickEditCommand {

    private final HologramCommandManager commandManager;
    private final InternalHologramEditor hologramEditor;

    public InsertlineCommand(HologramCommandManager commandManager, InternalHologramEditor hologramEditor) {
        super("insertline");
        setMinArgs(3);
        setUsageArgs("<hologram> <lineNumber> <text>");
        setDescription(
                "Inserts a line after the specified index.",
                "If the index is 0, the line will be put before",
                "the first line of the hologram.");

        this.commandManager = commandManager;
        this.hologramEditor = hologramEditor;
    }

    @Override
    public void execute(CommandSender sender, String[] args, SubCommandContext context) throws CommandException {
        InternalHologram hologram = hologramEditor.getExistingHologram(args[0]);
        int insertAfterIndex = CommandValidate.parseInteger(args[1]);
        String serializedLine = Strings.joinFrom(" ", args, 2);

        int oldLinesAmount = hologram.getLines().size();

        CommandValidate.check(insertAfterIndex >= 0 && insertAfterIndex <= oldLinesAmount,
                "The number must be between 0 and " + oldLinesAmount + " (amount of lines of the hologram).");

        InternalHologramLine line = hologramEditor.parseHologramLine(hologram, serializedLine);

        hologram.getLines().insert(insertAfterIndex, line);
        hologramEditor.saveChanges(hologram, ChangeType.EDIT_LINES);

        if (insertAfterIndex == 0) {
            sender.sendMessage(ColorScheme.PRIMARY + "Line inserted before first line.");
        } else if (insertAfterIndex == oldLinesAmount) {
            sender.sendMessage(ColorScheme.PRIMARY + "Line appended at the end.");
            DisplayFormat.sendTip(sender, "You can use \"/" + context.getRootLabel() + " addline\" to append a line at the end.");
        } else {
            sender.sendMessage(ColorScheme.PRIMARY + "Line inserted between lines " + insertAfterIndex
                    + " and " + (insertAfterIndex + 1) + ".");
        }
        commandManager.sendQuickEditCommands(context, hologram);
    }

    @Override
    public String getActionName() {
        return "Insert";
    }

}
