package com.gmail.filoghost.holographicdisplays.util;

import org.bukkit.plugin.Plugin;

import com.gmail.filoghost.holographicdisplays.api.line.HologramLine;

public class DebugHandler {
	
	private static boolean debug;
	private static Plugin plugin;
	
	public static void setDebugEnabled(boolean enabled) {
		debug = enabled;
	}

	public static void logToConsole(String msg) {
		if (debug) {
			plugin.getLogger().info("[Debug] " + msg);
		}
	}
	
	public static void handleAnimationLoadSuccess(String name, double speed) {
		logToConsole("Successfully loaded animation '"  + name + "', speed = " + speed + ".");
	}
	
	public static void handleSpawnFail(HologramLine parentPiece) {
		if (debug) {
			plugin.getLogger().severe("[Debug] Coulnd't spawn entity for this hologram: " + parentPiece.getParent().toString());
		}
	}

	public static void handleDebugException(Exception e) {
		if (debug) {
			e.printStackTrace();
		}
	}

	
	
}
