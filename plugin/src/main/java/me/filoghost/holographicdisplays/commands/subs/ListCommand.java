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
import me.filoghost.holographicdisplays.commands.HologramSubCommand;
import me.filoghost.holographicdisplays.commands.Messages;
import me.filoghost.holographicdisplays.object.NamedHologram;
import me.filoghost.holographicdisplays.object.NamedHologramManager;
import org.bukkit.command.CommandSender;

public class ListCommand extends HologramSubCommand {

    private static final int HOLOGRAMS_PER_PAGE = 10;

    public ListCommand() {
        super("list");
        setMinArgs(0);
        setUsageArgs("[page]");
        setDescription("Lists all the existing holograms.");
    }

    @Override
    public void execute(CommandSender sender, String[] args, SubCommandContext context) throws CommandException {

        int page = args.length > 0 ? CommandValidate.parseInteger(args[0]) : 1;

        if (page < 1) {
            throw new CommandException("Page number must be 1 or greater.");
        }

        int totalPages = NamedHologramManager.size() / HOLOGRAMS_PER_PAGE;
        if (NamedHologramManager.size() % HOLOGRAMS_PER_PAGE != 0) {
            totalPages++;
        }
        
        if (NamedHologramManager.size() == 0) {
            throw new CommandException("There are no holograms yet. Create one with /" + context.getRootLabel() + " create.");
        }

        sender.sendMessage("");
        Messages.sendTitle(sender, "Holograms list " + Colors.SECONDARY + "(Page " + page + " of " + totalPages + ")");
        int fromIndex = (page - 1) * HOLOGRAMS_PER_PAGE;
        int toIndex = fromIndex + HOLOGRAMS_PER_PAGE;

        for (int i = fromIndex; i < toIndex; i++) {
            if (i < NamedHologramManager.size()) {
                NamedHologram hologram = NamedHologramManager.get(i);
                sender.sendMessage(Colors.SECONDARY_SHADOW + "- " + Colors.SECONDARY + Colors.BOLD + hologram.getName() + " " + Colors.SECONDARY_SHADOW + "at x: " + (int) hologram.getX() + ", y: " + (int) hologram.getY() + ", z: " + (int) hologram.getZ() + " (lines: " + hologram.size() + ", world: \"" + hologram.getWorld().getName() + "\")");
            }
        }
        if (page < totalPages) {
            Messages.sendTip(sender, "See the next page with /" + context.getRootLabel() + " list " + (page + 1));
        }

    }

}
