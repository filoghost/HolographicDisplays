/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.commands.subs;

import me.filoghost.fcommons.command.sub.SubCommandContext;
import me.filoghost.fcommons.command.validation.CommandException;
import me.filoghost.fcommons.command.validation.CommandValidate;
import me.filoghost.holographicdisplays.plugin.format.ColorScheme;
import me.filoghost.holographicdisplays.plugin.commands.HologramCommandValidate;
import me.filoghost.holographicdisplays.plugin.commands.HologramSubCommand;
import me.filoghost.holographicdisplays.plugin.disk.ConfigManager;
import me.filoghost.holographicdisplays.plugin.hologram.internal.InternalHologram;
import me.filoghost.holographicdisplays.plugin.hologram.internal.InternalHologramManager;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public class MovehereCommand extends HologramSubCommand {

    private final InternalHologramManager internalHologramManager;
    private final ConfigManager configManager;

    public MovehereCommand(InternalHologramManager internalHologramManager, ConfigManager configManager) {
        super("movehere");
        setMinArgs(1);
        setUsageArgs("<hologram>");
        setDescription("Moves a hologram to your location.");

        this.internalHologramManager = internalHologramManager;
        this.configManager = configManager;
    }

    @Override
    public void execute(CommandSender sender, String[] args, SubCommandContext context) throws CommandException {
        Player player = CommandValidate.getPlayerSender(sender);
        InternalHologram hologram = HologramCommandValidate.getInternalHologram(internalHologramManager, args[0]);

        hologram.teleport(player.getLocation());

        configManager.saveHologramDatabase(internalHologramManager);
        Location to = player.getLocation();
        to.setPitch(90);
        player.teleport(to, TeleportCause.PLUGIN);
        player.sendMessage(ColorScheme.PRIMARY + "You moved the hologram '" + hologram.getName() + "' near to you.");
    }

}
