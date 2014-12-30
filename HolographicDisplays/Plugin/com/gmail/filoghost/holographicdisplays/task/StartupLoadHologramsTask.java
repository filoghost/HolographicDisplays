package com.gmail.filoghost.holographicdisplays.task;

import com.gmail.filoghost.holographicdisplays.object.NamedHologram;
import com.gmail.filoghost.holographicdisplays.object.NamedHologramManager;

public class StartupLoadHologramsTask implements Runnable {

	@Override
	public void run() {
		for (NamedHologram hologram : NamedHologramManager.getHolograms()) {
			hologram.refreshAll();
		}
	}

}
