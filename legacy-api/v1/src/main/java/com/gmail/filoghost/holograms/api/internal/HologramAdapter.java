/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package com.gmail.filoghost.holograms.api.internal;

import com.gmail.filoghost.holograms.api.Hologram;
import com.gmail.filoghost.holograms.api.TouchHandler;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.List;

@SuppressWarnings("deprecation")
class HologramAdapter extends BaseHologramAdapter implements Hologram {
    
    private TouchHandler touchHandler;

    public HologramAdapter(Plugin plugin, Location source, String[] lines) {
        super(plugin, source);
        for (String line : lines) {
            hologram.appendTextLine(line);
        }

        ActiveObjectsRegistry.addHologram(this);
    }

    public HologramAdapter(Plugin plugin, Location source, String[] lines, List<Player> whoCanSee) {
        super(plugin, source);
        restrictVisibityTo(whoCanSee);
        for (String line : lines) {
            hologram.appendTextLine(line);
        }
        
        ActiveObjectsRegistry.addHologram(this);
    }

    @Override
    public void delete() {
        hologram.delete();

        ActiveObjectsRegistry.removeHologram(this);
    }

    @Override
    public boolean update() {
        return true;
    }

    @Override
    public void hide() {
        
    }

    @Override
    public void addLine(String text) {
        hologram.appendTextLine(text);
        updateTouchHandler();
    }

    @Override
    public void setLine(int index, String text) {
        hologram.removeLine(index);
        hologram.insertTextLine(index, text);
        updateTouchHandler();
    }

    @Override
    public void insertLine(int index, String text) {
        hologram.insertTextLine(index, text);
        updateTouchHandler();
    }

    @Override
    public String[] getLines() {
        String[] lines = new String[hologram.size()];
        for (int i = 0; i < lines.length; i++) {
            lines[i] = ((me.filoghost.holographicdisplays.api.line.TextLine) hologram.getLine(i)).getText();
        }
        return lines;
    }

    @Override
    public int getLinesLength() {
        return hologram.size();
    }

    @Override
    public void setLocation(Location location) {
        hologram.teleport(location);
    }

    @Override
    public void setTouchHandler(TouchHandler handler) {
        this.touchHandler = handler;
        updateTouchHandler();
    }

    private void updateTouchHandler() {
        for (int i = 0; i < hologram.size(); i++) {
            me.filoghost.holographicdisplays.api.line.HologramLine line = hologram.getLine(i);
            if (!(line instanceof me.filoghost.holographicdisplays.api.line.TouchableLine)) {
                continue;
            }

            me.filoghost.holographicdisplays.api.line.TouchableLine touchableLine = (me.filoghost.holographicdisplays.api.line.TouchableLine) line;
            
            if (touchHandler != null) {
                touchableLine.setTouchHandler(new HologramTouchHandlerAdapter(this, touchHandler));
            } else {
                touchableLine.setTouchHandler(null);
            }
        }
    }

    @Override
    public TouchHandler getTouchHandler() {
        return touchHandler;
    }

    @Override
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
    public boolean isDeleted() {
        return hologram.isDeleted();
    }

}
