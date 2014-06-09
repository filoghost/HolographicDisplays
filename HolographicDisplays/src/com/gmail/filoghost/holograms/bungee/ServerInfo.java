package com.gmail.filoghost.holograms.bungee;

public class ServerInfo {

	private int onlinePlayers;
	private boolean isOnline;
	private long lastPing;
	private long lastRequest;

	public ServerInfo(int onlinePlayers, boolean isOnline, long lastPing) {
		this.onlinePlayers = onlinePlayers;
		this.isOnline = isOnline;
		this.lastPing = lastPing;
	}

	public int getOnlinePlayers() {
		return onlinePlayers;
	}

	public void setOnlinePlayers(int onlinePlayers) {
		this.onlinePlayers = onlinePlayers;
	}

	public long getLastPing() {
		return lastPing;
	}

	public void setLastPing(long lastPing) {
		this.lastPing = lastPing;
	}

	public long getLastRequest() {
		return lastRequest;
	}

	public void setLastRequest(long lastRequest) {
		this.lastRequest = lastRequest;
	}
	
	public void setOnline(boolean online) {
		this.isOnline = online;
	}
	
	public boolean isOnline() {
		return isOnline;
	}
}
