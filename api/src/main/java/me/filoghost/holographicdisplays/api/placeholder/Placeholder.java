/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.api.placeholder;

import me.filoghost.holographicdisplays.api.HolographicDisplaysAPI;
import me.filoghost.holographicdisplays.api.hologram.Hologram;
import me.filoghost.holographicdisplays.api.hologram.PlaceholderSetting;

/**
 * A placeholder is a dynamic text value that can be displayed in a text hologram line.
 * <p>
 * Placeholders can be global (show the same text to all players) or individual (show a different text to each player).
 * Global placeholders are preferable when possible, as individual placeholders have a higher performance impact.
 * <p>
 * To add a placeholder a class must implement either {@link GlobalPlaceholder} or {@link IndividualPlaceholder}, then
 * it must be registered with {@link HolographicDisplaysAPI#registerGlobalPlaceholder(String, GlobalPlaceholder)} or
 * {@link HolographicDisplaysAPI#registerIndividualPlaceholder(String, IndividualPlaceholder)} by providing an
 * identifier string.
 * <p>
 * The valid formats to display the placeholder inside a text line are:
 * <ul>
 * <li>{identifier}
 * <li>{identifier: argument}
 * <li>{plugin/identifier}
 * <li>{plugin/identifier: argument}
 * </ul>
 * <p>
 * The parts of the placeholder format are:
 * <ul>
 * <li>Identifier (required): the identifier used to register the placeholder.
 * <li>Argument (optional): the argument passed to the replacement callback.
 * <li>Plugin (optional): the name of the plugin to disambiguate if more plugins register the same identifier.
 * </ul>
 * <p>
 * Placeholders are disabled by default and need to be enabled with
 * {@link Hologram#setPlaceholderSetting(PlaceholderSetting)}.
 *
 * @since 1
 */
public interface Placeholder {

    /**
     * Returns the minimum interval in ticks between invocations of the replacement callback. For individual
     * placeholders the interval is counted separately for each player.
     * <p>
     * Note that more ticks can pass between invocations if no player is near, do not use the replacement callback as a
     * timer.
     *
     * @since 1
     */
    int getRefreshIntervalTicks();

}
