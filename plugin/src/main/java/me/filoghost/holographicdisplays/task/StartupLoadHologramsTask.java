/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.task;

import me.filoghost.fcommons.logging.Log;
import me.filoghost.holographicdisplays.disk.HologramDatabase;
import me.filoghost.holographicdisplays.exception.HologramLineParseException;
import me.filoghost.holographicdisplays.exception.HologramNotFoundException;
import me.filoghost.holographicdisplays.exception.InvalidFormatException;
import me.filoghost.holographicdisplays.exception.WorldNotFoundException;
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
                } catch (HologramNotFoundException e) {
                    Log.warning("Hologram '" + hologramName + "' not found, skipping it.");
                } catch (InvalidFormatException e) {
                    Log.warning("Hologram '" + hologramName + "' has an invalid location format.");
                } catch (WorldNotFoundException e) {
                    Log.warning("Hologram '" + hologramName + "' was in the world '" + e.getMessage() + "' but it wasn't loaded.");
                } catch (HologramLineParseException e) {
                    Log.warning("Hologram '" + hologramName + "' has an invalid line: " + e.getMessage());
                } catch (Exception e) {
                    Log.warning("Unhandled exception while loading the hologram '" + hologramName + "'. Please contact the developer.", e);
                }
            }
        }
    }

}
