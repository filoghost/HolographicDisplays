package com.gmail.filoghost.holographicdisplays.bridge.bungeecord.serverpinger;

import java.lang.Override;
import java.lang.String;

import org.json.simple.JSONObject;

public class ServerStatus
{
	private boolean isOnline;
    private String motd;
    private int onlinePlayers;
    private int maxPlayers;
    
    public ServerStatus(boolean isOnline, String motd, int onlinePlayers, int maxPlayers) {
		this.isOnline = isOnline;
		this.motd = motd;
		this.onlinePlayers = onlinePlayers;
		this.maxPlayers = maxPlayers;
	}

	public ServerStatus(JSONObject json) {
    	isOnline = true;
        motd = ((String) json.get("description")).replace("\n", "");
        
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

	@Override
	public String toString() {
		return "ServerStatus [motd=" + motd + ", onlinePlayers=" + onlinePlayers + ", maxPlayers=" + maxPlayers + "]";
	}
    
    

}
