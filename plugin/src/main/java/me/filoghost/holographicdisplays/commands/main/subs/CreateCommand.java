/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.commands.main.subs;

import me.filoghost.holographicdisplays.commands.Colors;
import me.filoghost.holographicdisplays.commands.CommandValidator;
import me.filoghost.holographicdisplays.commands.Strings;
import me.filoghost.holographicdisplays.commands.main.HologramSubCommand;
import me.filoghost.holographicdisplays.disk.HologramDatabase;
import me.filoghost.holographicdisplays.exception.CommandException;
import me.filoghost.holographicdisplays.object.NamedHologram;
import me.filoghost.holographicdisplays.object.NamedHologramManager;
import me.filoghost.holographicdisplays.object.line.CraftHologramLine;
import me.filoghost.holographicdisplays.util.Utils;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import java.util.Arrays;
import java.util.List;

public class CreateCommand extends HologramSubCommand {
    
    public CreateCommand() {
        super("create");
        setPermission(Strings.BASE_PERM + "create");
    }

    @Override
    public String getPossibleArguments() {
        return "<hologramName> [text]";
    }

    @Override
    public int getMinimumArguments() {
        return 1;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void execute(CommandSender sender, String label, String[] args) throws CommandException {
        Player player = CommandValidator.getPlayerSender(sender);
        String hologramName = args[0];

        if (!hologramName.matches("[a-zA-Z0-9_\\-]+")) {
            throw new CommandException("The name must contain only alphanumeric chars, underscores and hyphens.");
        }

        CommandValidator.isTrue(!NamedHologramManager.isExistingHologram(hologramName), "A hologram with that name already exists.");

        Location spawnLoc = player.getLocation();
        boolean moveUp = player.isOnGround();

        if (moveUp) {
            spawnLoc.add(0.0, 1.2, 0.0);
        }

        NamedHologram hologram = new NamedHologram(spawnLoc, hologramName);

        if (args.length > 1) {
            String text = Utils.join(args, " ", 1, args.length);
            CommandValidator.isTrue(!text.equalsIgnoreCase("{empty}"), "The first line should not be empty.");
            
            CraftHologramLine line = CommandValidator.parseHologramLine(hologram, text, true);
            hologram.getLinesUnsafe().add(line);
            player.sendMessage(Colors.SECONDARY_SHADOW + "(Change the lines with /" + label + " edit " + hologram.getName() + ")");
        } else {
            hologram.appendTextLine("Default hologram. Change it with " + Colors.PRIMARY + "/" + label + " edit " + hologram.getName());
        }

        NamedHologramManager.addHologram(hologram);
        hologram.refreshAll();

        HologramDatabase.saveHologram(hologram);
        HologramDatabase.trySaveToDisk();
        Location look = player.getLocation();
        look.setPitch(90);
        player.teleport(look, TeleportCause.PLUGIN);
        player.sendMessage(Colors.PRIMARY + "You created a hologram named '" + hologram.getName() + "'.");

        if (moveUp) {
            player.sendMessage(Colors.SECONDARY_SHADOW + "(You were on the ground, the hologram was automatically moved up. If you use /" + label + " movehere " + hologram.getName() + ", the hologram will be moved to your feet)");
        }
    }

    @Override
    public List<String> getTutorial() {
        return Arrays.asList(
                "Creates a new hologram with the given name, that must",
                "be alphanumeric. The name will be used as reference to",
                "that hologram for editing commands.");
    }

    @Override
    public SubCommandType getType() {
        return SubCommandType.GENERIC;
    }

}
