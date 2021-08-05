/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.hologram.base;

import me.filoghost.fcommons.Preconditions;
import org.jetbrains.annotations.MustBeInvokedByOverriders;

public abstract class BaseHologramComponent {

    private boolean deleted;

    public final boolean isDeleted() {
        return deleted;
    }

    @MustBeInvokedByOverriders
    public void setDeleted() {
        deleted = true;
    }

    protected final void checkNotDeleted() {
        Preconditions.checkState(!deleted, "not usable after being deleted");
    }

    @Override
    public final boolean equals(Object obj) {
        // Use the default identity comparison: two different instances are never equal
        return super.equals(obj);
    }

    @Override
    public final int hashCode() {
        // Use the default identity hash code: each instance has a different hash code
        return super.hashCode();
    }

}
