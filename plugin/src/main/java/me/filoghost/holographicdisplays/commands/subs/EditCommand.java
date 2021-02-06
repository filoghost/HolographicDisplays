/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.commands.subs;

import com.google.common.collect.Lists;
import me.filoghost.fcommons.command.sub.SubCommandContext;
import me.filoghost.fcommons.command.validation.CommandException;
import me.filoghost.holographicdisplays.Colors;
import me.filoghost.holographicdisplays.HolographicDisplays;
import me.filoghost.holographicdisplays.commands.HologramCommandValidate;
import me.filoghost.holographicdisplays.commands.HologramSubCommand;
import me.filoghost.holographicdisplays.commands.Messages;
import me.filoghost.holographicdisplays.disk.Configuration;
import me.filoghost.holographicdisplays.object.NamedHologram;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.ComponentBuilder.FormatRetention;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class EditCommand extends HologramSubCommand {
    
    private static final List<QuickCommandInfo> QUICK_EDIT_COMMANDS = Lists.newArrayList(
        new QuickCommandInfo("Add", AddlineCommand.class),
        new QuickCommandInfo("Remove", RemovelineCommand.class),
        new QuickCommandInfo("Set", SetlineCommand.class),
        new QuickCommandInfo("Insert", InsertlineCommand.class),
        new QuickCommandInfo("View", InfoCommand.class)
    );
    
    public EditCommand() {
        super("edit");
        setMinArgs(1);
        setUsageArgs("<hologram>");
        setDescription("Shows the commands to manipulate an existing hologram.");
    }

    @Override
    public void execute(CommandSender sender, String[] args, SubCommandContext context) throws CommandException {
        NamedHologram hologram = HologramCommandValidate.getNamedHologram(args[0]);
        
        sender.sendMessage("");
        Messages.sendTitle(sender, "How to edit the hologram '" + hologram.getName() + "'");
        for (HologramSubCommand subCommand : HolographicDisplays.getCommandManager().getSubCommands()) {
            if (subCommand instanceof LineEditingCommand) {
                String usage = subCommand.getFullUsageText(context).replace("<hologram>", hologram.getName());

                if (sender instanceof Player) {
                    List<String> help = new ArrayList<>();
                    help.add(Colors.PRIMARY + usage);
                    for (String tutLine : subCommand.getDescription(context)) {
                        help.add(Colors.SECONDARY_SHADOW + tutLine);
                    }
                    
                    ((Player) sender).spigot().sendMessage(new ComponentBuilder(usage)
                        .color(ChatColor.AQUA)
                        .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, usage))
                        .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(String.join("\n", help))))
                        .create());
                    
                } else {
                    sender.sendMessage(Colors.PRIMARY + usage);
                }
            }
        }

        if (sender instanceof Player) {
            HelpCommand.sendHoverTip((Player) sender);
        }
    }

    public static void sendQuickEditCommands(SubCommandContext commandContext, NamedHologram hologram) {
        if (!Configuration.quickEditCommands) {
            return;
        }
        if (!(commandContext.getSender() instanceof Player)) {
            return;
        }
        
        ComponentBuilder message = new ComponentBuilder("EDIT LINES:").color(ChatColor.GRAY).bold(true).append("  ", FormatRetention.NONE);
        
        for (QuickCommandInfo quickEditCommand : QUICK_EDIT_COMMANDS) {
            HologramSubCommand subCommand = HolographicDisplays.getCommandManager().getSubCommand(quickEditCommand.commandClass);
                
            // Assume first argument is always "<hologram>" and remove it
            String usageArgs = subCommand.getUsageArgs();
            if (usageArgs != null && usageArgs.contains(" ")) {
                usageArgs = usageArgs.substring(usageArgs.indexOf(" ") + 1);
            } else {
                usageArgs = "";
            }
            
            String usage = "/" + commandContext.getRootLabel() + " " + subCommand.getName() + " " + hologram + " ";
            message.append("[" + quickEditCommand.chatName + "]").color(ChatColor.DARK_AQUA)
                .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, usage))
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(
                    ChatColor.GRAY + "Click to insert in chat the highlighted part of the command:\n" +
                    ChatColor.YELLOW + usage + ChatColor.DARK_GRAY + usageArgs)));
            message.append("  ", FormatRetention.NONE);
        }
        
        ((Player) commandContext.getSender()).spigot().sendMessage(message.create());
    }
    
    
    private static class QuickCommandInfo {
        
        private final String chatName;
        private final Class<? extends HologramSubCommand> commandClass;
        
        public QuickCommandInfo(String chatName, Class<? extends HologramSubCommand> command) {
            this.chatName = chatName;
            this.commandClass = command;
        }    
        
    }

}
