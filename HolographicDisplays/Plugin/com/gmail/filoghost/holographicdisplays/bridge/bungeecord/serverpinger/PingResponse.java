package com.gmail.filoghost.holographicdisplays.bridge.bungeecord.serverpinger;

import java.lang.String;

import org.json.simple.JSONObject;

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

	public PingResponse(JSONObject json) {
    	isOnline = true;
        motd = ((String) json.get("description"));
        
        JSONObject playersJson = (JSONObject) json.get("players");
        onlinePlayers = ((Long) playersJson.get("online")).intValue();
        maxPlayers = ((Long) playersJson.get("max")).intValue();
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
