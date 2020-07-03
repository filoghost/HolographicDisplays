package com.gmail.filoghost.holographicdisplays.util.nbt.parser;

import net.md_5.bungee.api.ChatColor;

import java.io.IOException;

public class MojangsonParseException extends IOException {

	private static final long serialVersionUID = 1L;

	public MojangsonParseException(String msg, String content, int index) {
		super(msg + " at character " + index + ": " + printErrorLoc(content, index));
	}

	private static String printErrorLoc(String content, int index) {
		StringBuilder builder = new StringBuilder();
		int i = Math.min(content.length(), index);
		if (i > 35) {
			builder.append("...");
		}
		builder.append(content.substring(Math.max(0, i - 35), i));
		builder.append(ChatColor.GOLD + "<--[HERE]");

		return builder.toString();
	}

}
