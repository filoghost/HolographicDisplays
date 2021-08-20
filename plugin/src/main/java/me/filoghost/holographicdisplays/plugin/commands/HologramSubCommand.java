/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.commands;

import me.filoghost.fcommons.command.CommandContext;
import me.filoghost.fcommons.command.sub.SubCommand;
import me.filoghost.holographicdisplays.plugin.Permissions;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public abstract class HologramSubCommand implements SubCommand {

    private final String name;
    private final List<String> aliases;
    private final String permission;

    private String usageArgs;
    private int minArgs;

    private List<String> description;
    private boolean showInHelpCommand;

    protected HologramSubCommand(String name, String... aliases) {
        this.name = name;
        this.aliases = aliases != null ? Arrays.asList(aliases) : Collections.emptyList();
        this.permission = Permissions.COMMAND_BASE + name.toLowerCase(Locale.ROOT);

        this.showInHelpCommand = true;
    }

    public final String getFullUsageText(CommandContext context) {
        return "/" + context.getRootLabel() + " " + name + (usageArgs != null ? " " + usageArgs : "");
    }

    @Override
    public final String getName() {
        return name;
    }

    @Override
    public final String getPermission() {
        return permission;
    }

    @Override
    public final String getPermissionMessage() {
        return null;
    }

    @Override
    public final String getUsageArgs() {
        return usageArgs;
    }

    public final void setUsageArgs(String usageArgs) {
        this.usageArgs = usageArgs;
    }

    @Override
    public final int getMinArgs() {
        return minArgs;
    }

    public final void setMinArgs(int minArgs) {
        this.minArgs = minArgs;
    }

    public final @NotNull List<String> getAliases() {
        return aliases;
    }

    public final void setDescription(String... description) {
        this.description = Arrays.asList(description);
    }

    public List<String> getDescription(CommandContext context) {
        return description;
    }

    public final void setShowInHelpCommand(boolean show) {
        this.showInHelpCommand = show;
    }

    public final boolean isShowInHelpCommand() {
        return showInHelpCommand;
    }

}
