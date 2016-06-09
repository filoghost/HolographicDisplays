package com.gmail.filoghost.holographicdisplays.task;

import java.util.Iterator;
import java.util.Map.Entry;

import com.gmail.filoghost.holographicdisplays.bridge.bungeecord.BungeeServerInfo;
import com.gmail.filoghost.holographicdisplays.bridge.bungeecord.BungeeServerTracker;
import com.gmail.filoghost.holographicdisplays.util.DebugHandler;

/**
 * A task to remove unused server data in the server tracker.
 */
public class BungeeCleanupTask implements Runnable {

	@Override
	public void run() {
		
		Iterator<Entry<String, BungeeServerInfo>> iter = BungeeServerTracker.getTrackedServers().entrySet().iterator();
		
		while (iter.hasNext()) {
			Entry<String, BungeeServerInfo> next = iter.next();
			long lastRequest = next.getValue().getLastRequest();
			
			if (lastRequest != 0 && System.currentTimeMillis() - lastRequest > 600000) { // 10 * 60 * 1000 = 10 minutes.
				// Don't track that server anymore.
				iter.remove();
				DebugHandler.logToConsole("Removed bungee server \"" + next.getKey() + "\" from tracking due to inactivity.");
			}
		}
	}

}
