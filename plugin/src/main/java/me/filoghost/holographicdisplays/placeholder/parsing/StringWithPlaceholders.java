/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.placeholder.parsing;

import me.filoghost.fcommons.collection.CollectionUtils;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class StringWithPlaceholders {

    private static final char PLACEHOLDER_END_CHAR = '}';
    private static final char PLACEHOLDER_START_CHAR = '{';

    private final String string;
    private final List<PlaceholderMatch> placeholderMatches;

    public StringWithPlaceholders(String string) {
        this.string = string;
        this.placeholderMatches = findPlaceholders(string);
    }
    
    protected List<PlaceholderOccurrence> getPlaceholders() {
        return CollectionUtils.transform(placeholderMatches, match -> match.content);
    }

    public boolean containsPlaceholders() {
        return placeholderMatches != null && !placeholderMatches.isEmpty();
    }

    public String replacePlaceholders(Function<PlaceholderOccurrence, String> replaceFunction) {
        if (!containsPlaceholders()) {
            return string;
        }
        
        StringBuilder output = new StringBuilder();
        int lastAppendIndex = 0;
        
        for (PlaceholderMatch match : placeholderMatches) {
            // Append leading text (if any)
            if (lastAppendIndex != match.startIndex) {
                output.append(string, lastAppendIndex, match.startIndex);
            }
            
            String replacement = replaceFunction.apply(match.content);
            if (replacement != null) {                
                // Append placeholder replacement
                output.append(replacement);
            } else {
                // If no replacement is provided, do not replace the occurrence
                output.append(match.unparsedString);
            }
            lastAppendIndex = match.endIndex;
        }

        // Append trailing text (if any)
        if (lastAppendIndex < string.length()) {
            output.append(string, lastAppendIndex, string.length());
        }

        return output.toString();
    }
    
    @Nullable
    private List<PlaceholderMatch> findPlaceholders(String input) {
        int currentIndex = 0;
        int placeholderStartIndex = -1;
        List<PlaceholderMatch> matches = null;

        while (currentIndex < input.length()) {
            char currentChar = input.charAt(currentIndex);

            if (placeholderStartIndex >= 0) {
                if (currentChar == PLACEHOLDER_END_CHAR) {
                    int endIndex = currentIndex + 1;
                    
                    // The unparsed string includes the opening and closing tags (e.g.: "{online: lobby}")
                    String unparsedString = input.substring(placeholderStartIndex, endIndex);
                    
                    // The content string does NOT include the opening and closing tags (e.g.: "online: lobby")
                    String contentString = unparsedString.substring(1, unparsedString.length() - 1);
                    PlaceholderOccurrence content = PlaceholderOccurrence.parse(contentString);
                    
                    if (matches == null) {
                        matches = new ArrayList<>();
                    }
                    matches.add(new PlaceholderMatch(
                            content,
                            unparsedString,
                            placeholderStartIndex,
                            endIndex));
                    
                    placeholderStartIndex = -1;

                } else if (currentChar == PLACEHOLDER_START_CHAR) {
                    // Nested placeholder, ignore outer placeholder and update start index
                    placeholderStartIndex = currentIndex;
                }
            } else {
                if (currentChar == PLACEHOLDER_START_CHAR) {
                    placeholderStartIndex = currentIndex;
                }
            }

            currentIndex++;
        }
        
        return matches;
    }
    

    private static class PlaceholderMatch {

        private final PlaceholderOccurrence content;
        private final String unparsedString;
        private final int startIndex;
        private final int endIndex;

        public PlaceholderMatch(PlaceholderOccurrence parsedContent, String unparsedString, int startIndex, int endIndex) {
            this.content = parsedContent;
            this.unparsedString = unparsedString;
            this.startIndex = startIndex;
            this.endIndex = endIndex;
        }

    }

}
