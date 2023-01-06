/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.core.placeholder.parsing;

import me.filoghost.holographicdisplays.core.placeholder.PlaceholderOccurrence;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

class Parser {

    private static final char PLACEHOLDER_START_CHAR = '{';
    private static final char PLACEHOLDER_END_CHAR = '}';
    private static final char ESCAPE_CHAR = '\\';

    static @NotNull StringWithPlaceholders parse(@NotNull String string, boolean keepEscapes) {
        List<Part> parts = null;
        int placeholderStartIndex = -1;
        int lastAppendIndex = 0;

        for (int currentIndex = 0; currentIndex < string.length(); currentIndex++) {
            char currentChar = string.charAt(currentIndex);

            if (currentChar == ESCAPE_CHAR && currentIndex < string.length() - 1) {
                char nextChar = string.charAt(currentIndex + 1);
                if (isSpecialCharacter(nextChar)) {
                    // Skip the escape character and the next one if is a special character
                    currentIndex++;
                    continue;
                }
            }

            if (placeholderStartIndex >= 0) {
                if (currentChar == PLACEHOLDER_END_CHAR) {
                    // Inside placeholder
                    int endIndex = currentIndex + 1;

                    // The unparsed string includes the opening and closing tags (e.g.: "{online: lobby}")
                    String unparsedString = substring(string, placeholderStartIndex, endIndex, keepEscapes);

                    // The content string does NOT include the opening and closing tags (e.g.: "online: lobby")
                    String contentString = unparsedString.substring(1, unparsedString.length() - 1);
                    PlaceholderOccurrence content = PlaceholderOccurrence.parse(contentString);

                    if (parts == null) {
                        parts = new ArrayList<>();
                    }

                    // Append leading literal part (if any)
                    if (placeholderStartIndex != lastAppendIndex) {
                        parts.add(new StringPart(substring(string, lastAppendIndex, placeholderStartIndex, keepEscapes)));
                    }

                    // Append placeholder part
                    parts.add(new PlaceholderPart(content, unparsedString));
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
        if (lastAppendIndex != string.length() && parts != null) {
            parts.add(new StringPart(substring(string, lastAppendIndex, string.length(), keepEscapes)));
        }

        return new StringWithPlaceholders(keepEscapes ? string : removeEscapes(string), parts);
    }

    private static String substring(String string, int startIndex, int endIndex, boolean keepEscapes) {
        String substring = string.substring(startIndex, endIndex);
        if (keepEscapes) {
            return substring;
        } else {
            return removeEscapes(substring);
        }
    }

    private static String removeEscapes(String string) {
        StringBuilder output = null;

        for (int currentIndex = 0; currentIndex < string.length(); currentIndex++) {
            char currentChar = string.charAt(currentIndex);

            if (currentChar == ESCAPE_CHAR && currentIndex < string.length() - 1) {
                char nextChar = string.charAt(currentIndex + 1);
                if (isSpecialCharacter(nextChar)) {
                    // Lazy initialization, append the initial part of the string (optimization)
                    if (output == null) {
                        output = new StringBuilder(string.length());
                        output.append(string, 0, currentIndex);
                    }

                    // Append the next character without the escape and skip it
                    output.append(nextChar);
                    currentIndex++;
                    continue;
                }
            }

            if (output != null) {
                output.append(currentChar);
            }
        }

        if (output != null) {
            return output.toString();
        } else {
            return string;
        }
    }

    public static String addEscapes(String string) {
        StringBuilder output = new StringBuilder(string.length() + 16); // String gets longer with escapes

        for (int i = 0; i < string.length(); i++) {
            char currentChar = string.charAt(i);

            if (isSpecialCharacter(currentChar)) {
                output.append(ESCAPE_CHAR);
            }

            output.append(currentChar);
        }

        return output.toString();
    }

    private static boolean isSpecialCharacter(char currentChar) {
        return currentChar == ESCAPE_CHAR
                || currentChar == PLACEHOLDER_START_CHAR
                || currentChar == PLACEHOLDER_END_CHAR;
    }

}
