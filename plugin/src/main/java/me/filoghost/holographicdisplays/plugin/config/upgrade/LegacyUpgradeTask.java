/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.config.upgrade;

import me.filoghost.fcommons.config.exception.ConfigException;

import java.io.IOException;

public interface LegacyUpgradeTask {

    void run() throws IOException, ConfigException;

}
