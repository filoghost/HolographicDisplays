/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.commands;

import me.filoghost.fcommons.command.CommandContext;
import me.filoghost.fcommons.command.sub.SubCommand;
import me.filoghost.fcommons.command.sub.SubCommandContext;
import me.filoghost.fcommons.command.sub.SubCommandManager;
import me.filoghost.holographicdisplays.plugin.HolographicDisplays;
import me.filoghost.holographicdisplays.plugin.commands.subs.AddLineCommand;
import me.filoghost.holographicdisplays.plugin.commands.subs.AlignCommand;
import me.filoghost.holographicdisplays.plugin.commands.subs.CopyCommand;
import me.filoghost.holographicdisplays.plugin.commands.subs.CreateCommand;
import me.filoghost.holographicdisplays.plugin.commands.subs.DebugCommand;
import me.filoghost.holographicdisplays.plugin.commands.subs.DeleteCommand;
import me.filoghost.holographicdisplays.plugin.commands.subs.EditCommand;
import me.filoghost.holographicdisplays.plugin.commands.subs.HelpCommand;
import me.filoghost.holographicdisplays.plugin.commands.subs.InfoCommand;
import me.filoghost.holographicdisplays.plugin.commands.subs.InsertLineCommand;
import me.filoghost.holographicdisplays.plugin.commands.subs.ListCommand;
import me.filoghost.holographicdisplays.plugin.commands.subs.MoveHereCommand;
import me.filoghost.holographicdisplays.plugin.commands.subs.NearCommand;
import me.filoghost.holographicdisplays.plugin.commands.subs.QuickEditCommand;
import me.filoghost.holographicdisplays.plugin.commands.subs.ReadImageCommand;
import me.filoghost.holographicdisplays.plugin.commands.subs.ReadTextCommand;
import me.filoghost.holographicdisplays.plugin.commands.subs.ReloadCommand;
import me.filoghost.holographicdisplays.plugin.commands.subs.RemoveLineCommand;
import me.filoghost.holographicdisplays.plugin.commands.subs.SetLineCommand;
import me.filoghost.holographicdisplays.plugin.commands.subs.TeleportCommand;
import me.filoghost.holographicdisplays.plugin.config.ConfigManager;
import me.filoghost.holographicdisplays.plugin.config.Settings;
import me.filoghost.holographicdisplays.plugin.format.ColorScheme;
import me.filoghost.holographicdisplays.plugin.internal.hologram.InternalHologram;
import me.filoghost.holographicdisplays.plugin.internal.hologram.InternalHologramManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.ComponentBuilder.FormatRetention;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class HologramCommandManager extends SubCommandManager {

    private final HolographicDisplays holographicDisplays;
    private final InternalHologramEditor hologramEditor;
    private final List<HologramSubCommand> subCommands;
    private final HelpCommand helpCommand;

    public InternalHologramEditor getInternalHologramEditor() { return hologramEditor; }

    public HologramCommandManager(
            HolographicDisplays holographicDisplays,
            InternalHologramManager internalHologramManager,
            ConfigManager configManager) {
        setName("holograms");
        hologramEditor = new InternalHologramEditor(internalHologramManager, configManager);
        this.holographicDisplays = holographicDisplays;
        this.helpCommand = new HelpCommand(this);
        this.subCommands = new ArrayList<>();

        subCommands.add(new AddLineCommand(this, hologramEditor));
        subCommands.add(new CreateCommand(hologramEditor));
        subCommands.add(new DeleteCommand(hologramEditor));
        subCommands.add(new EditCommand(this, hologramEditor));
        subCommands.add(new ListCommand(hologramEditor));
        subCommands.add(new NearCommand(hologramEditor));
        subCommands.add(new TeleportCommand(hologramEditor));
        subCommands.add(new MoveHereCommand(hologramEditor));
        subCommands.add(new AlignCommand(hologramEditor));
        subCommands.add(new CopyCommand(hologramEditor));
        subCommands.add(new ReloadCommand(holographicDisplays));

        subCommands.add(new RemoveLineCommand(this, hologramEditor));
        subCommands.add(new SetLineCommand(this, hologramEditor));
        subCommands.add(new InsertLineCommand(this, hologramEditor));
        subCommands.add(new ReadTextCommand(hologramEditor));
        subCommands.add(new ReadImageCommand(hologramEditor));
        subCommands.add(new InfoCommand(this, hologramEditor));

        subCommands.add(new DebugCommand());
        subCommands.add(helpCommand);
    }

    @Override
    protected SubCommand getSubCommandByName(String name) {
        for (HologramSubCommand subCommand : subCommands) {
            if (subCommand.getName().equalsIgnoreCase(name)) {
                return subCommand;
            }

            for (String alias : subCommand.getAliases()) {
                if (alias.equalsIgnoreCase(name)) {
                    return subCommand;
                }
            }
        }

        return null;
    }

    public void sendQuickEditCommands(SubCommandContext commandContext, InternalHologram hologram) {
        if (!Settings.quickEditCommands) {
            return;
        }
        if (!(commandContext.getSender() instanceof Player)) {
            return;
        }

        ComponentBuilder message = new ComponentBuilder("EDIT LINES:").color(ChatColor.GRAY).bold(true).append("  ", FormatRetention.NONE);

        for (HologramSubCommand subCommand : subCommands) {
            if (!(subCommand instanceof QuickEditCommand)) {
                continue;
            }

            QuickEditCommand quickEditCommand = (QuickEditCommand) subCommand;

            // Assume first argument is always "<hologram>" and remove it
            String usageArgs = subCommand.getUsageArgs();
            if (usageArgs != null && usageArgs.contains(" ")) {
                usageArgs = usageArgs.substring(usageArgs.indexOf(" ") + 1);
            } else {
                usageArgs = "";
            }

            String usage = "/" + commandContext.getRootLabel() + " " + subCommand.getName() + " " + hologram.getName() + " ";
            message.append("[" + quickEditCommand.getActionName() + "]").color(ChatColor.DARK_AQUA)
                    .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, usage))
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(
                            ChatColor.GRAY + "Click to insert in chat the highlighted part of the command:\n"
                                    + ChatColor.YELLOW + usage + ChatColor.DARK_GRAY + usageArgs)));
            message.append("  ", FormatRetention.NONE);
        }

        ((Player) commandContext.getSender()).spigot().sendMessage(message.create());
    }

    @Override
    public Iterable<HologramSubCommand> getSubCommands() {
        return subCommands;
    }

    @Override
    protected void sendNoArgsMessage(CommandContext context) {
        CommandSender sender = context.getSender();
        String version = holographicDisplays.getDescription().getVersion();
        sender.sendMessage(ColorScheme.PRIMARY_DARK + "Server is running " + ColorScheme.PRIMARY + "Holographic Displays "
                + ColorScheme.PRIMARY_DARK + "v" + version + " by " + ColorScheme.PRIMARY + "filoghost");
        if (helpCommand.hasPermission(sender)) {
            sender.sendMessage(ColorScheme.PRIMARY_DARK + "Commands: " + ColorScheme.PRIMARY + helpCommand.getFullUsageText(context));
        }
    }

    @Override
    protected void sendSubCommandDefaultPermissionMessage(SubCommandContext context) {
        context.getSender().sendMessage(ColorScheme.ERROR + "You don't have permission for this sub-command.");
    }

    @Override
    protected void sendUnknownSubCommandMessage(SubCommandContext context) {
        context.getSender().sendMessage(ColorScheme.ERROR + "Unknown sub-command."
                + " Type \"" + helpCommand.getFullUsageText(context) + "\" for a list of commands.");
    }

    @Override
    protected void sendSubCommandUsage(SubCommandContext context) {
        context.getSender().sendMessage(ColorScheme.ERROR + "Usage: /" + context.getRootLabel() + " "
                + context.getSubLabel() + " " + context.getSubCommand().getUsageArgs());
    }

    @Override
    protected void sendExecutionErrorMessage(CommandContext context, String errorMessage) {
        context.getSender().sendMessage(ColorScheme.ERROR + errorMessage);
    }

    @Override
    protected void handleUnexpectedException(CommandContext context, Throwable t) {
        Bukkit.getLogger().log(Level.SEVERE, "Unhandled exception while executing /" + context.getRootLabel(), t);
        context.getSender().sendMessage(ColorScheme.ERROR + "Internal error while executing command."
                + " Please look on the console for more details.");
    }

}
