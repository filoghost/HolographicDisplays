/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.commands.main.subs;

import me.filoghost.holographicdisplays.Colors;
import me.filoghost.holographicdisplays.commands.CommandValidator;
import me.filoghost.holographicdisplays.commands.Messages;
import me.filoghost.holographicdisplays.commands.main.HologramSubCommand;
import me.filoghost.holographicdisplays.Permissions;
import me.filoghost.holographicdisplays.exception.CommandException;
import me.filoghost.holographicdisplays.object.NamedHologram;
import me.filoghost.holographicdisplays.object.NamedHologramManager;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class ListCommand extends HologramSubCommand {

    private static final int HOLOGRAMS_PER_PAGE = 10;

    public ListCommand() {
        super("list");
        setPermission(Permissions.COMMAND_BASE + "list");
    }

    @Override
    public String getPossibleArguments() {
        return "[page]";
    }

    @Override
    public int getMinimumArguments() {
        return 0;
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) throws CommandException {

        int page = args.length > 0 ? CommandValidator.getInteger(args[0]) : 1;

        if (page < 1) {
            throw new CommandException("Page number must be 1 or greater.");
        }

        int totalPages = NamedHologramManager.size() / HOLOGRAMS_PER_PAGE;
        if (NamedHologramManager.size() % HOLOGRAMS_PER_PAGE != 0) {
            totalPages++;
        }
        
        
        if (NamedHologramManager.size() == 0) {
            throw new CommandException("There are no holograms yet. Create one with /" + label + " create.");
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
            Messages.sendTip(sender, "See the next page with /" + label + " list " + (page + 1));
        }

    }

    @Override
    public List<String> getTutorial() {
        return Arrays.asList("Lists all the existing holograms.");
    }
    
    @Override
    public SubCommandType getType() {
        return SubCommandType.GENERIC;
    }

}
