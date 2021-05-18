/*
 * Copyright (C) Jan Schultke
 *
 * SPDX-License-Identifier: MIT
 */
package me.filoghost.holographicdisplays.nbt;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.regex.Pattern;

/**
 * The {@code TAG_Compound} tag.
 */
public final class NBTCompound extends NBTTag {

    private static final Pattern SIMPLE_STRING = Pattern.compile("[A-Za-z0-9._+-]+");

    private final Map<String, NBTTag> value;

    public NBTCompound(Map<String, NBTTag> value) {
        this.value = new LinkedHashMap<>(value);
    }

    public NBTCompound() {
        this.value = new LinkedHashMap<>();
    }

    // GETTERS

    /**
     * Returns the size of this compound.
     *
     * @return the size of this compound
     */
    public int size() {
        return value.size();
    }

    @Override
    public Map<String, NBTTag> getValue() {
        return value;
    }

    @Override
    public NBTType getType() {
        return NBTType.COMPOUND;
    }

    /**
     * Returns a tag named with the given key.
     *
     * @param key the key
     * @return a byte
     * @throws NoSuchElementException if there is no tag with given name
     */
    public NBTTag getTag(String key) {
        if (!hasKey(key)) {
            throw new NoSuchElementException(key);
        }
        return value.get(key);
    }

    /**
     * Returns a byte named with the given key.
     *
     * @param key the key
     * @return a byte
     * @throws NoSuchElementException if there is no byte with given name
     */
    public byte getByte(String key) {
        NBTTag tag = value.get(key);
        if (!(tag instanceof NBTByte)) {
            throw new NoSuchElementException(key);
        }
        return ((NBTByte) tag).getValue();
    }

    /**
     * Returns an short named with the given key.
     *
     * @param key the key
     * @return an short
     * @throws NoSuchElementException if there is no short with given name
     */
    public short getShort(String key) {
        NBTTag tag = value.get(key);
        if (!(tag instanceof NBTShort)) {
            throw new NoSuchElementException(key);
        }
        return ((NBTShort) tag).getValue();
    }

    /**
     * Returns an int named with the given key.
     *
     * @param key the key
     * @return an int
     * @throws NoSuchElementException if there is no int with given name
     */
    public int getInt(String key) {
        NBTTag tag = value.get(key);
        if (!(tag instanceof NBTInt)) {
            throw new NoSuchElementException(key);
        }
        return ((NBTInt) tag).getValue();
    }

    /**
     * Returns an long named with the given key.
     *
     * @param key the key
     * @return an long
     * @throws NoSuchElementException if there is no long with given name
     */
    public long getLong(String key) {
        NBTTag tag = value.get(key);
        if (!(tag instanceof NBTLong)) {
            throw new NoSuchElementException(key);
        }
        return ((NBTLong) tag).getValue();
    }

    /**
     * Returns float named with the given key.
     *
     * @param key the key
     * @return a float
     * @throws NoSuchElementException if there is no float with given name
     */
    public float getFloat(String key) {
        NBTTag tag = value.get(key);
        if (!(tag instanceof NBTFloat)) {
            throw new NoSuchElementException(key);
        }
        return ((NBTFloat) tag).getValue();
    }

    /**
     * Returns a double named with the given key.
     *
     * @param key the key
     * @return a double
     * @throws NoSuchElementException if there is no int with given name
     */
    public double getDouble(String key) {
        NBTTag tag = value.get(key);
        if (!(tag instanceof NBTDouble)) {
            throw new NoSuchElementException(key);
        }
        return ((NBTDouble) tag).getValue();
    }

    /**
     * Returns a byte array named with the given key.
     *
     * @param key the key
     * @return a byte array
     * @throws NoSuchElementException if there is no int with given name
     */
    public byte[] getByteArray(String key) {
        NBTTag tag = value.get(key);
        if (!(tag instanceof NBTByteArray)) {
            throw new NoSuchElementException(key);
        }
        return ((NBTByteArray) tag).getValue();
    }

    /**
     * Returns a string named with the given key.
     *
     * @param key the key
     * @return a string
     * @throws NoSuchElementException if there is no int with given name
     */
    public String getString(String key) {
        NBTTag tag = value.get(key);
        if (!(tag instanceof NBTString)) {
            throw new NoSuchElementException(key);
        }
        return ((NBTString) tag).getValue();
    }

    /**
     * Returns a list named with the given key.
     *
     * @param key the key
     * @return a list
     * @throws NoSuchElementException if there is no int with given name
     */
    public List<NBTTag> getList(String key) {
        return getTagList(key).getValue();
    }

    /**
     * Returns a list named with the given key.
     *
     * @param key the key
     * @return a list
     * @throws NoSuchElementException if there is no list with given name
     */
    public NBTList getTagList(String key) {
        NBTTag tag = value.get(key);
        if (!(tag instanceof NBTList)) {
            throw new NoSuchElementException(key);
        }
        return (NBTList) tag;
    }

    /**
     * Returns a list named with the given key.
     *
     * @param key the key
     * @return a list
     * @throws NoSuchElementException if there is no compound with given name
     */
    public Map<String, NBTTag> getCompound(String key) {
        return getCompoundTag(key).getValue();
    }

