/*
 * Copyright (C) Jan Schultke
 *
 * SPDX-License-Identifier: MIT
 */
package me.filoghost.holographicdisplays.util.nbt;

/**
 * The {@code TAG_Float} tag.
 */
public final class NBTFloat extends NBTTag implements Cloneable {

    private float value;

    public NBTFloat(float value) {
        this.value = value;
    }

    @Override
    public Float getValue() {
        return value;
    }

    public float getFloatValue() {
        return value;
    }

    public void setFloatValue(float value) {
        this.value = value;
    }

    @Override
    public NBTType getType() {
        return NBTType.FLOAT;
    }

    // MISC

    @Override
    public boolean equals(Object obj) {
        return obj instanceof NBTFloat && equals((NBTFloat) obj);
    }

    public boolean equals(NBTFloat tag) {
        return this.value == tag.value;
    }

    @Override
    public int hashCode() {
        return Float.hashCode(value);
    }

    @Override
    public String toMSONString() {
        return value + "f";
    }

    @Override
    public NBTFloat clone() {
        return new NBTFloat(value);
    }

}
