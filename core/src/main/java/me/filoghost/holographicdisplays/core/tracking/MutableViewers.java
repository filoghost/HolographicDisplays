/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.core.tracking;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class MutableViewers<T extends Viewer> implements Viewers<T> {

    // Optimization: the viewer is frequently only a single player
    private T viewer;
    private List<T> additionalViewers;

    public synchronized void add(T viewer) {
        if (this.viewer == null) {
            this.viewer = viewer;
        } else {
            if (this.additionalViewers == null) {
                this.additionalViewers = new ArrayList<>();
            }
            this.additionalViewers.add(viewer);
        }
    }

    @Override
    public synchronized void forEach(Consumer<? super T> action) {
        if (viewer != null) {
            action.accept(viewer);
            if (additionalViewers != null) {
                additionalViewers.forEach(action);
            }
        }
    }

}
