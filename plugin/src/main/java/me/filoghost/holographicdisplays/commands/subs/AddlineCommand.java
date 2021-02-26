/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.commands.subs;

import me.filoghost.fcommons.command.sub.SubCommandContext;
import me.filoghost.fcommons.command.validation.CommandException;
import me.filoghost.holographicdisplays.Colors;
import me.filoghost.holographicdisplays.commands.HologramCommandManager;
import me.filoghost.holographicdisplays.commands.HologramCommandValidate;
import me.filoghost.holographicdisplays.common.Utils;
import me.filoghost.holographicdisplays.disk.ConfigManager;
import me.filoghost.holographicdisplays.event.InternalHologramEditEvent;
import me.filoghost.holographicdisplays.object.internal.InternalHologram;
import me.filoghost.holographicdisplays.object.internal.InternalHologramLine;
import me.filoghost.holographicdisplays.object.internal.InternalHologramManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class AddlineCommand extends LineEditingCommand implements QuickEditCommand {

    private final HologramCommandManager commandManager;
    private final InternalHologramManager internalHologramManager;
    private final ConfigManager configManager;

    public AddlineCommand(HologramCommandManager commandManager, InternalHologramManager internalHologramManager, ConfigManager configManager) {
        super("addline");
        setMinArgs(2);
        setUsageArgs("<hologram> <text>");
        setDescription("Adds a line to an existing hologram.");

        this.commandManager = commandManager;
        this.internalHologramManager = internalHologramManager;
        this.configManager = configManager;
    }

    @Override
    public void execute(CommandSender sender, String[] args, SubCommandContext context) throws CommandException {
        InternalHologram hologram = HologramCommandValidate.getNamedHologram(internalHologramManager, args[0]);
        String serializedLine = Utils.join(args, " ", 1, args.length);
        
        InternalHologramLine line = HologramCommandValidate.parseHologramLine(hologram, serializedLine, true);
        hologram.getLinesUnsafe().add(line);
        hologram.refresh();

        configManager.getHologramDatabase().addOrUpdate(hologram);
        configManager.saveHologramDatabase();
        Bukkit.getPluginManager().callEvent(new InternalHologramEditEvent(hologram));
        
        sender.sendMessage(Colors.PRIMARY + "Line added.");
        commandManager.sendQuickEditCommands(context, hologram);
    }

    @Override
    public String getActionName() {
        return "Add";
    }

}
