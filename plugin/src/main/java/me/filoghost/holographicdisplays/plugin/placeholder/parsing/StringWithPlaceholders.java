/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.placeholder.parsing;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

public final class StringWithPlaceholders {

    private static final StringWithPlaceholders NULL_INSTANCE = new StringWithPlaceholders(null, null);

    private static final char PLACEHOLDER_END_CHAR = '}';
    private static final char PLACEHOLDER_START_CHAR = '{';

    private final @Nullable String string;
    private final @Nullable List<StringPart> stringParts;

    public static @NotNull StringWithPlaceholders of(@Nullable String string) {
        if (string == null) {
            return NULL_INSTANCE;
        }
        return new StringWithPlaceholders(string, splitToParts(string));
    }

    private StringWithPlaceholders(@Nullable String string, @Nullable List<StringPart> stringParts) {
        this.string = string;
        this.stringParts = stringParts;
    }

    public @Nullable String getUnreplacedString() {
        return string;
    }

    public boolean containsPlaceholders() {
        return stringParts != null;
    }

    public boolean anyMatch(Predicate<PlaceholderOccurrence> filter) {
        if (!containsPlaceholders()) {
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

    public @NotNull StringWithPlaceholders partiallyReplacePlaceholders(PlaceholderReplaceFunction replaceFunction) {
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
                    fullOutput.append(placeholderStringPart.unreplacedString);
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

    public @Nullable String replacePlaceholders(PlaceholderReplaceFunction replaceFunction) {
        if (!containsPlaceholders()) {
            return string;
        }

        StringBuilder output = new StringBuilder();
        for (StringPart part : stringParts) {
            output.append(part.getValue(replaceFunction));
        }

        return output.toString();
    }

    public String replaceLiteralParts(Function<String, String> transformFunction) {
        if (!containsPlaceholders() || string == null) {
            return transformFunction.apply(string);
        }

        StringBuilder stringOutput = new StringBuilder(string.length());

        for (StringPart part : stringParts) {
            if (part instanceof LiteralStringPart) {
                LiteralStringPart literalStringPart = (LiteralStringPart) part;
                String transformedPart = transformFunction.apply(literalStringPart.literalString);
                stringOutput.append(transformedPart);
            } else {
                stringOutput.append(((PlaceholderStringPart) part).unreplacedString);
            }
        }

        return stringOutput.toString();
    }

    private static @Nullable List<StringPart> splitToParts(@NotNull String string) {
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
        return Objects.equals(this.string, other.string);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(string);
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
        private final String unreplacedString;

        PlaceholderStringPart(PlaceholderOccurrence placeholderOccurrence, String unreplacedString) {
            this.placeholderOccurrence = placeholderOccurrence;
            this.unreplacedString = unreplacedString;
        }

        @Override
        public String getValue(PlaceholderReplaceFunction placeholderReplaceFunction) {
            String replacement = placeholderReplaceFunction.getReplacement(placeholderOccurrence);
            if (replacement != null) {
                return replacement;
            } else {
                // If no replacement is provided, leave the unreplaced placeholder string
                return unreplacedString;
            }
        }

    }

}
