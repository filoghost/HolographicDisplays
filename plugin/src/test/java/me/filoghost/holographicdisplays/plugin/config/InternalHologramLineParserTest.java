/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.config;

import org.bukkit.ChatColor;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class InternalHologramLineParserTest {

    @ParameterizedTest(name = "[{index}] {0} -> {1}")
    @MethodSource({"parseTextArguments"})
    void parseText(String input, String expectedOutput) {
        Settings.placeholderAPIExpandShortFormat = true;
        String output = InternalHologramLineParser.parseText(input);
        assertThat(output).isEqualTo(expectedOutput);
    }

    static Stream<Arguments> parseTextArguments() {
        return Stream.of(
                // PlaceholderAPI replacing and escaping
                Arguments.of("test1 %test2% test3", "test1 {papi: test2} test3"),
                Arguments.of("%&0colored%", "{papi: &0colored}"),
                Arguments.of("%test1% {test2} %test3%", "{papi: test1} {test2} {papi: test3}"),
                Arguments.of("%test_{escape\\this}%", "{papi: test_\\{escape\\\\this\\}}"),
                Arguments.of("%t{e%s}t", "{papi: t\\{e}s}t"),
                Arguments.of("{inside %placeholder%}", "{inside {papi: placeholder}}"),
                Arguments.of("\\{keep escapes\\}", "\\{keep escapes\\}"),
                Arguments.of("single % symbol", "single % symbol"),

                // Placeholders and colors
                Arguments.of("&0 {p: &0}", ChatColor.COLOR_CHAR + "0 {p: &0}")
        );
    }

}
