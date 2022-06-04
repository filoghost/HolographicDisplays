/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.internal.hologram;

import me.filoghost.holographicdisplays.api.hologram.Hologram;

public class TextInternalHologramLine extends InternalHologramLine {

    private final String text;

    public TextInternalHologramLine(String serializedString, String text) {
        super(serializedString);
        this.text = text;
    }

    @Override
    public void appendTo(Hologram hologram) {
        hologram.getLines().appendText(text);
    }

}
