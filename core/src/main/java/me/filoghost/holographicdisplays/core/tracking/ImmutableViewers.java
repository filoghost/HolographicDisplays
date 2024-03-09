/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.core.tracking;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;

public class ImmutableViewers<T extends Viewer> implements Viewers<T> {

    private final Collection<T> viewers;

    public ImmutableViewers(Collection<T> viewers) {
        this.viewers = new ArrayList<>(viewers);
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        viewers.forEach(action);
    }

}
