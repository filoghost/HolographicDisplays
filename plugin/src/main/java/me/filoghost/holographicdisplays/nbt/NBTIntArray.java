/*
 * Copyright (C) Jan Schultke
 *
 * SPDX-License-Identifier: MIT
 */
package me.filoghost.holographicdisplays.nbt;

import java.util.Arrays;
import java.util.Objects;

/**
 * The {@code TAG_Int_Array} tag.
 */
public final class NBTIntArray extends NBTTag implements Cloneable {

    private final int[] value;

    /**
     * Creates the tag with an empty name.
     *
     * @param value the value of the tag
     */
    public NBTIntArray(int[] value) {
        this.value = Objects.requireNonNull(value);
    }

    public NBTIntArray(Number[] numbers) {
        this.value = new int[numbers.length];
        for (int i = 0; i < numbers.length; i++)
            value[i] = numbers[i].intValue();
    }

    /**
     * Returns the length of this array.
     *
     * @return the length of this array
     */
    public int length() {
        return value.length;
    }

    @Override
    public int[] getValue() {
        return value;
    }

    @Override
    public NBTType getType() {
        return NBTType.INT_ARRAY;
    }

    // MISC

    @Override
    public boolean equals(Object obj) {
        return obj instanceof NBTIntArray && equals((NBTIntArray) obj);
    }

    public boolean equals(NBTIntArray tag) {
        return Arrays.equals(this.value, tag.value);
    }

    @Override
    public String toMSONString() {
        StringBuilder stringbuilder = new StringBuilder("[I;");
        for (int i = 0; i < this.value.length; i++) {
            if (i != 0) {
                stringbuilder.append(',');
            }
            stringbuilder.append(this.value[i]);
        }
        return stringbuilder.append(']').toString();
    }

    @Override
    public NBTIntArray clone() {
        return new NBTIntArray(value);
    }

}
