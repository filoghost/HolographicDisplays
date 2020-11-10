/*
 * Copyright (C) Jan Schultke
 *
 * SPDX-License-Identifier: MIT
 */
package me.filoghost.holographicdisplays.nbt;

/**
 * The {@code TAG_Long} tag.
 */
public final class NBTLong extends NBTTag implements Cloneable {

    private long value;

    public NBTLong(long value) {
        this.value = value;
    }

    @Override
    public Long getValue() {
        return value;
    }

    public long getLongValue() {
        return value;
    }

    public void setLongValue(long value) {
        this.value = value;
    }

    @Override
    public NBTType getType() {
        return NBTType.LONG;
    }

    // MISC

    @Override
    public boolean equals(Object obj) {
        return obj instanceof NBTLong && equals((NBTLong) obj);
    }

    public boolean equals(NBTLong tag) {
        return this.value == tag.value;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(value);
    }

    @Override
    public String toMSONString() {
        return value + "L";
    }

    @Override
    public NBTLong clone() {
        return new NBTLong(value);
    }

}
