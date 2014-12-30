package com.gmail.filoghost.holographicdisplays.bridge.bungeecord;

public class BungeeServerInfo {

	private boolean isOnline;
	private int onlinePlayers;
	private int maxPlayers;
	
	// The two lines of a motd
	private String motd1; // Should never be null
	private String motd2; // Should never be null
	
	private long lastRequest;

	protected BungeeServerInfo() {
		isOnline = true;
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
		
		if (motd.contains("\n")) {
			String[] split = motd.split("\n");
			this.motd1 = split[0];
			this.motd2 = split[1];
		} else {
			this.motd1 = motd;
			this.motd2 = "";
		}
	}

	public long getLastRequest() {
		return lastRequest;
	}

	public void updateLastRequest() {
		this.lastRequest = System.currentTimeMillis();
	}

}
