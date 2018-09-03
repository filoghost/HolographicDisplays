/*
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package com.gmail.filoghost.holographicdisplays.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class Strings {

	public static final String BASE_PERM = "holograms.";
	
	public static final String TIP_PREFIX = "" + ChatColor.YELLOW + ChatColor.BOLD + "TIP" + Colors.SECONDARY_SHADOW + " ";
	
	
	public static String formatTitle(String input) {
		return "" + Colors.PRIMARY_SHADOW + ChatColor.BOLD + "----- " + input + Colors.PRIMARY_SHADOW + ChatColor.BOLD + " -----";
	}
	
	public static String noSuchHologram(String name) {
		return ChatColor.RED + "Cannot find a hologram named \"" + name + "\".";
	}
	
	public static void sendWarning(CommandSender recipient, String warning) {
		recipient.sendMessage(ChatColor.RED + "( " + ChatColor.DARK_RED + ChatColor.BOLD + "!" + ChatColor.RED + " ) " + Colors.SECONDARY_SHADOW + warning);
	}
	
}
