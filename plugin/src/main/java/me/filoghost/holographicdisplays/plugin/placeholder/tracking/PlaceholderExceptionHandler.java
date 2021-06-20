/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.placeholder.tracking;

import me.filoghost.fcommons.logging.Log;
import me.filoghost.holographicdisplays.plugin.placeholder.PlaceholderException;
import me.filoghost.holographicdisplays.plugin.placeholder.TickClock;
import me.filoghost.holographicdisplays.plugin.placeholder.registry.PlaceholderExpansion;

import java.util.Map;
import java.util.WeakHashMap;

class PlaceholderExceptionHandler {

    private final TickClock tickClock;
    private final Map<PlaceholderExpansion, Long> lastErrorLogByPlaceholderExpansion;

    PlaceholderExceptionHandler(TickClock tickClock) {
        this.tickClock = tickClock;
        this.lastErrorLogByPlaceholderExpansion = new WeakHashMap<>();
    }

    void handle(PlaceholderException exception) {
        PlaceholderExpansion placeholderExpansion = exception.getPlaceholderExpansion();
        Long lastErrorLog = lastErrorLogByPlaceholderExpansion.get(placeholderExpansion);
        long currentTick = tickClock.getCurrentTick();

        if (lastErrorLog != null && currentTick - lastErrorLog < 20) {
            return; // Avoid spamming the console too frequently
        }

        lastErrorLogByPlaceholderExpansion.put(placeholderExpansion, currentTick);

        Log.warning("The placeholder \"" + placeholderExpansion.getIdentifier() + "\""
                        + " registered by the plugin " + placeholderExpansion.getPluginName()
                        + " generated an exception."
                        + " Please contact the author of " + placeholderExpansion.getPluginName(),
                exception.getCause());
    }

}
