/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.placeholder.tracking;

import me.filoghost.fcommons.collection.CaseInsensitiveString;
import me.filoghost.fcommons.logging.Log;
import me.filoghost.holographicdisplays.plugin.placeholder.PlaceholderException;
import me.filoghost.holographicdisplays.plugin.placeholder.PlaceholderOccurrence;
import me.filoghost.holographicdisplays.plugin.tick.TickClock;

import java.util.Map;
import java.util.WeakHashMap;

class PlaceholderExceptionHandler {

    private final TickClock tickClock;
    private final Map<CaseInsensitiveString, Long> lastErrorLogByPlaceholderContent;

    PlaceholderExceptionHandler(TickClock tickClock) {
        this.tickClock = tickClock;
        this.lastErrorLogByPlaceholderContent = new WeakHashMap<>();
    }

    void handle(PlaceholderException exception, PlaceholderOccurrence placeholderOccurrence) {
        CaseInsensitiveString unparsedContent = placeholderOccurrence.getUnparsedContent();
        Long lastErrorLog = lastErrorLogByPlaceholderContent.get(unparsedContent);
        long currentTick = tickClock.getCurrentTick();

        if (lastErrorLog != null && currentTick - lastErrorLog < 20) {
            return; // Avoid spamming the console too frequently
        }

        lastErrorLogByPlaceholderContent.put(unparsedContent, currentTick);

        Log.warning("The placeholder {" + unparsedContent + "}"
                        + " registered by the plugin " + exception.getPlaceholderExpansion().getPluginName()
                        + " generated an exception.",
                exception.getCause());
    }

}
