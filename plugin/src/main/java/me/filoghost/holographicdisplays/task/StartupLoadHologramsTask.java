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
import me.filoghost.holographicdisplays.object.internal.InternalHologram;
import me.filoghost.holographicdisplays.object.internal.InternalHologramManager;

import java.util.Collection;

public class StartupLoadHologramsTask implements Runnable {

    private final InternalHologramManager internalHologramManager;
    private final Collection<HologramConfig> hologramConfigsToLoad;

    public StartupLoadHologramsTask(InternalHologramManager internalHologramManager, Collection<HologramConfig> hologramConfigsToLoad) {
        this.internalHologramManager = internalHologramManager;
        this.hologramConfigsToLoad = hologramConfigsToLoad;
    }

    @Override
    public void run() {
        for (HologramConfig hologramConfig : hologramConfigsToLoad) {
            try {
                InternalHologram hologram = hologramConfig.createHologram(internalHologramManager);
                hologram.refresh();
            } catch (HologramLoadException e) {
                Log.warning(Utils.formatExceptionMessage(e));
            } catch (Exception e) {
                Log.warning("Unexpected exception while loading the hologram \"" + hologramConfig.getName() + "\"."
                        + " Please contact the developer.", e);
            }
        }
    }

}
