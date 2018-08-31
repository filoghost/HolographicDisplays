package com.gmail.filoghost.holographicdisplays.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.gmail.filoghost.holographicdisplays.exception.CommandException;

public class CommandValidator {
	
	public static void notNull(Object obj, String string) throws CommandException {
		if (obj == null) {
			throw new CommandException(string);
		}
	}
	
	public static void isTrue(boolean b, String string) throws CommandException {
		if (!b) {
			throw new CommandException(string);
		}
	}

	public static int getInteger(String integer) throws CommandException {
		try {
			return Integer.parseInt(integer);
		} catch (NumberFormatException ex) {
			throw new CommandException("Invalid number: '" + integer + "'.");
		}
	}
	
	public static boolean isInteger(String integer) {
		try {
			Integer.parseInt(integer);
			return true;
		} catch (NumberFormatException ex) {
			return false;
		}
	}
	
	public static Player getPlayerSender(CommandSender sender) throws CommandException {
		if (sender instanceof Player) {
			return (Player) sender;
		} else {
			throw new CommandException("You must be a player to use this command.");
		}
	}
	
	public static boolean isPlayerSender(CommandSender sender) {
		return sender instanceof Player;
	}
	
}
