/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.api.v2;

import com.gmail.filoghost.holographicdisplays.api.line.TextLine;
import me.filoghost.holographicdisplays.plugin.hologram.api.APITextLine;

@SuppressWarnings("deprecation")
public class V2TextLineAdapter extends V2TouchableLineAdapter implements TextLine {

    private final APITextLine v3TextLine;

    public V2TextLineAdapter(APITextLine v3TextLine) {
        super(v3TextLine);
        this.v3TextLine = v3TextLine;
    }

    @Override
    public String getText() {
        return v3TextLine.getText();
    }

    @Override
    public void setText(String text) {
        v3TextLine.setText(text);
    }

}
