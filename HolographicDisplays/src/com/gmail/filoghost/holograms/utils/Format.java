package com.gmail.filoghost.holograms.utils;

import org.bukkit.command.CommandSender;

public class Format {

	public static final String HIGHLIGHT = "§b";
	
	public static String formatTitle(String input) {
		return "§3§l----- " + input + " §3§l-----";
	}
	
	public static void sendWarning(CommandSender recipient, String warning) {
		recipient.sendMessage("§c( §4§l!§c ) §7" + warning);
	}
}
