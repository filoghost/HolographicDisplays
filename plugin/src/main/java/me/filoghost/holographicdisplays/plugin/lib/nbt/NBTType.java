/*
 * Copyright (C) Jan Schultke
 *
 * SPDX-License-Identifier: MIT
 */
package me.filoghost.holographicdisplays.plugin.lib.nbt;

/**
 * <p>
 * The type of an NBTTag.
 * </p>
 * <p>
 * This enum may be prone to further additions, such as the {@link #LONG_ARRAY} which has been added by Mojang
 * in NBT Version 19133. (second NBT version)
 * </p>
 * <p>
 * For a community maintained documentation of the NBT format and its types, visit the
 * <a href=https://minecraft.wiki/w/NBT_format>Minecraft Wiki</a>
 * </p>
 */
public enum NBTType {
    /**
     * Used to mark the end of compounds tags. May also be the type of empty list tags.
     *
     * @since NBT Version 19132
     */
    END("TAG_End", false, false, false),

    /**
     * A signed integer (8 bits). Sometimes used for booleans. (-128 to 127)
     *
     * @since NBT Version 19132
     */
    BYTE("TAG_Byte", true, true, false),

    /**
     * A signed integer (16 bits). (-2<sup>15</sup> to 2<sup>15</sup>-1)
     *
     * @since NBT Version 19132
     */
    SHORT("TAG_Short", true, true, false),

    /**
     * A signed integer (32 bits). (-2<sup>31</sup> to 2<sup>31</sup>-1)
     *
     * @since NBT Version 19132
     */
    INT("TAG_Int", true, true, false),

    /**
     * A signed integer (64 bits). (-2<sup>63</sup> to 2<sup>63</sup>-1)
     *
     * @since NBT Version 19132
     */
    LONG("TAG_Long", true, true, false),

    /**
     * A signed (IEEE 754-2008) floating point number (32 bits).
     *
     * @since NBT Version 19132
     */
    FLOAT("TAG_Float", true, true, false),

    /**
     * A signed (IEEE 754-2008) floating point number (64 bits).
     *
     * @since NBT Version 19132
     */
    DOUBLE("TAG_Double", true, true, false),

    /**
     * An array of {@link #BYTE} with maximum length of {@link Integer#MAX_VALUE}.
     *
     * @since NBT Version 19132
     */
    BYTE_ARRAY("TAG_Byte_Array", false, false, true),

    /**
     * UTF-8 encoded string.
     *
     * @since NBT Version 19132
     */
    STRING("TAG_String", true, false, false),

    /**
     * A list of unnamed tags of equal type.
     *
     * @since NBT Version 19132
     */
    LIST("TAG_List", false, false, false),

    /**
     * Compound of named tags followed by {@link #END}.
     *
     * @since NBT Version 19132
     */
    COMPOUND("TAG_Compound", false, false, false),

    /**
     * An array of {@link #BYTE} with maximum length of {@link Integer#MAX_VALUE}.
     *
     * @since NBT Version 19132
     */
    INT_ARRAY("TAG_Int_Array", false, false, true),

    /**
     * An array of {@link #LONG} with maximum length of {@link Integer#MAX_VALUE}.
     *
     * @since NBT Version 19133
     */
    LONG_ARRAY("TAG_Long_Array", false, false, true);

    private final String name;
    private final boolean numeric, primitive, array;
    private final byte id;

    NBTType(String name, boolean primitive, boolean numeric, boolean array) {
        this.name = name;
        this.id = (byte) ordinal();
        this.numeric = numeric;
        this.primitive = primitive;
        this.array = array;
    }

    /**
     * Returns the type with the given id.
     *
     * @param id the id
     * @return the type
     */
    public static NBTType getById(byte id) {
        return values()[id];
    }

    /**
     * <p>
     * Returns the id of this tag type.
     * </p>
     * <p>
     * Although this method is currently equivalent to {@link #ordinal()}, it should always be used in its stead,
     * since it is not guaranteed that this behavior will remain consistent.
     * </p>
     *
     * @return the id
     */
    public byte getId() {
        return id;
    }

    /**
     * Returns the name of this type.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * <p>
     * Returns whether this tag type is numeric.
     * </p>
     * <p>
     * All tag types with payloads that are representable as a {@link Number} are compliant with this definition.
     * </p>
     *
     * @return whether this type is numeric
     */
    public boolean isNumeric() {
        return numeric;
    }

    /**
     * Returns whether this tag type is primitive, meaning that it is not a {@link NBTByteArray}, {@link NBTIntArray},
     * {@link NBTList}, {@link NBTCompound}.
     *
     * @return whether this type is numeric
     */
    public boolean isPrimitive() {
        return primitive;
    }

    /**
     * Returns whether this tag type is an array type such as {@link NBTByteArray} or {@link NBTIntArray}.
     *
     * @return whether this type is an array type
     */
    public boolean isArray() {
        return array;
    }

    @Override
    public String toString() {
        return getName();
    }

}
