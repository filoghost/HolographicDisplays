package com.gmail.filoghost.holographicdisplays.disk;

public enum ConfigNode {

	SPACE_BETWEEN_LINES("space-between-lines", 0.02),
	IMAGES_SYMBOL("images.symbol", "[x]"),
	TRANSPARENCY_SPACE("images.transparency.space", " [|] "),
	TRANSPARENCY_COLOR("images.transparency.color", "&7"),
	UPDATE_NOTIFICATION("update-notification", true),
	BUNGEE_REFRESH_SECONDS("bungee.refresh-seconds", 3),
	BUNGEE_USE_REDIS_BUNGEE("bungee.use-RedisBungee", false),
	TIME_FORMAT("time.format", "H:mm"),
	TIME_ZONE("time.zone", "GMT+1"),
	DEBUG("debug", false);
	
	private final String path;
	private final Object value;
	
	private ConfigNode(String path, Object defaultValue) {
		this.path = path;
		value = defaultValue;
	}
	
	public String getPath() {
		return path;
	}
	
	public Object getDefaultValue() {
		return value;
	}
	
}