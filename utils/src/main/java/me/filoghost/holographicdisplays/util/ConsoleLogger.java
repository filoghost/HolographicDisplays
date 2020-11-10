/*
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package me.filoghost.holographicdisplays.util;

import me.filoghost.holographicdisplays.api.line.HologramLine;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ConsoleLogger {
    
    private static boolean debug;
    private static Logger logger;
    
    public static void setLogger(Logger logger) {
        ConsoleLogger.logger = logger;
    }
    
    public static void setDebugEnabled(boolean enabled) {
        debug = enabled;
    }
    
    public static void log(Level level, String msg, Throwable thrown) {
        if (logger != null) {
            logger.log(level, msg, thrown);
        }
    }
    
    public static void log(Level level, String msg) {
        log(level, msg, null);
    }

    public static void logDebug(Level level, String msg, Throwable thrown) {
        if (debug) {
            log(level, "[Debug] " + msg, thrown);
        }
    }
    
    public static void logDebug(Level level, String msg) {
        logDebug(level, msg, null);
    }
    
    public static void handleSpawnFail(HologramLine parentPiece) {
        logDebug(Level.WARNING, "Coulnd't spawn entity for this hologram: " + parentPiece.getParent().toString());
    }
    
}
