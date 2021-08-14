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
import me.filoghost.holographicdisplays.plugin.hologram.internal.InternalHologram;
import me.filoghost.holographicdisplays.plugin.hologram.internal.InternalHologramLine;
import org.bukkit.command.CommandSender;

public class SetLineCommand extends LineEditingCommand implements QuickEditCommand {

    private final HologramCommandManager commandManager;
    private final InternalHologramEditor hologramEditor;

    public SetLineCommand(HologramCommandManager commandManager, InternalHologramEditor hologramEditor) {
        super("setLine");
        setMinArgs(3);
        setUsageArgs("<hologram> <lineNumber> <newText>");
        setDescription("Changes a line of a hologram.");

        this.commandManager = commandManager;
        this.hologramEditor = hologramEditor;
    }

    @Override
    public void execute(CommandSender sender, String[] args, SubCommandContext context) throws CommandException {
        InternalHologram hologram = hologramEditor.getExistingHologram(args[0]);
        String serializedLine = Strings.joinFrom(" ", args, 2);

        int lineNumber = CommandValidate.parseInteger(args[1]);
        int linesAmount = hologram.getLines().size();

        CommandValidate.check(lineNumber >= 1 && lineNumber <= linesAmount,
                "The line number must be between 1 and " + linesAmount + ".");
        int index = lineNumber - 1;

        InternalHologramLine line = hologramEditor.parseHologramLine(hologram, serializedLine);

        hologram.getLines().set(index, line);
        hologramEditor.saveChanges(hologram, ChangeType.EDIT_LINES);

        sender.sendMessage(ColorScheme.PRIMARY + "Line " + lineNumber + " changed.");
        commandManager.sendQuickEditCommands(context, hologram);
    }

    @Override
    public String getActionName() {
        return "Set";
    }

}
