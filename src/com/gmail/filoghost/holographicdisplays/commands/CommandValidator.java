package com.gmail.filoghost.holographicdisplays.commands;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.gmail.filoghost.holographicdisplays.exception.CommandException;
import com.gmail.filoghost.holographicdisplays.util.ItemUtils;

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
	
	@SuppressWarnings("deprecation")
	public static ItemStack matchItemStack(String input) throws CommandException {

		input = input.replace(" ", ""); // Remove the spaces
		
		int dataValue = 0;
		if (input.contains(":")) {
			String[] split = input.split(":", 2);
			dataValue = getInteger(split[1]);
			input = split[0];
		}
		
		Material match = null;
		if (isInteger(input)) {
			int id = getInteger(input);
			for (Material mat : Material.values()) {
				if (mat.getId() == id) {
					match = mat;
					break;
				}
			}
		} else {
			match = ItemUtils.matchMaterial(input);
		}
		
		if (match == null || match == Material.AIR) {
			throw new CommandException("Invalid material: " + input);
		}
		
		return new ItemStack(match, 1, (short) dataValue);
	}
	
}
