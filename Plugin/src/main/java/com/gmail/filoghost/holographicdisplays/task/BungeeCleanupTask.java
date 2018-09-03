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
package com.gmail.filoghost.holographicdisplays.task;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import com.gmail.filoghost.holographicdisplays.bridge.bungeecord.BungeeServerInfo;
import com.gmail.filoghost.holographicdisplays.bridge.bungeecord.BungeeServerTracker;
import com.gmail.filoghost.holographicdisplays.util.ConsoleLogger;

/**
 * A task to remove unused server data in the server tracker.
 */
public class BungeeCleanupTask implements Runnable {
	
	private static final long MAX_INACTIVITY = TimeUnit.MINUTES.toMillis(10);

	@Override
	public void run() {
		
		long now = System.currentTimeMillis();
		Iterator<Entry<String, BungeeServerInfo>> iter = BungeeServerTracker.getTrackedServers().entrySet().iterator();
		
		while (iter.hasNext()) {
			Entry<String, BungeeServerInfo> next = iter.next();
			long lastRequest = next.getValue().getLastRequest();
			
			if (lastRequest != 0 && now - lastRequest > MAX_INACTIVITY) {
				// Don't track that server anymore.
				iter.remove();
				ConsoleLogger.logDebug(Level.INFO, "Removed bungee server \"" + next.getKey() + "\" from tracking due to inactivity.");
			}
		}
	}

}
