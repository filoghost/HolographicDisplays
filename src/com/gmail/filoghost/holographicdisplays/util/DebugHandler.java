package com.gmail.filoghost.holographicdisplays.util;

import com.gmail.filoghost.holographicdisplays.HolographicDisplays;
import com.gmail.filoghost.holographicdisplays.api.line.HologramLine;
import com.gmail.filoghost.holographicdisplays.disk.Configuration;

public class DebugHandler {

	public static void logToConsole(String msg) {
		if (Configuration.debug) {
			HolographicDisplays.getInstance().getLogger().info("[Debug] " + msg);
		}
	}
	
	public static void handleAnimationLoadSuccess(String name, double speed) {
		logToConsole("Successfully loaded animation '"  + name + "', speed = " + speed + ".");
	}
	
	public static void handleSpawnFail(HologramLine parentPiece) {
		if (Configuration.debug) {
			HolographicDisplays.getInstance().getLogger().severe("[Debug] Coulnd't spawn entity for this hologram: " + parentPiece.getParent().toString());
		}
	}

	public static void handleDebugException(Exception e) {
		if (Configuration.debug) {
			e.printStackTrace();
		}
	}

	
	
}
