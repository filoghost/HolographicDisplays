package com.gmail.filoghost.holograms.object;

import java.util.List;

import com.gmail.filoghost.holograms.nms.interfaces.NameableEntityNMS;
import com.gmail.filoghost.holograms.placeholders.Placeholder;

public class HologramLineData {

	private NameableEntityNMS nameableEntity;
	private String originalName;
	
	private Placeholder[] containedPlaceholders;
	
	private String[] bungeeOnlinePlayers;
	private String[] bungeeStatuses;
	private String[] worldsPlayerCount;
	
	public HologramLineData(NameableEntityNMS nameableEntity, String originalName) {
		this.nameableEntity = nameableEntity;
		this.originalName = originalName;
	}
	
	public void setContainedPlaceholders(List<Placeholder> list) {
		containedPlaceholders = new Placeholder[list.size()];
		containedPlaceholders = list.toArray(containedPlaceholders);
	}
	
	public void setBungeeOnlinePlayersToCheck(List<String> list) {
		bungeeOnlinePlayers = new String[list.size()];
		bungeeOnlinePlayers = list.toArray(bungeeOnlinePlayers);
	}
	
	public void setBungeeStatusesToCheck(List<String> list) {
		bungeeStatuses = new String[list.size()];
		bungeeStatuses = list.toArray(bungeeStatuses);
	}
	
	public void setWorldsCountToCheck(List<String> list) {
		worldsPlayerCount = new String[list.size()];
		worldsPlayerCount = list.toArray(worldsPlayerCount);
	}

	public NameableEntityNMS getNameableEntity() {
		return nameableEntity;
	}

	public String getSavedName() {
		return originalName;
	}
	
	public boolean hasPlaceholders() {
		return containedPlaceholders != null;
	}
	
	public boolean hasBungeeOnlinePlayersToCheck() {
		return bungeeOnlinePlayers != null;
	}
	
	public boolean hasBungeeStatusesToCheck() {
		return bungeeStatuses != null;
	}
	
	public boolean hasWorldsCountToCheck() {
		return worldsPlayerCount != null;
	}
	
	/**
	 * Can be null.
	 */
	public Placeholder[] getPlaceholders() {
		return containedPlaceholders;
	}
	
	/**
	 * Can be null.
	 */
	public String[] getBungeeOnlinePlayersToCheck() {
		return bungeeOnlinePlayers;
	}
	
	/**
	 * Can be null.
	 */
	public String[] getBungeeStatusesToCheck() {
		return bungeeStatuses;
	}
	
	/**
	 * Can be null.
	 */
	public String[] getWorldsPlayersCountToCheck() {
		return worldsPlayerCount;
	}
}
