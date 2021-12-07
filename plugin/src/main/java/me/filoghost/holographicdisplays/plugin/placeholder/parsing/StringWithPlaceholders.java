/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.placeholder.parsing;

import me.filoghost.holographicdisplays.plugin.placeholder.PlaceholderOccurrence;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public final class StringWithPlaceholders {

    private final @NotNull String string;
    private final @Nullable List<Part> parts;

    public static @NotNull StringWithPlaceholders of(@NotNull String string) {
        return new StringWithPlaceholders(string, Parser.parse(string));
    }

    StringWithPlaceholders(@NotNull String string, @Nullable List<Part> parts) {
        this.string = string;
        this.parts = parts;
    }

    public @NotNull String getString() {
        return string;
    }

    public boolean containsPlaceholders() {
        if (parts == null) {
            return false;
        }

        for (Part part : parts) {
            if (part instanceof PlaceholderPart) {
                return true;
            }
        }

        return false;
    }

    public boolean anyPlaceholderMatch(Predicate<PlaceholderOccurrence> filter) {
        if (parts == null) {
            return false;
        }

        for (Part part : parts) {
            if (part instanceof PlaceholderPart) {
                PlaceholderPart placeholderPart = (PlaceholderPart) part;
                if (filter.test(placeholderPart.getPlaceholderOccurrence())) {
                    return true;
                }
            }
        }

        return false;
    }

    public @NotNull String replacePlaceholders(Player player, PlaceholderReplaceFunction replaceFunction) {
        return replace(player, replaceFunction, StringReplaceFunction.NO_REPLACEMENTS);
    }

    public @NotNull String replaceStrings(StringReplaceFunction replaceFunction) {
        return replace(null, PlaceholderReplaceFunction.NO_REPLACEMENTS, replaceFunction);
    }

    private @NotNull String replace(
            Player player,
            PlaceholderReplaceFunction placeholderReplaceFunction,
            StringReplaceFunction literalPartReplaceFunction) {
        if (parts == null) {
            return literalPartReplaceFunction.getReplacement(string);
        }

        StringBuilder output = new StringBuilder(string.length());

        for (Part part : parts) {
            String replacement;
            if (part instanceof StringPart) {
                replacement = ((StringPart) part).getValue(literalPartReplaceFunction);
            } else if (part instanceof PlaceholderPart) {
                replacement = ((PlaceholderPart) part).getValue(player, placeholderReplaceFunction);
            } else {
                throw new AssertionError();
            }
            output.append(replacement);
        }

        return output.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        StringWithPlaceholders other = (StringWithPlaceholders) obj;
        return Objects.equals(this.string, other.string);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(string);
    }


}
