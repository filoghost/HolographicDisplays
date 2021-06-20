/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.common;

import me.filoghost.fcommons.logging.Log;
import me.filoghost.holographicdisplays.common.hologram.StandardHologramLine;
import me.filoghost.holographicdisplays.common.nms.SpawnFailedException;

public class DebugLogger {
    
    private static boolean debug;
    private static final String PREFIX = "[Debug] ";

    public static void setDebugEnabled(boolean enabled) {
        debug = enabled;
    }

    public static void info(String msg) {
        info(msg, null);
    }

    public static void info(String msg, Throwable thrown) {
        if (debug) {
            Log.info(PREFIX + msg, thrown);
        }
    }
    
    public static void warning(String msg) {
        warning(msg, null);
    }

    public static void warning(String msg, Throwable thrown) {
        if (debug) {
            Log.warning(PREFIX + msg, thrown);
        }
    }
    
    public static void severe(String msg) {
        severe(msg, null);
    }

    public static void severe(String msg, Throwable thrown) {
        if (debug) {
            Log.severe(PREFIX + msg, thrown);
        }
    }
    
    public static void handleSpawnFail(SpawnFailedException exception, StandardHologramLine parentHologramLine) {
        severe("Couldn't spawn entity for this hologram: " + parentHologramLine.getHologram(), exception);
    }

    public static void cannotSetPassenger(Throwable t) {
        severe("Couldn't set passenger", t);
    }

    public static void cannotSetArmorStandAsMarker(Throwable t) {
        severe("Couldn't set armor stand as marker", t);
    }

    public static void cannotSetPassengerPitchYawDelta(Throwable t) {
        severe("Couldn't set passenger pitch/yaw delta", t);
    }

}
