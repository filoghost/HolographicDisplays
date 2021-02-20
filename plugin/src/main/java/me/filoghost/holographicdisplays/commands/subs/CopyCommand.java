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
import me.filoghost.holographicdisplays.commands.HologramSubCommand;
import me.filoghost.holographicdisplays.disk.ConfigManager;
import me.filoghost.holographicdisplays.object.InternalHologram;
import me.filoghost.holographicdisplays.object.InternalHologramManager;
import me.filoghost.holographicdisplays.object.line.HologramLineImpl;
import org.bukkit.command.CommandSender;

public class CopyCommand extends HologramSubCommand {

    private final InternalHologramManager internalHologramManager;
    private final ConfigManager configManager;
    
    public CopyCommand(InternalHologramManager internalHologramManager, ConfigManager configManager) {
        super("copy");
        setMinArgs(2);
        setUsageArgs("<fromHologram> <toHologram>");
        setDescription("Copies the contents of a hologram into another one.");

        this.internalHologramManager = internalHologramManager;
        this.configManager = configManager;
    }
    
    @Override
    public void execute(CommandSender sender, String[] args, SubCommandContext context) throws CommandException {
        InternalHologram fromHologram = HologramCommandValidate.getNamedHologram(internalHologramManager, args[0]);
        InternalHologram toHologram = HologramCommandValidate.getNamedHologram(internalHologramManager, args[1]);
        
        toHologram.clearLines();
        for (HologramLineImpl line : fromHologram.getLinesUnsafe()) {
            HologramLineImpl clonedLine = HologramCommandValidate.parseHologramLine(toHologram, line.getSerializedConfigValue(), false);
            toHologram.getLinesUnsafe().add(clonedLine);
        }
        
        toHologram.refreshAll();

        configManager.getHologramDatabase().addOrUpdate(toHologram);
        configManager.saveHologramDatabase();
        
        sender.sendMessage(Colors.PRIMARY + "Hologram \"" + fromHologram.getName() + "\" copied into hologram \"" + toHologram.getName() + "\".");
    }

}
