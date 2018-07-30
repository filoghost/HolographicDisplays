package com.gmail.filoghost.holographicdisplays.task;

import com.gmail.filoghost.holographicdisplays.bridge.bungeecord.BungeeServerInfo;
import com.gmail.filoghost.holographicdisplays.bridge.bungeecord.BungeeServerTracker;
import com.gmail.filoghost.holographicdisplays.util.ConsoleLogger;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

/**
 * A task to remove unused server data in the server tracker.
 */
public class BungeeCleanupTask extends BukkitRunnable {

	private static final long MAX_INACTIVITY = TimeUnit.MINUTES.toMillis(10);

	@Override
	public void run() {
		long now = System.currentTimeMillis();
		Iterator<Entry<String, BungeeServerInfo>> serverIterator = BungeeServerTracker.getTrackedServers().entrySet().iterator();

		while (serverIterator.hasNext()) {
			Entry<String, BungeeServerInfo> next = serverIterator.next();
			long lastRequest = next.getValue().getLastRequest();

			if (lastRequest != 0 && now - lastRequest > MAX_INACTIVITY) {
				// Don't track that server anymore.
				serverIterator.remove();
				ConsoleLogger.info("Removed bungee server \"" + next.getKey() + "\" from tracking due to inactivity.");
			}
		}
	}
}
