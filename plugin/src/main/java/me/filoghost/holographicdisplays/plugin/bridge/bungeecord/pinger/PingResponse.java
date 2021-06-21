/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.bridge.bungeecord.pinger;

import me.filoghost.holographicdisplays.common.DebugLogger;
import me.filoghost.holographicdisplays.plugin.disk.ServerAddress;
import net.md_5.bungee.chat.ComponentSerializer;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class PingResponse {

    private final String motd;
    private final int onlinePlayers;
    private final int maxPlayers;

    private PingResponse(String motd, int onlinePlayers, int maxPlayers) {
        this.motd = motd;
        this.onlinePlayers = onlinePlayers;
        this.maxPlayers = maxPlayers;
    }

    protected static PingResponse fromJson(String jsonString, ServerAddress address) {
        if (jsonString == null || jsonString.isEmpty()) {
            return errorResponse("Invalid ping response (null or empty)", jsonString, address);
        }

        Object jsonObject = JSONValue.parse(jsonString);

        if (!(jsonObject instanceof JSONObject)) {
            return errorResponse("Invalid ping response (wrong format)", jsonString, address);
        }

        JSONObject json = (JSONObject) jsonObject;

        Object descriptionObject = json.get("description");

        String motd;
        int onlinePlayers = 0;
        int maxPlayers = 0;

        if (descriptionObject == null) {
            return errorResponse("Invalid ping response (description not found)", jsonString, address);
        }

        if (descriptionObject instanceof JSONObject) {
            String descriptionString = ((JSONObject) descriptionObject).toJSONString();
            try {
                motd = ComponentSerializer.parse(descriptionString)[0].toLegacyText();
            } catch (Exception e) {
                return errorResponse("Invalid ping response (could not parse description)", jsonString, address);
            }
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

    private static PingResponse errorResponse(String error, String jsonString, ServerAddress address) {
        DebugLogger.warning("Received invalid JSON response from IP \"" + address + "\": " + jsonString);
        return new PingResponse(error, 0, 0);
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
