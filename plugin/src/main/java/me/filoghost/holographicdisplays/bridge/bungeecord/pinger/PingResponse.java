/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.bridge.bungeecord.pinger;

import me.filoghost.holographicdisplays.core.DebugLogger;
import me.filoghost.holographicdisplays.disk.ServerAddress;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class PingResponse {
    
    private final String motd;
    private final int onlinePlayers;
    private final int maxPlayers;

    protected static PingResponse fromJson(String jsonString, ServerAddress address) {
        if (jsonString == null || jsonString.isEmpty()) {
            logInvalidResponse(jsonString, address);
            return errorResponse("Invalid ping response (null or empty)");
        }

        Object jsonObject = JSONValue.parse(jsonString);

        if (!(jsonObject instanceof JSONObject)) {
            logInvalidResponse(jsonString, address);
            return errorResponse("Invalid ping response (wrong format)");
        }

        JSONObject json = (JSONObject) jsonObject;
        
        Object descriptionObject = json.get("description");

        String motd;
        int onlinePlayers = 0;
        int maxPlayers = 0;

        if (descriptionObject == null) {
            logInvalidResponse(jsonString, address);
            return errorResponse("Invalid ping response (description not found)");
        }
        
        if (descriptionObject instanceof JSONObject) {
            Object text = ((JSONObject) descriptionObject).get("text");
            if (text == null) {
                logInvalidResponse(jsonString, address);
                return errorResponse("Invalid ping response (text not found)");
            }
            motd = text.toString();
        } else {
            motd = descriptionObject.toString();
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
        
        return new PingResponse(motd, onlinePlayers, maxPlayers);
    }

    private static void logInvalidResponse(String responseJsonString, ServerAddress address) {
        DebugLogger.warning("Received invalid JSON response from IP \"" + address + "\": " + responseJsonString);
    }

    private static PingResponse errorResponse(String error) {
        return new PingResponse(error, 0, 0);
    }

    private PingResponse(String motd, int onlinePlayers, int maxPlayers) {
        this.motd = motd;
        this.onlinePlayers = onlinePlayers;
        this.maxPlayers = maxPlayers;
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
