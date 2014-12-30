package com.gmail.filoghost.holographicdisplays.bridge.bungeecord;

public class BungeeServerInfo {

	private boolean isOnline;
	private int onlinePlayers;
	private int maxPlayers;
	private String motd; // Should never be null
	private long lastRequest;

	protected BungeeServerInfo() {
		isOnline = true;
		this.motd = "";
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

	public String getMotd() {
		return motd;
	}

	public void setMotd(String motd) {
		if (motd == null) {
			motd = "";
		}
		
		this.motd = motd;
	}

	public long getLastRequest() {
		return lastRequest;
	}

	public void updateLastRequest() {
		this.lastRequest = System.currentTimeMillis();
	}

}
