/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.task;

import me.filoghost.fcommons.logging.Log;
import me.filoghost.holographicdisplays.common.Utils;
import me.filoghost.holographicdisplays.disk.HologramConfig;
import me.filoghost.holographicdisplays.disk.HologramLoadException;
import me.filoghost.holographicdisplays.object.NamedHologram;
import me.filoghost.holographicdisplays.object.NamedHologramManager;

import java.util.Collection;

public class StartupLoadHologramsTask implements Runnable {

    private Collection<HologramConfig> hologramConfigsToLoad;

    public StartupLoadHologramsTask(Collection<HologramConfig> hologramConfigsToLoad) {
        this.hologramConfigsToLoad = hologramConfigsToLoad;
    }

    @Override
    public void run() {
        for (HologramConfig hologramConfig : hologramConfigsToLoad) {
            try {
                NamedHologram hologram = hologramConfig.createHologram();
                NamedHologramManager.addHologram(hologram);
                hologram.refreshAll();
            } catch (HologramLoadException e) {
                Log.warning(Utils.formatExceptionMessage(e));
            } catch (Exception e) {
                Log.warning("Unexpected exception while loading the hologram \"" + hologramConfig.getName() + "\"."
                        + " Please contact the developer.", e);
            }
        }
    }

}
