package com.gmail.filoghost.holographicdisplays.util;

import com.gmail.filoghost.holographicdisplays.HolographicDisplays;
import com.gmail.filoghost.holographicdisplays.api.line.HologramLine;
import com.gmail.filoghost.holographicdisplays.disk.Configuration;

public class DebugHandler {

	public static void handleSpawnFail(HologramLine parentPiece) {
		if (Configuration.debug) {
			HolographicDisplays.getInstance().getLogger().warning("[Debug] Coulnd't spawn entity for this hologram: " + parentPiece.getParent().toString());
		}
	}
	
	public static void handleAnimationLoadSuccess(String name, double speed) {
		if (Configuration.debug) {
			HolographicDisplays.getInstance().getLogger().info("[Debug] Successfully loaded animation '"  + name + "', speed = " + speed + ".");
		}
	}

	
	
}
