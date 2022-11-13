/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.core;

import me.filoghost.fcommons.Preconditions;

public class CorePreconditions {

    public static void checkMainThread() {
        Preconditions.checkMainThread("async operation is not supported");
    }

}
