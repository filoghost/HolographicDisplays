/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.commands.subs;

import me.filoghost.fcommons.command.sub.SubCommandContext;
import me.filoghost.fcommons.command.validation.CommandException;
import me.filoghost.fcommons.command.validation.CommandValidate;
import me.filoghost.holographicdisplays.plugin.commands.HologramSubCommand;
import me.filoghost.holographicdisplays.plugin.commands.InternalHologramEditor;
import me.filoghost.holographicdisplays.plugin.format.ColorScheme;
import me.filoghost.holographicdisplays.plugin.format.DisplayFormat;
import me.filoghost.holographicdisplays.plugin.hologram.base.BaseHologramPosition;
import me.filoghost.holographicdisplays.plugin.hologram.internal.InternalHologram;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ListCommand extends HologramSubCommand {

    private static final int HOLOGRAMS_PER_PAGE = 10;

    private final InternalHologramEditor hologramEditor;

    public ListCommand(InternalHologramEditor hologramEditor) {
        super("list");
        setMinArgs(0);
        setUsageArgs("[page]");
        setDescription("Lists all the existing holograms.");

        this.hologramEditor = hologramEditor;
    }

    @Override
    public void execute(CommandSender sender, String[] args, SubCommandContext context) throws CommandException {
        int page = args.length > 0 ? CommandValidate.parseInteger(args[0]) : 1;
        CommandValidate.check(page >= 1, "Page number must be 1 or greater.");

        List<InternalHologram> holograms = hologramEditor.getHolograms();
        int totalPages = holograms.size() / HOLOGRAMS_PER_PAGE;
        if (holograms.size() % HOLOGRAMS_PER_PAGE != 0) {
            totalPages++;
        }

        CommandValidate.check(holograms.size() > 0,
                "There are no holograms yet. Create one with /" + context.getRootLabel() + " create.");

        sender.sendMessage("");
        DisplayFormat.sendTitle(sender, "Holograms list " + ColorScheme.SECONDARY + "(Page " + page + " of " + totalPages + ")");
        int fromIndex = (page - 1) * HOLOGRAMS_PER_PAGE;
        int toIndex = fromIndex + HOLOGRAMS_PER_PAGE;

        for (int i = fromIndex; i < toIndex; i++) {
            if (i < holograms.size()) {
                InternalHologram hologram = holograms.get(i);
                BaseHologramPosition position = hologram.getBasePosition();
                sender.sendMessage(ColorScheme.SECONDARY_DARKER + "- " + ColorScheme.SECONDARY_BOLD + hologram.getName()
                        + " " + ColorScheme.SECONDARY_DARKER + "at"
                        + " x: " + position.getBlockX() + ", y: " + position.getBlockY() + ", z: " + position.getBlockZ()
                        + " (lines: " + hologram.getLineCount() + ", world: \"" + position.getWorld().getName() + "\")");
            }
        }

        if (page < totalPages) {
            DisplayFormat.sendTip(sender, "See the next page with /" + context.getRootLabel() + " list " + (page + 1));
        }
    }

}
