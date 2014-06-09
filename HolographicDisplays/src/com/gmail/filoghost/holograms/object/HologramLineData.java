package com.gmail.filoghost.holograms.object;

import java.util.List;

import com.gmail.filoghost.holograms.nms.interfaces.HologramHorse;
import com.gmail.filoghost.holograms.placeholders.Placeholder;

public class HologramLineData {

	private HologramHorse horse;
	private String originalName;
	
	private Placeholder[] containedPlaceholders;
	
	private String[] bungeeOnlinePlayers;
	private String[] bungeeStatuses;
	
	public HologramLineData(HologramHorse horse, String originalName) {
		this.horse = horse;
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

	public HologramHorse getHorse() {
		return horse;
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
}
