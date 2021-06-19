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
package com.gmail.filoghost.holographicdisplays.bridge.bungeecord;

import com.gmail.filoghost.holographicdisplays.disk.Configuration;
import org.bukkit.ChatColor;

public class BungeeServerInfo {

	private volatile boolean isOnline;
	private volatile int onlinePlayers;
	private volatile int maxPlayers;
	
	// The two lines of a motd
	private volatile String motd1; // Should never be null
	private volatile String motd2; // Should never be null
	
	private volatile long lastRequest;

	protected BungeeServerInfo() {
		isOnline = false;
		this.motd1 = "";
		this.motd2 = "";
		updateLastRequest();
	}
	
	public boolean isOnline() {
		return isOnline;
	}

	public void setOnline(boolean isOnline) {
		this.isOnline = isOnline;
	}

	public int getOnlinePlayers() {
		return onlinePlayers;
	}

	public void setOnlinePlayers(int onlinePlayers) {
		this.onlinePlayers = onlinePlayers;
	}
	
	public int getMaxPlayers() {
		return maxPlayers;
	}

	public void setMaxPlayers(int maxPlayers) {
		this.maxPlayers = maxPlayers;
	}

	public String getMotd1() {
		return motd1;
	}
	
	public String getMotd2() {
		return motd2;
	}

	public void setMotd(String motd) {

		if (motd == null) {
			this.motd1 = "";
			this.motd2 = "";
			return;
		}

		int separatorIndex = motd.indexOf("\n");
		if (separatorIndex >= 0) {
			String line1 = motd.substring(0, separatorIndex);
			String line2 = motd.substring(separatorIndex + 1);
			this.motd1 = Configuration.pingerTrimMotd ? trimWithColors(line1) : line1;
			this.motd2 = Configuration.pingerTrimMotd ? trimWithColors(line2) : line2;
		} else {
			this.motd1 = Configuration.pingerTrimMotd ? trimWithColors(motd) : motd;
			this.motd2 = "";
		}
	}

	private static String trimWithColors(String s) {
		if (s == null || s.isEmpty()) {
			return s;
		}

		int firstNonWhitespace = -1;
		int lastNonWhitespace = -1;

		int length = s.length();
		boolean trimWhitespace = true;
		for (int i = 0; i < length; i++) {
			char c = s.charAt(i);

			if (c == ' ' && trimWhitespace) {
				// Ignore space
			} else if (c == ChatColor.COLOR_CHAR && i < length - 1) {
				ChatColor chatColor = ChatColor.getByChar(s.charAt(i + 1));
				if (chatColor == null) {
					continue;
				}
				if (chatColor == ChatColor.STRIKETHROUGH || chatColor == ChatColor.UNDERLINE) {
					trimWhitespace = false;
				} else if (chatColor == ChatColor.RESET) {
					trimWhitespace = true;
				}
				i++;
			} else {
				if (firstNonWhitespace == -1) {
					// Set only once
					firstNonWhitespace = i;
				}
				lastNonWhitespace = i;
			}
		}
		
		if (firstNonWhitespace >= 0) {
			return s.substring(0, firstNonWhitespace).replace(" ", "")
					+ s.substring(firstNonWhitespace, lastNonWhitespace + 1)
					+ s.substring(lastNonWhitespace + 1).replace(" ", "");
		} else {
			return s;
		}
	}

	public long getLastRequest() {
		return lastRequest;
	}

	public void updateLastRequest() {
		this.lastRequest = System.currentTimeMillis();
	}

}
