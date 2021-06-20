/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.commands.subs;

import me.filoghost.fcommons.command.sub.SubCommandContext;
import me.filoghost.fcommons.command.validation.CommandException;
import me.filoghost.fcommons.command.validation.CommandValidate;
import me.filoghost.holographicdisplays.plugin.Colors;
import me.filoghost.holographicdisplays.plugin.commands.HologramCommandValidate;
import me.filoghost.holographicdisplays.plugin.commands.HologramSubCommand;
import me.filoghost.holographicdisplays.plugin.disk.ConfigManager;
import me.filoghost.holographicdisplays.plugin.object.internal.InternalHologram;
import me.filoghost.holographicdisplays.plugin.object.internal.InternalHologramManager;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;

public class AlignCommand extends HologramSubCommand {

    private final InternalHologramManager internalHologramManager;
    private final ConfigManager configManager;

    public AlignCommand(InternalHologramManager internalHologramManager, ConfigManager configManager) {
        super("align");
        setMinArgs(3);
        setUsageArgs("<X | Y | Z | XZ> <hologram> <referenceHologram>");
        setDescription("Aligns the first hologram to the second, in the specified axis.");

        this.internalHologramManager = internalHologramManager;
        this.configManager = configManager;
    }

    @Override
    public void execute(CommandSender sender, String[] args, SubCommandContext context) throws CommandException {
        InternalHologram hologram = HologramCommandValidate.getInternalHologram(internalHologramManager, args[1]);
        InternalHologram referenceHologram = HologramCommandValidate.getInternalHologram(internalHologramManager, args[2]);
        
        CommandValidate.check(hologram != referenceHologram, "The holograms must not be the same.");

        Location loc = hologram.getLocation();

        String axis = args[0];
        if (axis.equalsIgnoreCase("x")) {
            loc.setX(referenceHologram.getX());
        } else if (axis.equalsIgnoreCase("y")) {
            loc.setY(referenceHologram.getY());
        } else if (axis.equalsIgnoreCase("z")) {
            loc.setZ(referenceHologram.getZ());
        } else if (axis.equalsIgnoreCase("xz")) {
            loc.setX(referenceHologram.getX());
            loc.setZ(referenceHologram.getZ());
        } else {
            throw new CommandException("You must specify either X, Y, Z or XZ, " + axis + " is not a valid axis.");
        }

        hologram.teleport(loc);
            
        configManager.saveHologramDatabase(internalHologramManager);
        sender.sendMessage(Colors.PRIMARY + "Hologram \"" + hologram.getName() + "\"" 
                + " aligned to the hologram \"" + referenceHologram.getName() + "\"" 
                + " on the " + axis.toUpperCase() + " axis.");
    }
    
}
