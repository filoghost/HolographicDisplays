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
package com.gmail.filoghost.holographicdisplays.bridge.bungeecord.serverpinger;

import com.gmail.filoghost.holographicdisplays.disk.ServerAddress;
import com.gmail.filoghost.holographicdisplays.util.ConsoleLogger;
import net.md_5.bungee.chat.ComponentSerializer;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.util.logging.Level;

public class PingResponse
{
	private boolean isOnline;
    private String motd;
    private int onlinePlayers;
    private int maxPlayers;
    
    public PingResponse(boolean isOnline, String motd, int onlinePlayers, int maxPlayers) {
		this.isOnline = isOnline;
		this.motd = motd;
		this.onlinePlayers = onlinePlayers;
		this.maxPlayers = maxPlayers;
	}

	public PingResponse(String jsonString, ServerAddress address) {
		
		if (jsonString == null || jsonString.isEmpty()) {
    		motd = "Invalid ping response";
    		ConsoleLogger.logDebug(Level.WARNING, "Received empty Json response from IP \"" + address.toString() + "\"!");
    		return;
    	}
		
		Object jsonObject = JSONValue.parse(jsonString);
		
    	if (!(jsonObject instanceof JSONObject)) {
    		motd = "Invalid ping response";
    		ConsoleLogger.logDebug(Level.WARNING, "Received invalid Json response from IP \"" + address.toString() + "\": " + jsonString);
    		return;
    	}
    	
    	JSONObject json = (JSONObject) jsonObject;
    	isOnline = true;
    	
    	Object descriptionObject = json.get("description");
    	
    	if (descriptionObject != null) {
    		if (descriptionObject instanceof JSONObject) {
				String descriptionString = ((JSONObject) descriptionObject).toJSONString();
				try {
					motd = ComponentSerializer.parse(descriptionString)[0].toLegacyText();
				} catch (Exception e) {
					ConsoleLogger.log(Level.WARNING, "Could not parse ping response: " + descriptionString, e);
					motd = "Invalid ping response";
				}
    		} else {
    			motd = descriptionObject.toString();
    		}
    	} else {
    		motd = "Invalid ping response (description not found)";
    		ConsoleLogger.logDebug(Level.WARNING, "Received invalid Json response from IP \"" + address.toString() + "\": " + jsonString);
    	}
        
        Object playersObject = json.get("players");

		if (playersObject instanceof JSONObject) {
			JSONObject playersJson = (JSONObject) playersObject;
			
			Object onlineObject = playersJson.get("online");
			if (onlineObject instanceof Number) {
				onlinePlayers = ((Number) onlineObject).intValue();
			}
			
			Object maxObject = playersJson.get("max");
			if (maxObject instanceof Number) {
				maxPlayers = ((Number) maxObject).intValue();
			}
        }
    }

	public boolean isOnline() {
		return isOnline;
	}

	public String getMotd() {
		return motd;
	}

	public int getOnlinePlayers() {
		return onlinePlayers;
	}

	public int getMaxPlayers() {
		return maxPlayers;
	}

}
