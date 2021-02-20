/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package com.gmail.filoghost.holograms.api.adapter;

import com.gmail.filoghost.holograms.api.Hologram;
import com.gmail.filoghost.holograms.api.TouchHandler;
import me.filoghost.holographicdisplays.api.line.HologramLine;
import me.filoghost.holographicdisplays.api.line.TextLine;
import me.filoghost.holographicdisplays.api.line.TouchableLine;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("deprecation")
public class HologramAdapter implements Hologram {
    
    public static final Map<Plugin, Collection<HologramAdapter>> activeHolograms = new HashMap<>();
    
    private final Plugin plugin;
    private final me.filoghost.holographicdisplays.api.Hologram hologram;
    private TouchHandler touchHandler;
    
    public HologramAdapter(Plugin plugin, me.filoghost.holographicdisplays.api.Hologram delegate) {
        this.plugin = plugin;
        this.hologram = delegate;
        
        activeHolograms.computeIfAbsent(plugin, __ -> new ArrayList<>()).add(this);
    }
    
    @Override
    @Deprecated
    public boolean update() {
        return true;
    }

    @Override
    @Deprecated
    public void hide() {
        
    }

    @Override
    @Deprecated
    public void addLine(String text) {
        hologram.appendTextLine(text);
        updateTouchHandler();
    }

    @Override
    @Deprecated
    public void setLine(int index, String text) {
        hologram.removeLine(index);
        hologram.insertTextLine(index, text);
        updateTouchHandler();
    }

    @Override
    @Deprecated
    public void insertLine(int index, String text) {
        hologram.insertTextLine(index, text);
        updateTouchHandler();
    }

    @Override
    @Deprecated
    public String[] getLines() {
        String[] lines = new String[hologram.size()];
        for (int i = 0; i < lines.length; i++) {
            HologramLine lineObject = hologram.getLine(i);
            if (lineObject instanceof TextLine) {
                lines[i] = ((TextLine) lineObject).getText();
            } else {
                lines[i] = "";
            }
        }
        return lines;
    }

    @Override
    @Deprecated
    public int getLinesLength() {
        return hologram.size();
    }

    @Override
    @Deprecated
    public void setLocation(Location location) {
        hologram.teleport(location);
    }

    @Override
    @Deprecated
    public void setTouchHandler(TouchHandler handler) {
        this.touchHandler = handler;
        updateTouchHandler();
    }

    private void updateTouchHandler() {
        for (int i = 0; i < hologram.size(); i++) {
            HologramLine line = hologram.getLine(i);
            if (!(line instanceof TouchableLine)) {
                continue;
            }
            
            TouchableLine touchableLine = (TouchableLine) line;
            
            if (touchHandler != null) {
                touchableLine.setTouchHandler(new HologramTouchHandlerAdapter(this, touchHandler));
            } else {
                touchableLine.setTouchHandler(null);
            }
        }
    }

    @Override
    @Deprecated
    public TouchHandler getTouchHandler() {
        return touchHandler;
    }

    @Override
    @Deprecated
    public boolean hasTouchHandler() {
        return touchHandler != null;
    }

    @Override
    public void removeLine(int index) {
        hologram.removeLine(index);
    }

    @Override
    public void clearLines() {
        hologram.clearLines();
    }

    @Override
    public Location getLocation() {
        return hologram.getLocation();
    }

    @Override
    public double getX() {
        return hologram.getX();
    }

    @Override
    public double getY() {
        return hologram.getY();
    }

    @Override
    public double getZ() {
        return hologram.getZ();
    }

    @Override
    public World getWorld() {
        return hologram.getWorld();
    }

    @Override
    public void teleport(Location location) {
        hologram.teleport(location);
    }

    @Override
    public long getCreationTimestamp() {
        return hologram.getCreationTimestamp();
    }

    @Override
    public void delete() {
        hologram.delete();
        
        activeHolograms.get(plugin).remove(this);
    }

    @Override
    public boolean isDeleted() {
        return hologram.isDeleted();
    }

}
