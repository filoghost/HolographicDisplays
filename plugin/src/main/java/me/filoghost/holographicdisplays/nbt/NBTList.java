/*
 * Copyright (C) Jan Schultke
 *
 * SPDX-License-Identifier: MIT
 */
package me.filoghost.holographicdisplays.nbt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * The {@code TAG_List} tag.
 */
public final class NBTList extends NBTTag implements Iterable<NBTTag>, Cloneable {

    private NBTType type;

    private final List<NBTTag> list = new ArrayList<>();

    /**
     * Creates the list with a type and a series of elements.
     *
     * @param type  the type of tag
     * @param value the value of the tag
     */
    public NBTList(NBTType type, List<? extends NBTTag> value) {
        this.type = type;
        for (NBTTag entry : value) {
            this.add(entry);
        }
    }

    /**
     * Creates the list with a type and a series of elements.
     *
     * @param type  the type of tag
     * @param value the value of the tag
     */
    public NBTList(NBTType type, NBTTag... value) {
        this(type, Arrays.asList(value));
    }

    /**
     * Creates an empty list with a type.
     *
     * @param type the type of tag or null if the list has no type yet
     */
    public NBTList(NBTType type) {
        this.type = type;
    }

    /**
     * Creates an empty list without a type.
     */
    public NBTList() {
        this(null);
    }

    // GETTERS

    /**
     * Returns the size of this list.
     *
     * @return the size of this list
     */
    public int size() {
        return list.size();
    }

    @Override
    public List<NBTTag> getValue() {
        return list;
    }

    @Override
    public NBTType getType() {
        return NBTType.LIST;
    }

    /**
     * Gets the type of elements in this list.
     *
     * @return The type of elements in this list.
     */
    public NBTType getElementType() {
        return type;
    }

    /**
     * Returns a tag named with the given index.
     *
     * @param index the index
     * @return a byte
     * @throws NoSuchElementException if there is no tag with given index
     */
    public NBTTag get(int index) {
        return list.get(index);
    }

    // PREDICATES

    /**
     * Returns whether this list is empty.
     *
     * @return whether this list is empty
     */
    public boolean isEmpty() {
        return list.isEmpty();
    }

    // MUTATORS

    /**
     * Add the given tag.
     *
     * @param value the tag
     */
    public void add(NBTTag value) {
        if (this.type == null) {
            this.type = value.getType();
        } else if (this.type != value.getType()) {
            throw new IllegalArgumentException(value.getType() + " is not of expected type " + type);
        }
        list.add(value);
    }

    /**
     * Add the given tag at the given index in the list.
     *
     * @param value the tag
     */
    public void add(int index, NBTTag value) {
        if (index < 0 || index >= list.size()) {
            throw new IndexOutOfBoundsException(Integer.toString(index));
        }
        if (this.type == null) {
            this.type = value.getType();
        } else if (this.type != value.getType()) {
            throw new IllegalArgumentException(value.getType() + " is not of expected type " + type);
        }
        list.add(index, value);
    }

    /**
     * Add all the tags in the given list.
     *
     * @param values a list of tags
     */
    public void addAll(Collection<? extends NBTTag> values) {
        for (NBTTag entry : values) {
            this.add(entry);
        }
    }

    // MISC

    @Override
    public boolean equals(Object obj) {
        return obj instanceof NBTList && equals((NBTList) obj);
    }

    public boolean equals(NBTList tag) {
        return this.isEmpty() && tag.isEmpty()
                || this.type == tag.type && this.list.equals(tag.list);
    }

    @Override
    public Iterator<NBTTag> iterator() {
        return list.iterator();
    }

    @Override
    public String toMSONString() {
        StringBuilder builder = new StringBuilder("[");
        Iterator<NBTTag> iter = iterator();

        boolean first = true;
        while (iter.hasNext()) {
            if (first) {
                first = false;
            } else {
                builder.append(',');
            }
            builder.append(iter.next().toMSONString());
        }

        return builder.append("]").toString();
    }

    @Override
    public NBTList clone() {
        return new NBTList(type, list);
    }

}
