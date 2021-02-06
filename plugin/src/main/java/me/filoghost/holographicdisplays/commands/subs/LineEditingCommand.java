/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.commands.subs;

import me.filoghost.holographicdisplays.commands.HologramSubCommand;

public abstract class LineEditingCommand extends HologramSubCommand {

    protected LineEditingCommand(String name, String... aliases) {
        super(name, aliases);
        setShowInHelpCommand(false);
    }

}
