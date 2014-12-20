package com.gmail.filoghost.holographicdisplays.bridge.bungeecord;

public class BungeeServerInfo {

	private int onlinePlayers;
	private long lastRequest;

	protected BungeeServerInfo(int onlinePlayers, long lastRequest) {
		this.onlinePlayers = onlinePlayers;
		this.lastRequest = lastRequest;
	}

	public int getOnlinePlayers() {
		return onlinePlayers;
	}

	public void setOnlinePlayers(int onlinePlayers) {
		this.onlinePlayers = onlinePlayers;
	}

	public long getLastRequest() {
		return lastRequest;
	}

	public void setLastRequest(long lastRequest) {
		this.lastRequest = lastRequest;
	}

}
