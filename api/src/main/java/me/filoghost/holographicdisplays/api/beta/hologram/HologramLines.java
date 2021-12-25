/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.api.beta.hologram;

import me.filoghost.holographicdisplays.api.beta.hologram.line.HologramLine;
import me.filoghost.holographicdisplays.api.beta.hologram.line.ItemHologramLine;
import me.filoghost.holographicdisplays.api.beta.hologram.line.TextHologramLine;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The editable list of lines of a hologram.
 *
 * @since 1
 */
public interface HologramLines {

    /**
     * Adds a new text line at the end.
     *
     * @param text the content of the line, see {@link TextHologramLine#setText(String)}
     * @return the created line
     * @since 1
     */
    @NotNull TextHologramLine appendText(@Nullable String text);

    /**
     * Adds a new item line at the end.
     *
     * @param itemStack the content of the line, see {@link ItemHologramLine#setItemStack(ItemStack)}
     * @return the created line
     * @since 1
     */
    @NotNull ItemHologramLine appendItem(@Nullable ItemStack itemStack);

    /**
     * Inserts a new text line before the given index.
     *
     * @param beforeIndex the index before which the line is inserted, 0 to insert as first
     * @param text the content of the line, see {@link TextHologramLine#setText(String)}
     * @return the created line
     * @throws IndexOutOfBoundsException if the index is out of range (index &lt; 0 || index &gt;= size())
     * @since 1
     */
    @NotNull TextHologramLine insertText(int beforeIndex, @Nullable String text);

    /**
     * Inserts a new item line before the given index.
     *
     * @param beforeIndex the index before which the line is inserted, 0 to insert as first
     * @param itemStack the content of the line, see {@link ItemHologramLine#setItemStack(ItemStack)}
     * @return the created line
     * @throws IndexOutOfBoundsException if the index is out of range (index &lt; 0 || index &gt;= size())
     * @since 1
     */
    @NotNull ItemHologramLine insertItem(int beforeIndex, @NotNull ItemStack itemStack);

    /**
     * Returns the line at the given index.
     *
     * @param index the index of the line to retrieve
     * @return the line at the given index
     * @throws IndexOutOfBoundsException if the index is out of range (index &lt; 0 || index &gt;= size())
     * @since 1
     */
    @NotNull HologramLine get(int index);

    /**
     * Removes the line at the given index.
     *
     * @param index the index of the line to remove
     * @throws IndexOutOfBoundsException if the index is out of range (index &lt; 0 || index &gt;= size())
     * @since 1
     */
    void remove(int index);

    /**
     * Removes a line.
     *
     * @param line the line to be removed
     * @return if the line was found and removed
     * @since 1
     */
    boolean remove(@NotNull HologramLine line);

    /**
     * Removes all the lines.
     *
     * @since 1
     */
    void clear();

    /**
     * Returns the amount of lines.
     *
     * @return the amount of lines
     * @since 1
     */
    int size();

    /**
     * The total height of the lines, including the gaps between them.
     *
     * @return the total height of the lines
     * @since 1
     */
    double getHeight();

}
