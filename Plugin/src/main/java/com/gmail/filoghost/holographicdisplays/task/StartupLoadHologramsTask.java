package com.gmail.filoghost.holographicdisplays.task;

import com.gmail.filoghost.holographicdisplays.object.NamedHologram;
import com.gmail.filoghost.holographicdisplays.object.NamedHologramManager;
import org.bukkit.scheduler.BukkitRunnable;

public class StartupLoadHologramsTask extends BukkitRunnable {

	@Override
	public void run() {
		for (NamedHologram hologram : NamedHologramManager.getHolograms()) {
			hologram.refreshAll();
		}
	}
}
