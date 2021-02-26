/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.commands;

import me.filoghost.fcommons.command.CommandContext;
import me.filoghost.fcommons.command.sub.SubCommand;
import me.filoghost.fcommons.command.sub.SubCommandContext;
import me.filoghost.fcommons.command.sub.SubCommandManager;
import me.filoghost.holographicdisplays.Colors;
import me.filoghost.holographicdisplays.HolographicDisplays;
import me.filoghost.holographicdisplays.commands.subs.AddlineCommand;
import me.filoghost.holographicdisplays.commands.subs.AlignCommand;
import me.filoghost.holographicdisplays.commands.subs.CopyCommand;
import me.filoghost.holographicdisplays.commands.subs.CreateCommand;
import me.filoghost.holographicdisplays.commands.subs.DebugCommand;
import me.filoghost.holographicdisplays.commands.subs.DeleteCommand;
import me.filoghost.holographicdisplays.commands.subs.EditCommand;
import me.filoghost.holographicdisplays.commands.subs.HelpCommand;
import me.filoghost.holographicdisplays.commands.subs.InfoCommand;
import me.filoghost.holographicdisplays.commands.subs.InsertlineCommand;
import me.filoghost.holographicdisplays.commands.subs.ListCommand;
import me.filoghost.holographicdisplays.commands.subs.MovehereCommand;
import me.filoghost.holographicdisplays.commands.subs.NearCommand;
import me.filoghost.holographicdisplays.commands.subs.QuickEditCommand;
import me.filoghost.holographicdisplays.commands.subs.ReadimageCommand;
import me.filoghost.holographicdisplays.commands.subs.ReadtextCommand;
import me.filoghost.holographicdisplays.commands.subs.ReloadCommand;
import me.filoghost.holographicdisplays.commands.subs.RemovelineCommand;
import me.filoghost.holographicdisplays.commands.subs.SetlineCommand;
import me.filoghost.holographicdisplays.commands.subs.TeleportCommand;
import me.filoghost.holographicdisplays.core.Utils;
import me.filoghost.holographicdisplays.disk.ConfigManager;
import me.filoghost.holographicdisplays.disk.Configuration;
import me.filoghost.holographicdisplays.core.nms.NMSManager;
import me.filoghost.holographicdisplays.object.internal.InternalHologram;
import me.filoghost.holographicdisplays.object.internal.InternalHologramManager;
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

    private final List<HologramSubCommand> subCommands;
    
    private final HelpCommand helpCommand;

    public HologramCommandManager(ConfigManager configManager, InternalHologramManager internalHologramManager, NMSManager nmsManager) {
        setName("holograms");
        subCommands = new ArrayList<>();

        subCommands.add(new AddlineCommand(this, internalHologramManager, configManager));
        subCommands.add(new CreateCommand(internalHologramManager, configManager));
        subCommands.add(new DeleteCommand(internalHologramManager, configManager));
        subCommands.add(new EditCommand(this, internalHologramManager));
        subCommands.add(new ListCommand(internalHologramManager));
        subCommands.add(new NearCommand(internalHologramManager));
        subCommands.add(new TeleportCommand(internalHologramManager));
        subCommands.add(new MovehereCommand(internalHologramManager, configManager));
        subCommands.add(new AlignCommand(internalHologramManager, configManager));
        subCommands.add(new CopyCommand(internalHologramManager, configManager));
        subCommands.add(new ReloadCommand());

        subCommands.add(new RemovelineCommand(this, internalHologramManager, configManager));
        subCommands.add(new SetlineCommand(this, internalHologramManager, configManager));
        subCommands.add(new InsertlineCommand(this, internalHologramManager, configManager));
        subCommands.add(new ReadtextCommand(internalHologramManager, configManager));
        subCommands.add(new ReadimageCommand(internalHologramManager, configManager));
        subCommands.add(new InfoCommand(this, internalHologramManager));

        subCommands.add(new DebugCommand(nmsManager));
        subCommands.add(helpCommand = new HelpCommand(this));
    }

    @Override
    protected SubCommand getSubCommandByName(String name) {
        for (HologramSubCommand subCommand : subCommands) {
            if (subCommand.getName().equalsIgnoreCase(name)) {
                return subCommand;
            }

            if (subCommand.getAliases() != null) {
                for (String alias : subCommand.getAliases()) {
                    if (alias.equalsIgnoreCase(name)) {
                        return subCommand;
                    }
                }
            }
        }
        
        return null;
    }

    public void sendQuickEditCommands(SubCommandContext commandContext, InternalHologram hologram) {
        if (!Configuration.quickEditCommands) {
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

            String usage = "/" + commandContext.getRootLabel() + " " + subCommand.getName() + " " + hologram + " ";
            message.append("[" + quickEditCommand.getActionName() + "]").color(ChatColor.DARK_AQUA)
                    .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, usage))
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(
                            ChatColor.GRAY + "Click to insert in chat the highlighted part of the command:\n" +
                                    ChatColor.YELLOW + usage + ChatColor.DARK_GRAY + usageArgs)));
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
        sender.sendMessage(Colors.PRIMARY_SHADOW + "Server is running " + Colors.PRIMARY + "Holographic Displays " + Colors.PRIMARY_SHADOW + "v" + HolographicDisplays.getInstance().getDescription().getVersion() + " by " + Colors.PRIMARY + "filoghost");
        if (helpCommand.hasPermission(sender)) {
            sender.sendMessage(Colors.PRIMARY_SHADOW + "Commands: " + Colors.PRIMARY + helpCommand.getFullUsageText(context));
        }
    }

    @Override
    protected void sendSubCommandDefaultPermissionMessage(SubCommandContext context) {
        context.getSender().sendMessage(Colors.ERROR + "You don't have permission for this sub-command.");
    }

    @Override
    protected void sendUnknownSubCommandMessage(SubCommandContext context) {
        context.getSender().sendMessage(Colors.ERROR + "Unknown sub-command." 
                + " Type \"" + helpCommand.getFullUsageText(context) + "\" for a list of commands.");
    }

    @Override
    protected void sendSubCommandUsage(SubCommandContext context) {
        context.getSender().sendMessage(Colors.ERROR + "Usage: /" + context.getRootLabel() + " " 
                + context.getSubLabel() + " " + context.getSubCommand().getUsageArgs());
    }

    @Override
    protected void sendExecutionErrorMessage(CommandContext context, String errorMessage) {
        context.getSender().sendMessage(Colors.ERROR + Utils.formatExceptionMessage(errorMessage));
    }

    @Override
    protected void handleUnexpectedException(CommandContext context, Throwable t) {
        Bukkit.getLogger().log(Level.SEVERE, "Unhandled exception while executing /" + context.getRootLabel(), t);
        context.getSender().sendMessage(Colors.ERROR + "Internal error while executing command." 
                + " Please look on the console for more details.");
    }
    
}
