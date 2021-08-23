/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.hologram.base;

import me.filoghost.holographicdisplays.api.hologram.HologramPosition;
import me.filoghost.holographicdisplays.plugin.config.Settings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class BaseHologramLines<T extends EditableHologramLine> implements Iterable<T> {

    private final BaseHologram hologram;
    private final List<T> lines;
    private final List<T> unmodifiableLinesView;

    public BaseHologramLines(BaseHologram hologram) {
        this.hologram = hologram;
        this.lines = new ArrayList<>();
        this.unmodifiableLinesView = Collections.unmodifiableList(lines);
    }

    @Override
    public Iterator<T> iterator() {
        return unmodifiableLinesView.iterator();
    }

    public int size() {
        return lines.size();
    }

    public boolean isEmpty() {
        return lines.isEmpty();
    }

    public T get(int index) {
        return lines.get(index);
    }

    public void add(T line) {
        checkNotDeleted();

        lines.add(line);
        updateLinePositions();
    }

    public void addAll(List<? extends T> newLines) {
        checkNotDeleted();

        lines.addAll(newLines);
        updateLinePositions();
    }

    public void insert(int afterIndex, T line) {
        checkNotDeleted();

        lines.add(afterIndex, line);
        updateLinePositions();
    }

    public void set(int index, T line) {
        checkNotDeleted();

        T previousLine = lines.set(index, line);
        previousLine.setDeleted();
        updateLinePositions();
    }

    public void setAll(List<T> newLines) {
        checkNotDeleted();

        clear();
        lines.addAll(newLines);
        updateLinePositions();
    }

    public void remove(int index) {
        checkNotDeleted();

        lines.remove(index).setDeleted();
        updateLinePositions();
    }

    public boolean remove(T line) {
        checkNotDeleted();

        boolean removed = lines.remove(line);
        if (removed) {
            line.setDeleted();
            updateLinePositions();
        }
        return removed;
    }

    public void clear() {
        checkNotDeleted();

        Iterator<T> iterator = lines.iterator();
        while (iterator.hasNext()) {
            T line = iterator.next();
            iterator.remove();
            line.setDeleted();
        }

        // No need to update positions, since there are no lines
    }

    /**
     * The top part of the first line should be exactly on the Y position of the hologram.
     * The second line is below the first, and so on.
     */
    protected void updateLinePositions() {
        HologramPosition hologramPosition = hologram.getPosition();
        double currentLineY = hologramPosition.getY();

        for (int i = 0; i < lines.size(); i++) {
            T line = lines.get(i);

            currentLineY -= line.getHeight();
            if (i > 0) {
                currentLineY -= Settings.spaceBetweenLines;
            }

            line.setPosition(hologramPosition.getX(), currentLineY, hologramPosition.getZ());
        }
    }

    public double getHeight() {
        if (isEmpty()) {
            return 0;
        }

        double height = 0.0;

        for (EditableHologramLine line : lines) {
            height += line.getHeight();
        }

        height += Settings.spaceBetweenLines * (lines.size() - 1);
        return height;
    }

    public void setDeleted() {
        for (T line : lines) {
            line.setDeleted();
        }
    }

    private void checkNotDeleted() {
        hologram.checkNotDeleted();
    }

    @Override
    public String toString() {
        return lines.toString();
    }

}
