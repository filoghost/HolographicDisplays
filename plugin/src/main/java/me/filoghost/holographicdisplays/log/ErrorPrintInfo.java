/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.log;

import java.util.List;

class ErrorPrintInfo {

    private final int index;
    private final List<String> message;
    private final String details;
    private final Throwable cause;

    protected ErrorPrintInfo(int index, List<String> message, String details, Throwable cause) {
        this.index = index;
        this.message = message;
        this.details = details;
        this.cause = cause;
    }

    public int getIndex() {
        return index;
    }

    public List<String> getMessage() {
        return message;
    }

    public String getDetails() {
        return details;
    }

    public Throwable getCause() {
        return cause;
    }

}
