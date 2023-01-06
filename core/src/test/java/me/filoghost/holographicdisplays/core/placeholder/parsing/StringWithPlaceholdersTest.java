/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.core.placeholder.parsing;

import me.filoghost.holographicdisplays.core.placeholder.PlaceholderOccurrence;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class StringWithPlaceholdersTest {

    @ParameterizedTest(name = "[{index}] {0} -> {1}")
    @MethodSource("replacementsTestArguments")
    void replacements(String input, String expectedOutput) {
        boolean expectedContainsPlaceholders = expectedOutput.contains("#");
        StringWithPlaceholders s = StringWithPlaceholders.of(input);

        assertThat(s.replacePlaceholders(null, (player, occurrence) -> "#")).isEqualTo(expectedOutput);
        assertThat(s.containsPlaceholders()).isEqualTo(expectedContainsPlaceholders);
    }

    static Stream<Arguments> replacementsTestArguments() {
        return Stream.of(
                Arguments.of("", ""),
                Arguments.of("{}", "#"), // Empty placeholder should still be detected
                Arguments.of("{p}{p}", "##"),
                Arguments.of(" {p} ", " # "),
                Arguments.of("{p} {p} {p}", "# # #"),
                Arguments.of("{{p}}", "{#}"), // Only the innermost placeholder should be replaced
                Arguments.of("{{p}", "{#"),
                Arguments.of("{p}}", "#}"),
                Arguments.of("{p abc", "{p abc"), // Placeholder without closing tag
                Arguments.of("abc p}", "abc p}") // Placeholder without opening tag
        );
    }

    @ParameterizedTest(name = "[{index}] {0} -> {1}")
    @MethodSource("replaceLiteralPartsTestArguments")
    void replaceLiteralParts(String input, String expectedOutput) {
        StringWithPlaceholders s = StringWithPlaceholders.of(input);
        assertThat(s.replaceOutsidePlaceholders(literalPart -> "_")).isEqualTo(expectedOutput);
    }

    static Stream<Arguments> replaceLiteralPartsTestArguments() {
        return Stream.of(
                Arguments.of(" {p} ", "_{p}_"),
                Arguments.of("{p} {p} abc {p}", "{p}_{p}_{p}"),
                Arguments.of("{p}{p}", "{p}{p}"),
                Arguments.of(" {{p}} ", "_{p}_")
        );
    }

    @Test
    void skipReplacing() {
        String input = "{p} a {p} b {p}";
        StringWithPlaceholders s = StringWithPlaceholders.of(input);
        assertThat(s.replacePlaceholders(null, (player, occurrence) -> null)).isEqualTo(input);
    }

    @ParameterizedTest(name = "[{index}] {0} -> {1}, {2}, {3}")
    @MethodSource("parsingTestArguments")
    void parsing(String input, String expectedPluginName, String expectedIdentifier, String expectedArgument) {
        StringWithPlaceholders s = StringWithPlaceholders.of(input);

        List<PlaceholderOccurrence> placeholders = new ArrayList<>();
        s.replacePlaceholders(null, (player, occurrence) -> {
            placeholders.add(occurrence); // Just collect occurrences
            return null;
        });
        assertThat(placeholders).hasSize(1);

        PlaceholderOccurrence placeholder = placeholders.get(0);

        if (expectedPluginName != null) {
            assertThat(placeholder.getPluginName()).hasToString(expectedPluginName);
        } else {
            assertThat(placeholder.getPluginName()).isNull();
        }
        assertThat(placeholder.getIdentifier()).hasToString(expectedIdentifier);
        assertThat(placeholder.getArgument()).isEqualTo(expectedArgument);
    }

    static Stream<Arguments> parsingTestArguments() {
        return Stream.of(
                Arguments.of("{}", null, "", null),
                Arguments.of("{identifier}", null, "identifier", null),
                Arguments.of("{plugin/identifier}", "plugin", "identifier", null),
                Arguments.of("{plugin/identifier: argument}", "plugin", "identifier", "argument"),
                Arguments.of("{identifier: argument}", null, "identifier", "argument"),
                Arguments.of("{plugin/identifier/nestedIdentifier}", "plugin", "identifier/nestedIdentifier", null),
                Arguments.of("{identifier: argument: nestedArgument}", null, "identifier", "argument: nestedArgument")
        );
    }

    @ParameterizedTest(name = "[{index}] {0} -> {1}")
    @MethodSource("escapesTestArguments")
    void escapes(String input, String expectedOutput) {
        StringWithPlaceholders s = StringWithPlaceholders.of(input);

        String actualOutput = s.replacePlaceholders(null, (player, occurrence) -> "<" + occurrence.getUnparsedContent() + ">");
        assertThat(actualOutput).isEqualTo(expectedOutput);
    }

    @ParameterizedTest(name = "[{index}] {0} -> {0}")
    @MethodSource("escapesTestArguments")
    void preservingEscapes(String input) {
        String output = StringWithPlaceholders.withEscapes(input).replaceOutsidePlaceholders(StringReplaceFunction.NO_REPLACEMENTS);
        assertThat(output).isEqualTo(input);
    }

    static Stream<Arguments> escapesTestArguments() {
        return Stream.of(
                Arguments.of("\\{", "{"),
                Arguments.of("\\}", "}"),
                Arguments.of("\\\\", "\\"),
                Arguments.of("x\\", "x\\"),
                Arguments.of("\\x", "\\x"),
                Arguments.of("{\\{x\\}}", "<{x}>"),
                Arguments.of("\\{{x}", "{<x>"),
                Arguments.of("{x\\}}", "<x}>"),
                Arguments.of("{\\x}", "<\\x>"),
                Arguments.of("{\\{}", "<{>"),
                Arguments.of("{\\}}", "<}>"),
                Arguments.of("{\\\\}", "<\\>")
        );
    }

    @Test
    void addEscapes() {
        String escaped = StringWithPlaceholders.addEscapes("} { \\");
        assertThat(escaped).isEqualTo("\\} \\{ \\\\");
    }

}
