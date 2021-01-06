/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.commands.main;

import me.filoghost.holographicdisplays.HolographicDisplays;
import me.filoghost.holographicdisplays.Permissions;
import me.filoghost.holographicdisplays.Colors;
import me.filoghost.holographicdisplays.commands.main.subs.AddlineCommand;
import me.filoghost.holographicdisplays.commands.main.subs.AlignCommand;
import me.filoghost.holographicdisplays.commands.main.subs.CopyCommand;
import me.filoghost.holographicdisplays.commands.main.subs.CreateCommand;
import me.filoghost.holographicdisplays.commands.main.subs.DebugCommand;
import me.filoghost.holographicdisplays.commands.main.subs.DeleteCommand;
import me.filoghost.holographicdisplays.commands.main.subs.EditCommand;
import me.filoghost.holographicdisplays.commands.main.subs.HelpCommand;
import me.filoghost.holographicdisplays.commands.main.subs.InfoCommand;
import me.filoghost.holographicdisplays.commands.main.subs.InsertlineCommand;
import me.filoghost.holographicdisplays.commands.main.subs.ListCommand;
import me.filoghost.holographicdisplays.commands.main.subs.MovehereCommand;
import me.filoghost.holographicdisplays.commands.main.subs.NearCommand;
import me.filoghost.holographicdisplays.commands.main.subs.ReadimageCommand;
import me.filoghost.holographicdisplays.commands.main.subs.ReadtextCommand;
import me.filoghost.holographicdisplays.commands.main.subs.ReloadCommand;
import me.filoghost.holographicdisplays.commands.main.subs.RemovelineCommand;
import me.filoghost.holographicdisplays.commands.main.subs.SetlineCommand;
import me.filoghost.holographicdisplays.commands.main.subs.TeleportCommand;
import me.filoghost.holographicdisplays.exception.CommandException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HologramsCommandHandler implements CommandExecutor {

    private List<HologramSubCommand> subCommands;
    private Map<Class<? extends HologramSubCommand>, HologramSubCommand> subCommandsByClass;

    public HologramsCommandHandler() {
        subCommands = new ArrayList<>();
        subCommandsByClass = new HashMap<>();
        
        registerSubCommand(new AddlineCommand());
        registerSubCommand(new CreateCommand());
        registerSubCommand(new DeleteCommand());
        registerSubCommand(new EditCommand());
        registerSubCommand(new ListCommand());
        registerSubCommand(new NearCommand());
        registerSubCommand(new TeleportCommand());
        registerSubCommand(new MovehereCommand());
        registerSubCommand(new AlignCommand());
        registerSubCommand(new CopyCommand());
        registerSubCommand(new ReloadCommand());
        
        registerSubCommand(new RemovelineCommand());
        registerSubCommand(new SetlineCommand());
        registerSubCommand(new InsertlineCommand());
        registerSubCommand(new ReadtextCommand());
        registerSubCommand(new ReadimageCommand());
        registerSubCommand(new InfoCommand());
        
        registerSubCommand(new DebugCommand());
        registerSubCommand(new HelpCommand());
    }
    
    public void registerSubCommand(HologramSubCommand subCommand) {
        subCommands.add(subCommand);
        subCommandsByClass.put(subCommand.getClass(), subCommand);
    }
    
    public List<HologramSubCommand> getSubCommands() {
        return new ArrayList<>(subCommands);
    }
    
    public HologramSubCommand getSubCommand(Class<? extends HologramSubCommand> subCommandClass) {
        return subCommandsByClass.get(subCommandClass);
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(Colors.PRIMARY_SHADOW + "Server is running " + Colors.PRIMARY + "Holographic Displays " + Colors.PRIMARY_SHADOW + "v" + HolographicDisplays.getInstance().getDescription().getVersion() + " by " + Colors.PRIMARY + "filoghost");
            if (sender.hasPermission(Permissions.COMMAND_BASE + "help")) {
                sender.sendMessage(Colors.PRIMARY_SHADOW + "Commands: " + Colors.PRIMARY + "/" + label + " help");
            }
            return true;
        }
        
        for (HologramSubCommand subCommand : subCommands) {
            if (subCommand.isValidTrigger(args[0])) {
                
                if (!subCommand.hasPermission(sender)) {
                    sender.sendMessage(Colors.ERROR + "You don't have permission.");
                    return true;
                }
                
                if (args.length - 1 >= subCommand.getMinimumArguments()) {
                    try {
                        subCommand.execute(sender, label, Arrays.copyOfRange(args, 1, args.length));
                    } catch (CommandException e) {
                        sender.sendMessage(Colors.ERROR + e.getMessage());
                    } catch (Exception e) {
                        e.printStackTrace();
                        sender.sendMessage(Colors.ERROR + "Unhandled exception while executing command. Please look on the console for more details.");
                    }
                } else {
                    sender.sendMessage(Colors.ERROR + "Usage: /" + label + " " + subCommand.getName() + " " + subCommand.getPossibleArguments());
                }
                
                return true;
            }
        }
        
        sender.sendMessage(Colors.ERROR + "Unknown sub-command. Type \"/" + label + " help\" for a list of commands.");
        return true;
    }
}
