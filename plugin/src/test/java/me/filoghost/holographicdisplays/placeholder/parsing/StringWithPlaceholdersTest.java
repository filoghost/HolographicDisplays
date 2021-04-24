/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.placeholder.parsing;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class StringWithPlaceholdersTest {

    @ParameterizedTest(name = "[{index}] {0} -> {1}")
    @MethodSource("replacementsTestArguments")
    void replacements(String input, String expectedOutput) {
        StringWithPlaceholders s = new StringWithPlaceholders(input);
        assertThat(s.replacePlaceholders(occurrence -> "#")).isEqualTo(expectedOutput);
    }
    
    static Stream<Arguments> replacementsTestArguments() {
        return Stream.of(
                Arguments.of("{}", "#"), // Empty placeholder should still be detected
                Arguments.of("{p}{p}", "##"),
                Arguments.of("{p} {p} {p}", "# # #"),
                Arguments.of("{{p}}", "{#}"), // Only the innermost placeholder should be replaced
                Arguments.of("{p abc", "{p abc"), // Placeholder without closing tag
                Arguments.of("abc p}", "abc p}") // Placeholder without opening tag
        );
    }

    @ParameterizedTest(name = "[{index}] {0} -> {1}, {2}, {3}")
    @MethodSource("parsingTestArguments")
    void parsing(String input, String expectedPluginName, String expectedIdentifier, String expectedArgument) {
        StringWithPlaceholders s = new StringWithPlaceholders(input);
        
        List<PlaceholderOccurrence> placeholders = s.getPlaceholders();
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

}
