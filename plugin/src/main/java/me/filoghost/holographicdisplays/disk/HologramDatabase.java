/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.disk;

import me.filoghost.fcommons.config.Config;
import me.filoghost.fcommons.config.ConfigSection;
import me.filoghost.holographicdisplays.object.internal.InternalHologram;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class HologramDatabase {

    private final LinkedHashMap<String, HologramConfig> hologramConfigs;
    
    public HologramDatabase() {
        this.hologramConfigs = new LinkedHashMap<>();
    }

    public void reloadFromConfig(Config config) {
        hologramConfigs.clear();

        for (String hologramName : config.getKeys()) {
            ConfigSection hologramSection = config.getConfigSection(hologramName);
            if (hologramSection == null) {
                continue;
            }

            HologramConfig hologramConfig = new HologramConfig(hologramName, hologramSection);
            hologramConfigs.put(hologramConfig.getName(), hologramConfig);
        }
    }
    
    public Config saveToConfig() {
        Config config = new Config();

        for (Entry<String, HologramConfig> entry : hologramConfigs.entrySet()) {
            config.setConfigSection(entry.getKey(), entry.getValue().toConfigSection());
        }

        config.setHeader(
                "",
                "Please do NOT edit this file manually if possible.",
                ""
        );
        
        return config;
    }

    public Collection<HologramConfig> getHolograms() {
        return hologramConfigs.values();
    }
    
    public void addOrUpdate(InternalHologram hologram) {
        HologramConfig hologramConfig = new HologramConfig(hologram);
        
        hologramConfigs.put(hologram.getName(), hologramConfig);
    }

    public void removeHologram(InternalHologram hologram) {
        hologramConfigs.remove(hologram.getName());
    }

}
