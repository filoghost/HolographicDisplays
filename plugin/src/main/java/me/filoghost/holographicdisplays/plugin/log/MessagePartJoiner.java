/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.log;

import me.filoghost.fcommons.Strings;

import java.util.List;

class MessagePartJoiner {

    private final StringBuilder output;

    private String previousMessagePart;
    private boolean appendedFirstSentenceSeparator;

    public static String join(List<String> messageParts) {
        int estimateLength = getEstimateLength(messageParts);
        MessagePartJoiner errorMessageBuilder = new MessagePartJoiner(estimateLength);
        for (String messagePart : messageParts) {
            errorMessageBuilder.append(messagePart);
        }
        return errorMessageBuilder.build();
    }

    private static int getEstimateLength(List<String> messageParts) {
        int estimateLength = 0;

        // Length of message parts
        for (String messagePart : messageParts) {
            estimateLength += messagePart.length();
        }

        // Length of separators in between
        estimateLength += (messageParts.size() - 1) * 2;

        return estimateLength;
    }

    private MessagePartJoiner(int estimateLength) {
        output = new StringBuilder(estimateLength);
    }

    private void append(String messagePart) {
        appendSeparator();
        appendMessagePart(messagePart);

        previousMessagePart = messagePart;
    }

    private void appendMessagePart(String messagePart) {
        if (previousMessagePart == null || hasSentenceEnding(previousMessagePart)) {
            output.append(Strings.capitalizeFirst(messagePart));
        } else {
            output.append(messagePart);
        }
    }

    private void appendSeparator() {
        if (previousMessagePart == null) {
            return;
        }
        
        if (hasSentenceEnding(previousMessagePart)) {
            output.append(" ");
            this.appendedFirstSentenceSeparator = false;

        } else if (!appendedFirstSentenceSeparator) {
            output.append(": ");
            this.appendedFirstSentenceSeparator = true;

        } else {
            output.append(", ");
        }
    }

    private boolean hasSentenceEnding(String s) {
        if (Strings.isEmpty(s)) {
            return false;
        }

        char lastChar = s.charAt(s.length() - 1);
        return lastChar == '.' || lastChar == '?' || lastChar == '!';
    }

    private String build() {
        return output.toString();
    }

}
