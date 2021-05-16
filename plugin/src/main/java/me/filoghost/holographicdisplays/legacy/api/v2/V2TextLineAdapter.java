/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.legacy.api.v2;

import com.gmail.filoghost.holographicdisplays.api.line.TextLine;
import me.filoghost.holographicdisplays.object.api.APITextLine;

@SuppressWarnings("deprecation")
public class V2TextLineAdapter extends V2TouchableLineAdapter implements TextLine {

    private final APITextLine newTextLine;

    public V2TextLineAdapter(APITextLine newTextLine) {
        super(newTextLine);
        this.newTextLine = newTextLine;
    }

    @Override
    public String getText() {
        return newTextLine.getText();
    }

    @Override
    public void setText(String text) {
        newTextLine.setText(text);
    }

}
