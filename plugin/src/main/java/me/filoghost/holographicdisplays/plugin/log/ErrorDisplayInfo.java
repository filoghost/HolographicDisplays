/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.log;

import java.util.List;

class ErrorDisplayInfo {

    private final List<String> messageParts;
    private final String details;
    private final Throwable exception;

    ErrorDisplayInfo(List<String> messageParts, String details, Throwable exception) {
        this.messageParts = messageParts;
        this.details = details;
        this.exception = exception;
    }

    List<String> getMessageParts() {
        return messageParts;
    }

    String getDetails() {
        return details;
    }

    Throwable getException() {
        return exception;
    }

}
