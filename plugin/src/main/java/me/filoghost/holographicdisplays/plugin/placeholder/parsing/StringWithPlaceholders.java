/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.placeholder.parsing;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public final class StringWithPlaceholders {

    private static final char PLACEHOLDER_END_CHAR = '}';
    private static final char PLACEHOLDER_START_CHAR = '{';

    private final @NotNull String string;
    private final List<StringPart> stringParts;

    @Contract("null -> null; !null -> !null")
    public static StringWithPlaceholders of(@Nullable String string) {
        return string != null ? new StringWithPlaceholders(string) : null;
    }

    public StringWithPlaceholders(@NotNull String string) {
        this.string = string;
        this.stringParts = splitToParts(string);
    }

    private StringWithPlaceholders(@NotNull String string, @Nullable List<StringPart> stringParts) {
        this.string = string;
        this.stringParts = stringParts;
    }

    public String getUnreplacedString() {
        return string;
    }

    public boolean containsPlaceholders() {
        return stringParts != null;
    }

    public boolean anyMatch(Predicate<PlaceholderOccurrence> filter) {
        if (stringParts == null) {
            return false;
        }

        for (StringPart stringPart : stringParts) {
            if (stringPart instanceof PlaceholderStringPart) {
                PlaceholderStringPart placeholderStringPart = (PlaceholderStringPart) stringPart;
                if (filter.test(placeholderStringPart.placeholderOccurrence)) {
                    return true;
                }
            }
        }

        return false;
    }

    public StringWithPlaceholders partiallyReplacePlaceholders(PlaceholderReplaceFunction replaceFunction) {
        if (!containsPlaceholders()) {
            return this;
        }

        StringBuilder output = new StringBuilder();
        StringBuilder fullOutput = new StringBuilder();
        List<StringPart> newStringParts = null; // Lazy initialization

        for (StringPart part : stringParts) {
            if (part instanceof PlaceholderStringPart) {
                PlaceholderStringPart placeholderStringPart = (PlaceholderStringPart) part;
                String replacement = replaceFunction.getReplacement(placeholderStringPart.placeholderOccurrence);
                if (replacement != null) {
                    output.append(replacement);
                    fullOutput.append(replacement);
                } else {
                    // Placeholder was not replaced, may be replaced later
                    if (newStringParts == null) {
                        newStringParts = new ArrayList<>();
                    }
                    // Append leading literal string, if present
                    if (output.length() > 0) {
                        newStringParts.add(new LiteralStringPart(output.toString()));
                        output.setLength(0);
                    }
                    newStringParts.add(placeholderStringPart);
                    output.append(placeholderStringPart.nonReplacedString);
                    fullOutput.append(placeholderStringPart.nonReplacedString);
                }
            } else {
                LiteralStringPart literalStringPart = (LiteralStringPart) part;
                output.append(literalStringPart.literalString);
                fullOutput.append(literalStringPart.literalString);
            }
        }

        if (output.length() > 0 && newStringParts != null) {
            newStringParts.add(new LiteralStringPart(output.toString()));
        }

        return new StringWithPlaceholders(fullOutput.toString(), newStringParts);
    }

    public String replacePlaceholders(PlaceholderReplaceFunction replaceFunction) {
        if (!containsPlaceholders()) {
            return string;
        }

        StringBuilder output = new StringBuilder();
        for (StringPart part : stringParts) {
            output.append(part.getValue(replaceFunction));
        }

        return output.toString();
    }

    private @Nullable List<StringPart> splitToParts(String string) {
        int placeholderStartIndex = -1;
        int lastAppendIndex = 0;
        List<StringPart> stringParts = null; // Lazy initialization

        for (int currentIndex = 0; currentIndex < string.length(); currentIndex++) {
            char currentChar = string.charAt(currentIndex);

            if (placeholderStartIndex >= 0) {
                // Inside placeholder
                if (currentChar == PLACEHOLDER_END_CHAR) {
                    int endIndex = currentIndex + 1;

                    // The unparsed string includes the opening and closing tags (e.g.: "{online: lobby}")
                    String unparsedString = string.substring(placeholderStartIndex, endIndex);

                    // The content string does NOT include the opening and closing tags (e.g.: "online: lobby")
                    String contentString = unparsedString.substring(1, unparsedString.length() - 1);
                    PlaceholderOccurrence content = PlaceholderOccurrence.parse(contentString);

                    if (stringParts == null) {
                        stringParts = new ArrayList<>();
                    }

                    // Append leading literal part (if any)
                    if (placeholderStartIndex != lastAppendIndex) {
                        stringParts.add(new LiteralStringPart(string.substring(lastAppendIndex, placeholderStartIndex)));
                    }

                    // Append placeholder part
                    stringParts.add(new PlaceholderStringPart(content, unparsedString));
                    lastAppendIndex = endIndex;
                    placeholderStartIndex = -1;

                } else if (currentChar == PLACEHOLDER_START_CHAR) {
                    // Nested placeholder, ignore outer placeholder and update start index
                    placeholderStartIndex = currentIndex;
                }
            } else {
                // Outside placeholders, just look for the start of a placeholder
                if (currentChar == PLACEHOLDER_START_CHAR) {
                    placeholderStartIndex = currentIndex;
                }
            }
        }

        // Append trailing literal part (if any)
        if (lastAppendIndex != string.length() && stringParts != null) {
            stringParts.add(new LiteralStringPart(string.substring(lastAppendIndex)));
        }

        return stringParts;
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
        return this.string.equals(other.string);
    }

    @Override
    public int hashCode() {
        return string.hashCode();
    }


    private interface StringPart {

        String getValue(PlaceholderReplaceFunction placeholderReplaceFunction);

    }


    private static class LiteralStringPart implements StringPart {

        private final String literalString;

        LiteralStringPart(String literalString) {
            this.literalString = literalString;
        }

        @Override
        public String getValue(PlaceholderReplaceFunction placeholderReplaceFunction) {
            return literalString;
        }

    }


    private static class PlaceholderStringPart implements StringPart {

        private final PlaceholderOccurrence placeholderOccurrence;
        private final String nonReplacedString;

        PlaceholderStringPart(PlaceholderOccurrence placeholderOccurrence, String nonReplacedString) {
            this.placeholderOccurrence = placeholderOccurrence;
            this.nonReplacedString = nonReplacedString;
        }

        @Override
        public String getValue(PlaceholderReplaceFunction placeholderReplaceFunction) {
            String replacement = placeholderReplaceFunction.getReplacement(placeholderOccurrence);
            if (replacement != null) {
                return replacement;
            } else {
                // If no replacement is provided, leave the unparsed placeholder string
                return nonReplacedString;
            }
        }

    }

}
