/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.log;

import me.filoghost.fcommons.ExceptionUtils;
import me.filoghost.fcommons.Strings;
import me.filoghost.fcommons.config.exception.ConfigException;
import me.filoghost.fcommons.config.exception.ConfigSyntaxException;
import me.filoghost.fcommons.logging.ErrorCollector;
import me.filoghost.fcommons.logging.ErrorLog;
import me.filoghost.holographicdisplays.plugin.config.InternalHologramLoadException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class PrintableErrorCollector extends ErrorCollector {

    private static final String ERROR_PREFIX = ChatColor.RED + "[HolographicDisplays] ";

    public void logSummaryToConsole() {
        Bukkit.getConsoleSender().sendMessage(ERROR_PREFIX
                + "Encountered " + getErrorsCount() + " error(s) on load. "
                + "Check previous console logs for more information.");
    }

    @Override
    public void logToConsole() {
        List<String> outputLines = new ArrayList<>();

        if (errors.size() > 0) {
            outputLines.add(ERROR_PREFIX + "Encountered " + errors.size() + " error(s) on load:");
            outputLines.add(" ");

            for (int i = 0; i < errors.size(); i++) {
                ErrorDisplayInfo errorDisplayInfo = getDisplayInfo(errors.get(i));
                displayError(outputLines, i + 1, errorDisplayInfo);
            }
        }

        Bukkit.getConsoleSender().sendMessage(String.join(ChatColor.RESET + "\n", outputLines));
    }

    private ErrorDisplayInfo getDisplayInfo(ErrorLog error) {
        List<String> messageParts = new ArrayList<>(error.getMessage());
        String details = null;
        Throwable exception = error.getCause();

        // Inspect the exception cause chain until an unknown exception or the last one in the chain
        while (true) {
            if (exception instanceof ConfigSyntaxException) {
                // Stop inspecting the cause chain, only show details instead of stack traces for syntax exceptions
                messageParts.add(exception.getMessage());
                details = ((ConfigSyntaxException) exception).getSyntaxErrorDetails();
                break;

            } else if (exception instanceof ConfigException || exception instanceof InternalHologramLoadException) {
                // Known exceptions, add the message and inspect the cause
                messageParts.add(exception.getMessage());
                exception = exception.getCause();

            } else {
                // Unknown exception or last one in the chain
                break;
            }
        }

        return new ErrorDisplayInfo(messageParts, details, exception);
    }

    private static void displayError(List<String> outputLines, int index, ErrorDisplayInfo errorDisplayInfo) {
        StringBuilder message = new MessagePartJoiner(errorDisplayInfo.getMessageParts()).getOutput();
        if (!Strings.hasSentenceEnding(message.toString())) {
            message.append(".");
        }
        if (errorDisplayInfo.getDetails() != null) {
            message.append(" Details:");
        }

        outputLines.add("" + ChatColor.YELLOW + index + ") " + ChatColor.WHITE + message);

        if (errorDisplayInfo.getDetails() != null) {
            outputLines.add(ChatColor.YELLOW + errorDisplayInfo.getDetails());
        }

        if (errorDisplayInfo.getException() != null) {
            outputLines.add(ChatColor.DARK_GRAY + "------------[ Exception details ]------------");
            for (String stackTraceLine : ExceptionUtils.getStackTraceOutputLines(errorDisplayInfo.getException())) {
                outputLines.add(ChatColor.DARK_GRAY + stackTraceLine);
            }
            outputLines.add(ChatColor.DARK_GRAY + "---------------------------------------------");
        }
        outputLines.add(" ");
    }

}
