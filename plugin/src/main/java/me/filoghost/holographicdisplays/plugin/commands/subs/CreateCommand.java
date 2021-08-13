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
import me.filoghost.holographicdisplays.plugin.commands.HologramSubCommand;
import me.filoghost.holographicdisplays.plugin.commands.InternalHologramEditor;
import me.filoghost.holographicdisplays.plugin.event.InternalHologramChangeEvent.ChangeType;
import me.filoghost.holographicdisplays.plugin.format.ColorScheme;
import me.filoghost.holographicdisplays.plugin.hologram.base.BaseHologramPosition;
import me.filoghost.holographicdisplays.plugin.hologram.internal.InternalHologram;
import me.filoghost.holographicdisplays.plugin.hologram.internal.InternalHologramLine;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreateCommand extends HologramSubCommand {

    private final InternalHologramEditor hologramEditor;

    public CreateCommand(InternalHologramEditor hologramEditor) {
        super("create");
        setMinArgs(1);
        setUsageArgs("<hologramName> [text]");
        setDescription(
                "Creates a new hologram with the given name, that must",
                "be alphanumeric. The name will be used as reference to",
                "that hologram for editing commands.");

        this.hologramEditor = hologramEditor;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void execute(CommandSender sender, String[] args, SubCommandContext context) throws CommandException {
        Player player = CommandValidate.getPlayerSender(sender);
        String hologramName = args[0];

        CommandValidate.check(hologramName.matches("[a-zA-Z0-9_\\-]+"),
                "The name must contain only alphanumeric characters, underscores and hyphens.");
        CommandValidate.check(!hologramEditor.hologramExists(hologramName), "A hologram with that name already exists.");

        BaseHologramPosition spawnPosition = new BaseHologramPosition(player.getLocation());
        boolean moveUp = player.isOnGround();

        if (moveUp) {
            spawnPosition.add(0, 1.2, 0);
        }

        InternalHologram hologram = hologramEditor.create(spawnPosition, hologramName);
        InternalHologramLine line;

        if (args.length > 1) {
            String text = Strings.joinFrom(" ", args, 1);
            line = hologramEditor.parseHologramLine(hologram, text);
            player.sendMessage(ColorScheme.SECONDARY_DARKER + "(Change the lines with /" + context.getRootLabel()
                    + " edit " + hologram.getName() + ")");
        } else {
            String defaultText = "Default hologram. Change it with "
                    + ColorScheme.PRIMARY + "/" + context.getRootLabel() + " edit " + hologram.getName();
            line = hologram.createTextLine(defaultText, defaultText.replace(ChatColor.COLOR_CHAR, '&'));
        }

        hologram.getLines().add(line);
        hologramEditor.saveChanges(hologram, ChangeType.CREATE);

        hologramEditor.teleportLookingDown(player, player.getLocation());
        player.sendMessage(ColorScheme.PRIMARY + "Hologram named \"" + hologram.getName() + "\" created.");

        if (moveUp) {
            player.sendMessage(ColorScheme.SECONDARY_DARKER + "(You were on the ground,"
                    + " the hologram was automatically moved up."
                    + " If you use /" + context.getRootLabel() + " movehere " + hologram.getName() + ","
                    + " the hologram will be moved to your feet)");
        }
    }

}
