package com.gmail.filoghost.holographicdisplays.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class Strings {

	public static final String NO_SUCH_HOLOGRAM = ChatColor.RED + "A hologram with that name doesn't exist.";

	public static final String BASE_PERM = "holograms.";
	
	public static final String TIP_PREFIX = "" + ChatColor.YELLOW + ChatColor.BOLD + "TIP" + Colors.SECONDARY_SHADOW + " ";
	
	
	public static String formatTitle(String input) {
		return "" + Colors.PRIMARY_SHADOW + ChatColor.BOLD + "----- " + input + Colors.PRIMARY_SHADOW + ChatColor.BOLD + " -----";
	}
	
	public static void sendWarning(CommandSender recipient, String warning) {
		recipient.sendMessage(ChatColor.RED + "( " + ChatColor.DARK_RED + ChatColor.BOLD + "!" + ChatColor.RED + " ) " + Colors.SECONDARY_SHADOW + warning);
	}
	
}
