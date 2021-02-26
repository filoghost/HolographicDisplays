/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.object.internal;

import me.filoghost.holographicdisplays.api.line.ItemLine;
import me.filoghost.holographicdisplays.api.line.TextLine;
import me.filoghost.holographicdisplays.nms.interfaces.NMSManager;
import me.filoghost.holographicdisplays.object.base.BaseHologram;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class InternalHologram extends BaseHologram {

    private final String name;
    private final List<InternalHologramLine> lines;

    protected InternalHologram(Location source, String name, NMSManager nmsManager) {
        super(source, nmsManager);
        super.setAllowPlaceholders(true);
        this.name = name;
        this.lines = new ArrayList<>();
    }

    public String getName() {
        return name;
    }
    
    @Override
    public List<InternalHologramLine> getLinesUnsafe() {
        return lines;
    }
    
    @Override
    public String toString() {
        return "InternalHologram [name=" + name + ", super=" + super.toString() + "]";
    }

    @Override
    public TextLine appendTextLine(String text) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ItemLine appendItemLine(ItemStack itemStack) {
        throw new UnsupportedOperationException();
    }

    @Override
    public TextLine insertTextLine(int index, String text) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ItemLine insertItemLine(int index, ItemStack itemStack) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete() {
        throw new UnsupportedOperationException();
    }

}
