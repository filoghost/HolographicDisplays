/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.placeholder.parsing;

import me.filoghost.fcommons.collection.CaseInsensitiveString;
import org.bukkit.plugin.Plugin;

public class PluginName {

    private final CaseInsensitiveString pluginName;

    public PluginName(Plugin plugin) {
        this(plugin.getName());
    }

    public PluginName(String pluginName) {
        this.pluginName = new CaseInsensitiveString(pluginName);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        PluginName other = (PluginName) obj;
        return this.pluginName.equals(other.pluginName);
    }

    @Override
    public int hashCode() {
        return pluginName.hashCode();
    }
    
    @Override
    public String toString() {
        return pluginName.toString();
    }

}
