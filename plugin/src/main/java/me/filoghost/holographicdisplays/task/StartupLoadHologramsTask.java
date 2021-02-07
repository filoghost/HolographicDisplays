/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.task;

import me.filoghost.fcommons.logging.Log;
import me.filoghost.holographicdisplays.common.Utils;
import me.filoghost.holographicdisplays.disk.HologramDatabase;
import me.filoghost.holographicdisplays.disk.HologramLoadException;
import me.filoghost.holographicdisplays.object.NamedHologram;
import me.filoghost.holographicdisplays.object.NamedHologramManager;

import java.util.Set;

public class StartupLoadHologramsTask implements Runnable {

    @Override
    public void run() {
        Set<String> savedHologramsNames = HologramDatabase.getHolograms();
        if (savedHologramsNames != null) {
            for (String hologramName : savedHologramsNames) {
                try {
                    NamedHologram namedHologram = HologramDatabase.loadHologram(hologramName);
                    NamedHologramManager.addHologram(namedHologram);
                    namedHologram.refreshAll();
                } catch (HologramLoadException e) {
                    Log.warning(Utils.formatExceptionMessage(e));
                } catch (Exception e) {
                    Log.warning("Unexpected exception while loading the hologram \"" + hologramName + "\"." 
                            + " Please contact the developer.", e);
                }
            }
        }
    }

}
