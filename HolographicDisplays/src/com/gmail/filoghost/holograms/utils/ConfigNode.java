package com.gmail.filoghost.holograms.utils;

import org.bukkit.configuration.file.FileConfiguration;


public enum ConfigNode {

	VERTICAL_SPACING("vertical-spacing", 0.25),
	IMAGES_SYMBOL("images.symbol", "[x]"),
	TRANSPARENCY_SPACE("images.transparency.space", " [|] "),
	TRANSPARENCY_COLOR("images.transparency.color", "&7"),
	UPDATE_NOTIFICATION("update-notification", true),
	BUNGEE_REFRESH_SECONDS("bungee-refresh-seconds", 3),
	BUNGEE_ONLINE_FORMAT("bungee-online-format", "&aOnline"),
	BUNGEE_OFFLINE_FORMAT("bungee-offline-format", "&cOffline"),
	TIME_FORMAT("time-format", "H:mm");
	
	private String path;
	private Object value;
	
	private ConfigNode(String path, Object defaultValue) {
		this.path = path;
		value = defaultValue;
	}
	
	public String getPath() {
		return path;
	}
	
	public Object getDefault() {
		return value;
	}
	
	public String getString(FileConfiguration config) {
		return config.getString(path);
	}
	
	public boolean getBoolean(FileConfiguration config) {
		return config.getBoolean(path);
	}
	
	public double getDouble(FileConfiguration config) {
		return config.getDouble(path);
	}
	
	public int getInt(FileConfiguration config) {
		return config.getInt(path);
	}
}
