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
import me.filoghost.holographicdisplays.commands.HologramSubCommand;
import me.filoghost.holographicdisplays.common.Utils;
import me.filoghost.holographicdisplays.disk.ConfigManager;
import me.filoghost.holographicdisplays.object.NamedHologram;
import me.filoghost.holographicdisplays.object.NamedHologramManager;
import me.filoghost.holographicdisplays.object.line.CraftHologramLine;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public class CreateCommand extends HologramSubCommand {

    private final ConfigManager configManager;
    
    public CreateCommand(ConfigManager configManager) {
        super("create");
        setMinArgs(1);
        setUsageArgs("<hologramName> [text]");
        setDescription(
                "Creates a new hologram with the given name, that must",
                "be alphanumeric. The name will be used as reference to",
                "that hologram for editing commands.");
        
        this.configManager = configManager;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void execute(CommandSender sender, String[] args, SubCommandContext context) throws CommandException {
        Player player = CommandValidate.getPlayerSender(sender);
        String hologramName = args[0];

        if (!hologramName.matches("[a-zA-Z0-9_\\-]+")) {
            throw new CommandException("The name must contain only alphanumeric chars, underscores and hyphens.");
        }

        CommandValidate.check(!NamedHologramManager.isExistingHologram(hologramName), "A hologram with that name already exists.");

        Location spawnLoc = player.getLocation();
        boolean moveUp = player.isOnGround();

        if (moveUp) {
            spawnLoc.add(0.0, 1.2, 0.0);
        }

        NamedHologram hologram = new NamedHologram(spawnLoc, hologramName);

        if (args.length > 1) {
            String text = Utils.join(args, " ", 1, args.length);
            CommandValidate.check(!text.equalsIgnoreCase("{empty}"), "The first line should not be empty.");
            
            CraftHologramLine line = HologramCommandValidate.parseHologramLine(hologram, text, true);
            hologram.getLinesUnsafe().add(line);
            player.sendMessage(Colors.SECONDARY_SHADOW + "(Change the lines with /" + context.getRootLabel() + " edit " + hologram.getName() + ")");
        } else {
            hologram.appendTextLine("Default hologram. Change it with " + Colors.PRIMARY + "/" + context.getRootLabel() + " edit " + hologram.getName());
        }

        NamedHologramManager.addHologram(hologram);
        hologram.refreshAll();

        configManager.getHologramDatabase().addOrUpdate(hologram);
        configManager.saveHologramDatabase();
        Location look = player.getLocation();
        look.setPitch(90);
        player.teleport(look, TeleportCause.PLUGIN);
        player.sendMessage(Colors.PRIMARY + "You created a hologram named '" + hologram.getName() + "'.");

        if (moveUp) {
            player.sendMessage(Colors.SECONDARY_SHADOW + "(You were on the ground, the hologram was automatically moved up. If you use /" + context.getRootLabel() + " movehere " + hologram.getName() + ", the hologram will be moved to your feet)");
        }
    }
    
}
