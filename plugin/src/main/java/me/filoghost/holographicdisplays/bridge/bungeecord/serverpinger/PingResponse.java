/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.bridge.bungeecord.serverpinger;

import me.filoghost.holographicdisplays.disk.ServerAddress;
import me.filoghost.holographicdisplays.common.DebugLogger;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class PingResponse
{
    private boolean isOnline;
    private String motd;
    private int onlinePlayers;
    private int maxPlayers;

    public PingResponse(String jsonString, ServerAddress address) {
        if (jsonString == null || jsonString.isEmpty()) {
            motd = "Invalid ping response";
            DebugLogger.warning("Received empty Json response from IP \"" + address.toString() + "\".");
            return;
        }
        
        Object jsonObject = JSONValue.parse(jsonString);
        
        if (!(jsonObject instanceof JSONObject)) {
            motd = "Invalid ping response";
            DebugLogger.warning("Received invalid Json response from IP \"" + address.toString() + "\": " + jsonString);
            return;
        }
        
        JSONObject json = (JSONObject) jsonObject;
        isOnline = true;
        
        Object descriptionObject = json.get("description");
        
        if (descriptionObject != null) {
            if (descriptionObject instanceof JSONObject) {
                Object text = ((JSONObject) descriptionObject).get("text");
                if (text != null) {
                    motd = text.toString();
                } else {
                    motd = "Invalid ping response (text not found)";
                }
            } else {
                motd = descriptionObject.toString();
            }
        } else {
            motd = "Invalid ping response (description not found)";
            DebugLogger.warning("Received invalid Json response from IP \"" + address.toString() + "\": " + jsonString);
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
