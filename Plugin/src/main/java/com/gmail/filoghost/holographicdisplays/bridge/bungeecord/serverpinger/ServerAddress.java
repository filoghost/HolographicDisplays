package com.gmail.filoghost.holographicdisplays.bridge.bungeecord.serverpinger;

public class ServerAddress {
	
	private String ip;
	private int port;
	
	public ServerAddress(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}

	public String getAddress() {
		return ip;
	}

	public int getPort() {
		return port;
	}

	@Override
	public String toString() {
		return ip + ":" + port;
	}
	
}
