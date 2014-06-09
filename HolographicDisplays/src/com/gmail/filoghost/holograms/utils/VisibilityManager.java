package com.gmail.filoghost.holograms.utils;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.entity.Player;

public class VisibilityManager {

	Set<String> whoCanSee;

	public VisibilityManager() {
		
	}
	
	public VisibilityManager(Set<String> whoCanSee) {
		this.whoCanSee = whoCanSee;
	}
	
	public void showTo(Player player) {
		if (whoCanSee == null) {
			whoCanSee = new HashSet<String>();
		}
		
		whoCanSee.add(player.getName());
	}
	
	public void hideTo(Player player) {
		if (whoCanSee != null) {
			whoCanSee.remove(player.getName());
		}
	}
	
	public boolean isVisibleTo(Player player) {
		return whoCanSee != null && whoCanSee.contains(player.getName());
	}
}
