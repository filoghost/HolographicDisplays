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
import me.filoghost.holographicdisplays.plugin.hologram.internal.InternalHologram;
import me.filoghost.holographicdisplays.plugin.hologram.internal.InternalHologramManager;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;


public class TeleportCommand extends HologramSubCommand {

    private final InternalHologramManager internalHologramManager;
    
    public TeleportCommand(InternalHologramManager internalHologramManager) {
        super("teleport", "tp");
        setMinArgs(1);
        setUsageArgs("<hologram>");
        setDescription("Teleports you to the given hologram.");
        
        this.internalHologramManager = internalHologramManager;
    }

    @Override
    public void execute(CommandSender sender, String[] args, SubCommandContext context) throws CommandException {
        Player player = CommandValidate.getPlayerSender(sender);
        InternalHologram hologram = HologramCommandValidate.getInternalHologram(internalHologramManager, args[0]);
        
        Location loc = hologram.getLocation();
        loc.setPitch(90);
        player.teleport(loc, TeleportCause.PLUGIN);
        player.sendMessage(Colors.PRIMARY + "You were teleported to the hologram named '" + hologram.getName() + "'.");
    }

}
