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
import me.filoghost.holographicdisplays.commands.HologramCommandManager;
import me.filoghost.holographicdisplays.commands.HologramCommandValidate;
import me.filoghost.holographicdisplays.common.Utils;
import me.filoghost.holographicdisplays.disk.ConfigManager;
import me.filoghost.holographicdisplays.event.InternalHologramEditEvent;
import me.filoghost.holographicdisplays.object.InternalHologram;
import me.filoghost.holographicdisplays.object.InternalHologramManager;
import me.filoghost.holographicdisplays.object.line.HologramLineImpl;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class SetlineCommand extends LineEditingCommand implements QuickEditCommand {

    private final HologramCommandManager commandManager;
    private final InternalHologramManager internalHologramManager;
    private final ConfigManager configManager;

    public SetlineCommand(HologramCommandManager commandManager, InternalHologramManager internalHologramManager, ConfigManager configManager) {
        super("setline");
        setMinArgs(3);
        setUsageArgs("<hologram> <lineNumber> <newText>");
        setDescription("Changes a line of a hologram.");

        this.commandManager = commandManager;
        this.internalHologramManager = internalHologramManager;
        this.configManager = configManager;
    }

    @Override
    public void execute(CommandSender sender, String[] args, SubCommandContext context) throws CommandException {
        InternalHologram hologram = HologramCommandValidate.getNamedHologram(internalHologramManager, args[0]);
        String serializedLine = Utils.join(args, " ", 2, args.length);
        
        int lineNumber = CommandValidate.parseInteger(args[1]);
        CommandValidate.check(lineNumber >= 1 && lineNumber <= hologram.size(), "The line number must be between 1 and " + hologram.size() + ".");
        int index = lineNumber - 1;
        
        HologramLineImpl line = HologramCommandValidate.parseHologramLine(hologram, serializedLine, true);
        
        hologram.getLinesUnsafe().get(index).despawn();
        hologram.getLinesUnsafe().set(index, line);
        hologram.refreshAll();

        configManager.getHologramDatabase().addOrUpdate(hologram);
        configManager.saveHologramDatabase();
        Bukkit.getPluginManager().callEvent(new InternalHologramEditEvent(hologram));
        
        sender.sendMessage(Colors.PRIMARY + "Line " + lineNumber + " changed.");
        commandManager.sendQuickEditCommands(context, hologram);
    }

    @Override
    public String getActionName() {
        return "Set";
    }

}
