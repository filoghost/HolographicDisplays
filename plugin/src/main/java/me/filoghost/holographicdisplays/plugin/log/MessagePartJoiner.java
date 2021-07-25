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

    MessagePartJoiner(List<String> messageParts) {
        this.output = new StringBuilder();
        for (String messagePart : messageParts) {
            append(messagePart);
        }
    }

    private void append(String messagePart) {
        appendSeparator();
        appendMessagePart(messagePart);

        previousMessagePart = messagePart;
    }

    private void appendMessagePart(String messagePart) {
        if (previousMessagePart == null || Strings.hasSentenceEnding(previousMessagePart)) {
            output.append(Strings.capitalizeFirst(messagePart));
        } else {
            output.append(messagePart);
        }
    }

    private void appendSeparator() {
        if (previousMessagePart == null) {
            return;
        }

        if (Strings.hasSentenceEnding(previousMessagePart)) {
            output.append(" ");
            this.appendedFirstSentenceSeparator = false;

        } else if (!appendedFirstSentenceSeparator) {
            output.append(": ");
            this.appendedFirstSentenceSeparator = true;

        } else {
            output.append(", ");
        }
    }

    StringBuilder getOutput() {
        return output;
    }

}
