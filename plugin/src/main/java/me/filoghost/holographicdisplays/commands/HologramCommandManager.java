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
import me.filoghost.holographicdisplays.commands.subs.ReadimageCommand;
import me.filoghost.holographicdisplays.commands.subs.ReadtextCommand;
import me.filoghost.holographicdisplays.commands.subs.ReloadCommand;
import me.filoghost.holographicdisplays.commands.subs.RemovelineCommand;
import me.filoghost.holographicdisplays.commands.subs.SetlineCommand;
import me.filoghost.holographicdisplays.commands.subs.TeleportCommand;
import me.filoghost.holographicdisplays.common.Utils;
import me.filoghost.holographicdisplays.disk.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class HologramCommandManager extends SubCommandManager {

    private final List<HologramSubCommand> subCommands;
    private final Map<Class<? extends HologramSubCommand>, HologramSubCommand> subCommandsByClass;
    
    private final HelpCommand helpCommand;

    public HologramCommandManager(ConfigManager configManager) {
        setName("holograms");
        subCommands = new ArrayList<>();
        subCommandsByClass = new HashMap<>();
        
        registerSubCommand(new AddlineCommand(configManager));
        registerSubCommand(new CreateCommand(configManager));
        registerSubCommand(new DeleteCommand(configManager));
        registerSubCommand(new EditCommand());
        registerSubCommand(new ListCommand());
        registerSubCommand(new NearCommand());
        registerSubCommand(new TeleportCommand());
        registerSubCommand(new MovehereCommand(configManager));
        registerSubCommand(new AlignCommand(configManager));
        registerSubCommand(new CopyCommand(configManager));
        registerSubCommand(new ReloadCommand(configManager));
        
        registerSubCommand(new RemovelineCommand(configManager));
        registerSubCommand(new SetlineCommand(configManager));
        registerSubCommand(new InsertlineCommand(configManager));
        registerSubCommand(new ReadtextCommand(configManager));
        registerSubCommand(new ReadimageCommand(configManager));
        registerSubCommand(new InfoCommand());
        
        registerSubCommand(new DebugCommand());
        registerSubCommand(helpCommand = new HelpCommand());
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

    @Override
    public Iterable<HologramSubCommand> getSubCommands() {
        return subCommands;
    }

    public void registerSubCommand(HologramSubCommand subCommand) {
        subCommands.add(subCommand);
        subCommandsByClass.put(subCommand.getClass(), subCommand);
    }
    
    public HologramSubCommand getSubCommand(Class<? extends HologramSubCommand> subCommandClass) {
        return subCommandsByClass.get(subCommandClass);
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
