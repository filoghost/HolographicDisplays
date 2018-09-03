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
package com.gmail.filoghost.holographicdisplays.disk;

import java.util.Arrays;

public enum ConfigNode {

	SPACE_BETWEEN_LINES("space-between-lines", 0.02),
	PRECISE_HOLOGRAM_MOVEMENT("precise-hologram-movement", true),
	IMAGES_SYMBOL("images.symbol", "[x]"),
	TRANSPARENCY_SPACE("images.transparency.space", " [|] "),
	TRANSPARENCY_COLOR("images.transparency.color", "&7"),
	UPDATE_NOTIFICATION("update-notification", true),
	BUNGEE_REFRESH_SECONDS("bungee.refresh-seconds", 3),
	BUNGEE_USE_REDIS_BUNGEE("bungee.use-RedisBungee", false),
	BUNGEE_USE_FULL_PINGER("bungee.pinger.enable", false),
	BUNGEE_PINGER_TIMEOUT("bungee.pinger.timeout", 500),
	BUNGEE_PINGER_OFFLINE_MOTD("bungee.pinger.offline-motd", "&cOffline, couldn't get the MOTD."),
	BUNGEE_PINGER_ONLINE_FORMAT("bungee.pinger.status.online", "&aOnline"),
	BUNGEE_PINGER_OFFLINE_FORMAT("bungee.pinger.status.offline", "&cOffline"),
	BUNGEE_PINGER_TRIM_MOTD("bungee.pinger.motd-remove-leading-trailing-spaces", true),
	BUNGEE_PINGER_SERVERS("bungee.pinger.servers", Arrays.asList("hub: 127.0.0.1:25565", "survival: 127.0.0.1:25566", "minigames: 127.0.0.1:25567")),
	TIME_FORMAT("time.format", "H:mm"),
	TIME_ZONE("time.zone", "GMT+1"),
	DEBUG("debug", false);
	
	private final String path;
	private final Object value;
	
	private ConfigNode(String path, Object defaultValue) {
		this.path = path;
		value = defaultValue;
	}
	
	public String getPath() {
		return path;
	}
	
	public Object getDefaultValue() {
		return value;
	}
	
}
