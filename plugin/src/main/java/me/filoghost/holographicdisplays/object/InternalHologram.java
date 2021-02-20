/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.object;

import me.filoghost.holographicdisplays.nms.interfaces.NMSManager;
import org.bukkit.Location;

public class InternalHologram extends BaseHologram {

    private final String name;
    private final InternalHologramManager internalHologramManager;

    protected InternalHologram(Location source, String name, NMSManager nmsManager, InternalHologramManager internalHologramManager) {
        super(source, nmsManager);
        this.name = name;
        this.internalHologramManager = internalHologramManager;
        setAllowPlaceholders(true);
    }

    public String getName() {
        return name;
    }
    
    @Override
    public void delete() {
        internalHologramManager.deleteHologram(this);
    }

    @Override
    public String toString() {
        return "InternalHologram [name=" + name + ", super=" + super.toString() + "]";
    }    
    
}
