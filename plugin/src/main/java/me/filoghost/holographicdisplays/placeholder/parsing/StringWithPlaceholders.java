/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.placeholder.parsing;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class StringWithPlaceholders {

    private static final char PLACEHOLDER_END_CHAR = '}';
    private static final char PLACEHOLDER_START_CHAR = '{';

    private final String string;
    private final List<StringPart> stringParts;

    public StringWithPlaceholders(String string) {
        this.string = string;
        this.stringParts = toStringParts(string);
    }

    public boolean containsPlaceholders() {
        return stringParts != null;
    }

    public String replacePlaceholders(Function<PlaceholderOccurrence, String> replaceFunction) {
        if (!containsPlaceholders()) {
            return string;
        }
        
        StringBuilder output = new StringBuilder();
        for (StringPart part : stringParts) {
            output.append(part.getValue(replaceFunction));
        }
        
        return output.toString();
    }
    
    private @Nullable List<StringPart> toStringParts(String string) {
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
    
    
    private interface StringPart {
        
        String getValue(Function<PlaceholderOccurrence, String> placeholderReplaceFunction);
        
    }
    
    
    private static class LiteralStringPart implements StringPart {
        
        private final String literalString;

        LiteralStringPart(String literalString) {
            this.literalString = literalString;
        }

        @Override
        public String getValue(Function<PlaceholderOccurrence, String> placeholderReplaceFunction) {
            return literalString;
        }

    }
    

    private static class PlaceholderStringPart implements StringPart {

        private final PlaceholderOccurrence content;
        private final String unparsedString;

        PlaceholderStringPart(PlaceholderOccurrence parsedContent, String unparsedString) {
            this.content = parsedContent;
            this.unparsedString = unparsedString;
        }

        @Override
        public String getValue(Function<PlaceholderOccurrence, String> placeholderReplaceFunction) {
            String replacement = placeholderReplaceFunction.apply(content);
            if (replacement != null) {
                return replacement;
            } else {
                // If no replacement is provided, leave the unparsed placeholder string
                return unparsedString;
            }
        }

    }

}
