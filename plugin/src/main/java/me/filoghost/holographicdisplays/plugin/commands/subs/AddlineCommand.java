/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.commands.subs;

import me.filoghost.fcommons.Strings;
import me.filoghost.fcommons.command.sub.SubCommandContext;
import me.filoghost.fcommons.command.validation.CommandException;
import me.filoghost.holographicdisplays.plugin.format.ColorScheme;
import me.filoghost.holographicdisplays.plugin.commands.HologramCommandManager;
import me.filoghost.holographicdisplays.plugin.commands.HologramCommandValidate;
import me.filoghost.holographicdisplays.plugin.disk.ConfigManager;
import me.filoghost.holographicdisplays.plugin.event.InternalHologramEditEvent;
import me.filoghost.holographicdisplays.plugin.hologram.internal.InternalHologram;
import me.filoghost.holographicdisplays.plugin.hologram.internal.InternalHologramLine;
import me.filoghost.holographicdisplays.plugin.hologram.internal.InternalHologramManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class AddlineCommand extends LineEditingCommand implements QuickEditCommand {

    private final HologramCommandManager commandManager;
    private final InternalHologramManager internalHologramManager;
    private final ConfigManager configManager;

    public AddlineCommand(
            HologramCommandManager commandManager,
            InternalHologramManager internalHologramManager,
            ConfigManager configManager) {
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
        InternalHologram hologram = HologramCommandValidate.getInternalHologram(internalHologramManager, args[0]);
        String serializedLine = Strings.joinFrom(" ", args, 1);

        InternalHologramLine line = HologramCommandValidate.parseHologramLine(hologram, serializedLine);
        hologram.addLine(line);

        configManager.saveHologramDatabase(internalHologramManager);
        Bukkit.getPluginManager().callEvent(new InternalHologramEditEvent(hologram));

        sender.sendMessage(ColorScheme.PRIMARY + "Line added.");
        commandManager.sendQuickEditCommands(context, hologram);
    }

    @Override
    public String getActionName() {
        return "Add";
    }

}
