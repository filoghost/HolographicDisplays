/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.commands.subs;

import me.filoghost.fcommons.command.sub.SubCommandContext;
import me.filoghost.fcommons.command.validation.CommandException;
import me.filoghost.fcommons.command.validation.CommandValidate;
import me.filoghost.holographicdisplays.plugin.commands.HologramCommandManager;
import me.filoghost.holographicdisplays.plugin.commands.InternalHologramEditor;
import me.filoghost.holographicdisplays.plugin.event.InternalHologramChangeEvent.ChangeType;
import me.filoghost.holographicdisplays.plugin.format.ColorScheme;
import me.filoghost.holographicdisplays.plugin.internal.hologram.InternalHologram;
import org.bukkit.command.CommandSender;

public class RemoveLineCommand extends LineEditingCommand implements QuickEditCommand {

    private final HologramCommandManager commandManager;
    private final InternalHologramEditor hologramEditor;

    public RemoveLineCommand(HologramCommandManager commandManager, InternalHologramEditor hologramEditor) {
        super("removeLine");
        setMinArgs(2);
        setUsageArgs("<hologram> <lineNumber>");
        setDescription("Removes a line from a hologram.");

        this.commandManager = commandManager;
        this.hologramEditor = hologramEditor;
    }

    @Override
    public void execute(CommandSender sender, String[] args, SubCommandContext context) throws CommandException {
        InternalHologram hologram = hologramEditor.getExistingHologram(args[0]);

        int lineNumber = CommandValidate.parseInteger(args[1]);
        int linesAmount = hologram.lines().size();

        CommandValidate.check(lineNumber >= 1 && lineNumber <= linesAmount,
                "The line number must be between 1 and " + linesAmount + ".");
        int index = lineNumber - 1;

        CommandValidate.check(linesAmount >= 2,
                "A hologram must always have at least 1 line. If you want to delete it, use /" + context.getRootLabel() + " delete.");

        hologram.lines().remove(index);
        hologramEditor.saveChanges(hologram, ChangeType.EDIT_LINES);

        sender.sendMessage(ColorScheme.PRIMARY + "Line " + lineNumber + " removed.");
        commandManager.sendQuickEditCommands(context, hologram);
    }

    @Override
    public String getActionName() {
        return "Remove";
    }

}
