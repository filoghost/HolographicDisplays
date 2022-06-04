/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.internal.hologram;

import me.filoghost.holographicdisplays.api.HolographicDisplaysAPI;
import me.filoghost.holographicdisplays.api.Position;
import me.filoghost.holographicdisplays.api.hologram.Hologram;
import me.filoghost.holographicdisplays.api.hologram.PlaceholderSetting;
import me.filoghost.holographicdisplays.plugin.event.InternalHologramChangeEvent;
import me.filoghost.holographicdisplays.plugin.event.InternalHologramChangeEvent.ChangeType;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InternalHologram {

    private final Hologram renderedHologram;
    private final String name;
    private Position position;
    private final List<InternalHologramLine> lines;
    private final List<InternalHologramLine> unmodifiableLinesView;
    private boolean deleted;

    public InternalHologram(HolographicDisplaysAPI api, String name, Position position) {
        this.renderedHologram = api.createHologram(position);
        this.renderedHologram.setPlaceholderSetting(PlaceholderSetting.ENABLE_ALL);
        this.name = name;
        this.position = position;
        this.lines = new ArrayList<>();
        this.unmodifiableLinesView = Collections.unmodifiableList(lines);
    }

    public Hologram getRenderedHologram() {
        return renderedHologram;
    }

    public String getName() {
        return name;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        checkNotDeleted();
        this.position = position;
        updateRendering();
    }

    public List<InternalHologramLine> getLines() {
        return unmodifiableLinesView;
    }

    public void addLine(InternalHologramLine line) {
        checkNotDeleted();
        lines.add(line);
        updateRendering();
    }

    public void addLines(List<InternalHologramLine> lines) {
        checkNotDeleted();
        this.lines.addAll(lines);
        updateRendering();
    }

    public void setLine(int index, InternalHologramLine line) {
        checkNotDeleted();
        lines.set(index, line);
        updateRendering();
    }

    public void setLines(List<InternalHologramLine> lines) {
        checkNotDeleted();
        this.lines.clear();
        this.lines.addAll(lines);
        updateRendering();
    }

    public void insertLine(int beforeIndex, InternalHologramLine line) {
        checkNotDeleted();
        lines.add(beforeIndex, line);
        updateRendering();
    }

    public void removeLine(int index) {
        checkNotDeleted();
        lines.remove(index);
        updateRendering();
    }

    void delete() {
        if (!deleted) {
            deleted = true;
            renderedHologram.delete();
        }
    }

    private void updateRendering() {
        renderedHologram.setPosition(position);
        renderedHologram.getLines().clear();
        for (InternalHologramLine serializedLine : lines) {
            serializedLine.appendTo(renderedHologram);
        }
    }

    private void checkNotDeleted() {
        if (deleted) {
            throw new IllegalStateException("already deleted");
        }
    }

    public void callChangeEvent(ChangeType changeType) {
        Bukkit.getPluginManager().callEvent(new InternalHologramChangeEvent(this, changeType));
    }

}
