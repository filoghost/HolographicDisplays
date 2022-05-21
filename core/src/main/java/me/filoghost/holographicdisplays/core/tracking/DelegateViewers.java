/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.core.tracking;

import java.util.Collection;
import java.util.function.Consumer;

public class DelegateViewers<T extends Viewer> implements Viewers<T> {

    private final Collection<T> viewers;

    public DelegateViewers(Collection<T> viewers) {
        this.viewers = viewers;
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        viewers.forEach(action);
    }

}