    /**
     * Returns a compound named with the given key.
     *
     * @param key the key
     * @return a compound
     * @throws NoSuchElementException if there is no compound with given name
     */
    public NBTCompound getCompoundTag(String key) {
        NBTTag tag = value.get(key);
        if (!(tag instanceof NBTCompound)) {
            throw new NoSuchElementException(key);
        }
        return (NBTCompound) tag;
    }

    /**
     * Returns an int array named with the given key.
     *
     * @param key the key
     * @return a int array
     * @throws NoSuchElementException if there is no int array with given name
     */
    public int[] getIntArray(String key) {
        NBTTag tag = value.get(key);
        if (!(tag instanceof NBTIntArray)) {
            throw new NoSuchElementException(key);
        }
        return ((NBTIntArray) tag).getValue();
    }

    /**
     * Returns a long array named with the given key.
     *
     * @param key the key
     * @return a int array
     * @throws NoSuchElementException if there is no int array with given name
     */
    public long[] getLongArray(String key) {
        NBTTag tag = value.get(key);
        if (!(tag instanceof NBTLongArray)) {
            throw new NoSuchElementException(key);
        }
        return ((NBTLongArray) tag).getValue();
    }

    /**
     * Returns an immutable set containing all the keys in this compound.
     *
     * @return an immutable set
     */
    public Set<String> getKeys() {
        return Collections.unmodifiableSet(value.keySet());
    }

    // PREDICATES

    /**
     * Returns whether this compound is empty.
     *
     * @return whether this compound is empty
     */
    public boolean isEmpty() {
        return value.isEmpty();
    }

    /**
     * Returns whether this compound tag contains the given key.
     *
     * @param key the given key
     * @return true if the tag contains the given key
     */
    public boolean hasKey(String key) {
        return value.containsKey(key);
    }

    /**
     * Returns whether this compound tag contains the given key and its value is of a given type.
     *
     * @param key  the given key
     * @param type the type of the value
     * @return true if the tag contains an entry with given key and of given type
     */
    public boolean hasKeyOfType(String key, NBTType type) {
        Objects.requireNonNull(type);
        return value.containsKey(key) && value.get(key).getType() == type;
    }

    // MUTATORS

    /**
     * Put the given name and its corresponding tag into the compound tag.
     *
     * @param name the tag name
     * @param tag  the tag value
     */
    public void put(String name, NBTTag tag) {
        this.value.put(name, tag);
    }

    /**
     * Put the given key and value into the compound tag.
     *
     * @param key   they key
     * @param value the value
     */
    public void putByteArray(String key, byte[] value) {
        put(key, new NBTByteArray(value));
    }

    /**
     * Put the given key and value into the compound tag.
     *
     * @param key   they key
     * @param value the value
     */
    public void putByte(String key, byte value) {
        put(key, new NBTByte(value));
    }

    /**
     * Put the given key and value into the compound tag.
     *
     * @param key   they key
     * @param value the value
     */
    public void putDouble(String key, double value) {
        put(key, new NBTDouble(value));
    }

    /**
     * Put the given key and value into the compound tag.
     *
     * @param key   they key
     * @param value the value
     */
    public void putFloat(String key, float value) {
        put(key, new NBTFloat(value));
    }

    /**
     * Put the given key and value into the compound tag.
     *
     * @param key   they key
     * @param value the value
     */
    public void putIntArray(String key, int[] value) {
        put(key, new NBTIntArray(value));
    }

    /**
     * Put the given key and value into the compound tag.
     *
     * @param key   they key
     * @param value the value
     */
    public void putLongArray(String key, long[] value) {
        put(key, new NBTLongArray(value));
    }

    /**
     * Put the given key and value into the compound tag.
     *
     * @param key   they key
     * @param value the value
     */
    public void putInt(String key, int value) {
        put(key, new NBTInt(value));
    }

    /**
     * Put the given key and value into the compound tag.
     *
     * @param key   they key
     * @param value the value
     */
    public void putLong(String key, long value) {
        put(key, new NBTLong(value));
    }

    /**
     * Put the given key and value into the compound tag.
     *
     * @param key   they key
     * @param value the value
     */
    public void putShort(String key, short value) {
        put(key, new NBTShort(value));
    }

    /**
     * Put the given key and value into the compound tag.
     *
     * @param key   they key
     * @param value the value
     */
    public void putString(String key, String value) {
        put(key, new NBTString(value));
    }

    // ITERATION

    /**
     * Performs an action for every pair of keys and tags.
     *
     * @param action the action
     */
    public void forEach(BiConsumer<String, ? super NBTTag> action) {
        this.value.forEach(action);
    }

    // MISC

    @Override
    public boolean equals(Object obj) {
        return obj instanceof NBTCompound && equals((NBTCompound) obj);
    }

    public boolean equals(NBTCompound tag) {
        return this.isEmpty() && tag.isEmpty()
                || this.value.equals(tag.value);
    }

    @Override
    public String toMSONString() {
        StringBuilder builder = new StringBuilder("{");
        Set<String> keys = this.value.keySet();

        for (String key : keys) {
            if (builder.length() > 1) {
                builder.append(',');
            }
            builder
                    .append(SIMPLE_STRING.matcher(key).matches() ? key : NBTString.toMSONString(key))
                    .append(':')
                    .append(this.value.get(key).toMSONString());
        }

        return builder.append("}").toString();
    }

}
