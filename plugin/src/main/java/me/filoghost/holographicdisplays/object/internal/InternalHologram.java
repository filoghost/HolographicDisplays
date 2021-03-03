/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.object.internal;

import me.filoghost.holographicdisplays.HolographicDisplays;
import me.filoghost.holographicdisplays.core.nms.NMSManager;
import me.filoghost.holographicdisplays.object.base.BaseHologram;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class InternalHologram extends BaseHologram {

    private final String name;
    private final List<InternalHologramLine> lines;

    protected InternalHologram(Location source, String name, NMSManager nmsManager) {
        super(source, nmsManager);
        this.name = name;
        this.lines = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public InternalTextLine createTextLine(String text, String serializedConfigValue) {
        return new InternalTextLine(this, getNMSManager(), text, serializedConfigValue);
    }

    public InternalItemLine createItemLine(ItemStack icon, String serializedConfigValue) {
        return new InternalItemLine(this, getNMSManager(), icon, serializedConfigValue);
    }

    @Override
    public Plugin getOwnerPlugin() {
        return HolographicDisplays.getInstance();
    }

    @Override
    public List<InternalHologramLine> getLinesUnsafe() {
        return lines;
    }

    @Override
    public boolean isVisibleTo(Player player) {
        return true;
    }

    @Override
    public String toFormattedString() {
        return name;
    }

    @Override
    public String toString() {
        return "InternalHologram [name=" + name + ", super=" + super.toString() + "]";
    }

}
