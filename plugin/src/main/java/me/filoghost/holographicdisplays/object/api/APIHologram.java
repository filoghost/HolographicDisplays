/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.object.api;

import me.filoghost.fcommons.Preconditions;
import me.filoghost.holographicdisplays.api.Hologram;
import me.filoghost.holographicdisplays.api.line.ItemLine;
import me.filoghost.holographicdisplays.api.line.TextLine;
import me.filoghost.holographicdisplays.core.nms.NMSManager;
import me.filoghost.holographicdisplays.object.base.BaseHologram;
import me.filoghost.holographicdisplays.object.base.BaseHologramLine;
import me.filoghost.holographicdisplays.object.base.BaseItemLine;
import me.filoghost.holographicdisplays.object.base.BaseTextLine;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class APIHologram extends BaseHologram implements Hologram {
    
    private final Plugin plugin;
    private final APIHologramManager apiHologramManager;
    private final List<BaseHologramLine> lines;

    protected APIHologram(Location source, Plugin plugin, NMSManager nmsManager, APIHologramManager apiHologramManager) {
        super(source, nmsManager);
        Preconditions.notNull(plugin, "plugin");
        this.plugin = plugin;
        this.apiHologramManager = apiHologramManager;
        this.lines = new ArrayList<>();

    }
    
    public Plugin getOwner() {
        return plugin;
    }

    @Override
    public List<BaseHologramLine> getLinesUnsafe() {
        return lines;
    }

    @Override
    public TextLine appendTextLine(String text) {
        checkState();

        BaseTextLine line = new BaseTextLine(this, text);
        lines.add(line);
        refresh();
        return line;
    }

    @Override
    public ItemLine appendItemLine(ItemStack itemStack) {
        checkState();
        Preconditions.notNull(itemStack, "itemStack");

        BaseItemLine line = new BaseItemLine(this, itemStack);
        lines.add(line);
        refresh();
        return line;
    }

    @Override
    public TextLine insertTextLine(int index, String text) {
        checkState();

        BaseTextLine line = new BaseTextLine(this, text);
        lines.add(index, line);
        refresh();
        return line;
    }

    @Override
    public ItemLine insertItemLine(int index, ItemStack itemStack) {
        checkState();
        Preconditions.notNull(itemStack, "itemStack");

        BaseItemLine line = new BaseItemLine(this, itemStack);
        lines.add(index, line);
        refresh();
        return line;
    }

    @Override
    public void delete() {
        apiHologramManager.deleteHologram(this);
    }

}
