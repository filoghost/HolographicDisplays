/*
 * Copyright (C) Jan Schultke
 *
 * SPDX-License-Identifier: MIT
 */
package me.filoghost.holographicdisplays.plugin.lib.nbt.parser;

import me.filoghost.holographicdisplays.plugin.lib.nbt.NBTByte;
import me.filoghost.holographicdisplays.plugin.lib.nbt.NBTByteArray;
import me.filoghost.holographicdisplays.plugin.lib.nbt.NBTCompound;
import me.filoghost.holographicdisplays.plugin.lib.nbt.NBTDouble;
import me.filoghost.holographicdisplays.plugin.lib.nbt.NBTFloat;
import me.filoghost.holographicdisplays.plugin.lib.nbt.NBTInt;
import me.filoghost.holographicdisplays.plugin.lib.nbt.NBTIntArray;
import me.filoghost.holographicdisplays.plugin.lib.nbt.NBTList;
import me.filoghost.holographicdisplays.plugin.lib.nbt.NBTLong;
import me.filoghost.holographicdisplays.plugin.lib.nbt.NBTLongArray;
import me.filoghost.holographicdisplays.plugin.lib.nbt.NBTTag;
import me.filoghost.holographicdisplays.plugin.lib.nbt.NBTType;
import me.filoghost.holographicdisplays.plugin.lib.nbt.NBTString;
import me.filoghost.holographicdisplays.plugin.lib.nbt.NBTShort;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public final class MojangsonParser {

    private static final Pattern
            DOUBLE_NS = Pattern.compile("[-+]?(?:[0-9]+[.]|[0-9]*[.][0-9]+)(?:e[-+]?[0-9]+)?", Pattern.CASE_INSENSITIVE),
            DOUBLE_S = Pattern.compile("[-+]?(?:[0-9]+[.]?|[0-9]*[.][0-9]+)(?:e[-+]?[0-9]+)?d", Pattern.CASE_INSENSITIVE),
            FLOAT = Pattern.compile("[-+]?(?:[0-9]+[.]?|[0-9]*[.][0-9]+)(?:e[-+]?[0-9]+)?f", Pattern.CASE_INSENSITIVE),
            BYTE = Pattern.compile("[-+]?(?:0|[1-9][0-9]*)b", Pattern.CASE_INSENSITIVE),
            LONG = Pattern.compile("[-+]?(?:0|[1-9][0-9]*)l", Pattern.CASE_INSENSITIVE),
            SHORT = Pattern.compile("[-+]?(?:0|[1-9][0-9]*)s", Pattern.CASE_INSENSITIVE),
            INT = Pattern.compile("[-+]?(?:0|[1-9][0-9]*)");


    private final String str;
    private int index;

    public static NBTCompound parse(String mson) throws MojangsonParseException {
        return new MojangsonParser(mson).parseRootCompound();
    }

    private MojangsonParser(String str) {
        this.str = str;
    }

    // PARSE

    private NBTCompound parseRootCompound() throws MojangsonParseException {
        skipWhitespace();
        NBTCompound result = parseCompound();
        expectNoTrail();
        return result;
    }

    private String parseCompoundKey() throws MojangsonParseException {
        skipWhitespace();
        if (!hasNext()) {
            throw parseException("expected key");
        }
        return currentChar() == '"' ? parseQuotedString() : parseSimpleString();
    }

    private NBTTag parseStringOrLiteral() throws MojangsonParseException {
        skipWhitespace();
        if (currentChar() == '"') {
            return new NBTString(parseQuotedString());
        }
        String str = parseSimpleString();
        if (str.isEmpty()) {
            throw parseException("expected value");
        }
        return parseLiteral(str);
    }

    private NBTTag parseLiteral(String str) {
        try {
            if (FLOAT.matcher(str).matches()) {
                return new NBTFloat(Float.parseFloat(str.substring(0, str.length() - 1)));
            }
            if (BYTE.matcher(str).matches()) {
                return new NBTByte(Byte.parseByte(str.substring(0, str.length() - 1)));
            }
            if (LONG.matcher(str).matches()) {
                return new NBTLong(Long.parseLong(str.substring(0, str.length() - 1)));
            }
            if (SHORT.matcher(str).matches()) {
                return new NBTShort(Short.parseShort(str.substring(0, str.length() - 1)));
            }
            if (INT.matcher(str).matches()) {
                return new NBTInt(Integer.parseInt(str));
            }
            if (DOUBLE_S.matcher(str).matches()) {
                return new NBTDouble(Double.parseDouble(str.substring(0, str.length() - 1)));
            }
            if (DOUBLE_NS.matcher(str).matches()) {
                return new NBTDouble(Double.parseDouble(str));
            }
            if ("true".equalsIgnoreCase(str)) {
                return new NBTByte((byte) 1);
            }
            if ("false".equalsIgnoreCase(str)) {
                return new NBTByte((byte) 0);
            }
        } catch (NumberFormatException ex) {
            return new NBTString(str);
        }
        return new NBTString(str);
    }

    private String parseQuotedString() throws MojangsonParseException {
        int j = ++this.index;
        StringBuilder builder = null;
        boolean escape = false;

        while (hasNext()) {
            char c = nextChar();
            if (escape) {
                if ((c != '\\') && (c != '"')) {
                    throw parseException("invalid escape of '" + c + "'");
                }
                escape = false;
            } else {
                if (c == '\\') {
                    escape = true;
                    if (builder != null) {
                        continue;
                    }
                    builder = new StringBuilder(this.str.substring(j, this.index - 1));
                    continue;
                }
                if (c == '"') {
                    return builder == null ? this.str.substring(j, this.index - 1) : builder.toString();
                }
            }
            if (builder != null) {
                builder.append(c);
            }
        }
        throw parseException("missing termination quote");
    }

    private String parseSimpleString() {
        int j = this.index;
        while (hasNext() && isSimpleChar(currentChar())) {
            this.index += 1;
        }
        return this.str.substring(j, this.index);
    }

    private NBTTag parseAnything() throws MojangsonParseException {
        skipWhitespace();
        if (!hasNext()) {
            throw parseException("expected value");
        }

        int c = currentChar();
        if (c == '{') {
            return parseCompound();
        } else if (c == '[') {
            return parseDetectedArray();
        } else {
            return parseStringOrLiteral();
        }
    }

    private NBTTag parseDetectedArray() throws MojangsonParseException {
        if (hasCharsLeft(2) && getChar(1) != '"' && getChar(2) == ';') {
            return parseNumArray();
        }
        return parseList();
    }

    private NBTCompound parseCompound() throws MojangsonParseException {
        expectChar('{');

        NBTCompound compound = new NBTCompound();

        skipWhitespace();
        while ((hasNext()) && (currentChar() != '}')) {
            String str = parseCompoundKey();
            if (str.isEmpty()) {
                throw parseException("expected non-empty key");
            }
            expectChar(':');

            compound.put(str, parseAnything());
            if (!advanceToNextArrayElement()) {
                break;
            }
            if (!hasNext()) {
                throw parseException("expected key");
            }
        }
        expectChar('}');

        return compound;
    }

    private NBTList parseList() throws MojangsonParseException {
        expectChar('[');

        skipWhitespace();
        if (!hasNext()) {
            throw parseException("expected value");
        }
        NBTList list = new NBTList();
        NBTType listType = null;

        while (currentChar() != ']') {
            NBTTag element = parseAnything();
            NBTType elementType = element.getType();

            if (listType == null) {
                listType = elementType;
            } else if (elementType != listType) {
                throw parseException("unable to insert " + elementType + " into ListTag of type " + listType);
            }
            list.add(element);
            if (!advanceToNextArrayElement()) {
                break;
            }
            if (!hasNext()) {
                throw parseException("expected value");
            }
        }
        expectChar(']');

        return list;
    }

    private NBTTag parseNumArray() throws MojangsonParseException {
        expectChar('[');
        char arrayType = nextChar();
        expectChar(';');
        //nextChar(); semicolon ignored by Mojang

        skipWhitespace();
        if (!hasNext()) {
            throw parseException("expected value");
        }
        if (arrayType == 'B') {
            return new NBTByteArray(parseNumArray(NBTType.BYTE_ARRAY, NBTType.BYTE));
        } else if (arrayType == 'L') {
            return new NBTLongArray(parseNumArray(NBTType.LONG_ARRAY, NBTType.LONG));
        } else if (arrayType == 'I') {
            return new NBTIntArray(parseNumArray(NBTType.INT_ARRAY, NBTType.INT));
        }
        throw parseException("invalid array type '" + arrayType + "' found");
    }

    private Number[] parseNumArray(NBTType arrayType, NBTType primType) throws MojangsonParseException {
        List<Number> result = new ArrayList<>();
        while (currentChar() != ']') {
            NBTTag element = parseAnything();
            NBTType elementType = element.getType();

            if (elementType != primType) {
                throw parseException("unable to insert " + elementType + " into " + arrayType);
            }
            if (primType == NBTType.BYTE) {
                result.add(((NBTByte) element).getValue());
            } else if (primType == NBTType.LONG) {
                result.add(((NBTLong) element).getValue());
            } else {
                result.add(((NBTInt) element).getValue());
            }
            if (!advanceToNextArrayElement()) {
                break;
            }
            if (!hasNext()) {
                throw parseException("expected value");
            }
        }
        expectChar(']');

        return result.toArray(new Number[0]);
    }

    // CHARACTER NAVIGATION

    private boolean advanceToNextArrayElement() {
        skipWhitespace();
        if (hasNext() && currentChar() == ',') {
            this.index += 1;
            skipWhitespace();
            return true;
        }
        return false;
    }

    private void skipWhitespace() {
        while (hasNext() && Character.isWhitespace(currentChar())) {
            this.index += 1;
        }
    }

    private boolean hasCharsLeft(int paramInt) {
        return this.index + paramInt < this.str.length();
    }

    private boolean hasNext() {
        return hasCharsLeft(0);
    }

    /**
     * Returns the character in the string at the current index plus a given offset.
     *
     * @param offset the offset
     * @return the character at the offset
     */
    private char getChar(int offset) {
        return this.str.charAt(this.index + offset);
    }

    /**
     * Returns the current character.
     *
     * @return the current character
     */
    private char currentChar() {
        return getChar(0);
    }

    /**
     * Returns the current character and increments the index.
     *
     * @return the current character
     */
    private char nextChar() {
        return this.str.charAt(this.index++);
    }

    // UTIL

    /**
     * Verifies whether the current character is of given value and whether the parser can advance. If these conditions
     * are met, the parser advances by one. If these conditions are not met, an exception is thrown.
     *
     * @param c the expected character
     * @throws MojangsonParseException if {@link #currentChar()} does not equal {@code c} or if {@link #hasNext()}
     *                                 returns false
     */
    private void expectChar(char c) throws MojangsonParseException {
        skipWhitespace();

        boolean hasNext = hasNext();
        if (hasNext && currentChar() == c) {
            this.index += 1;
            return;
        }
        Object unexpectedChar = hasNext ? Character.valueOf(currentChar()) : "<End of string>";
        throw new MojangsonParseException("expected '" + c + "' but got '" + unexpectedChar + "'", this.str, this.index + 1);
    }

    /**
     * Verifies that the string has ended or that all characters from the next character on only consists of whitespace.
     *
     * @throws MojangsonParseException if the following characters contain a non-whitespace character
     */
    private void expectNoTrail() throws MojangsonParseException {
        skipWhitespace();
        if (hasNext()) {
            this.index++;
            throw parseException("trailing data found");
        }
    }

    private MojangsonParseException parseException(String paramString) {
        return new MojangsonParseException(paramString, this.str, this.index);
    }

    private static boolean isSimpleChar(char paramChar) {
        return (paramChar >= '0' && paramChar <= '9')
                || (paramChar >= 'A' && paramChar <= 'Z')
                || (paramChar >= 'a' && paramChar <= 'z')
                || paramChar == '_'
                || paramChar == '-'
                || paramChar == '.'
                || paramChar == '+';
    }

}
