/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.core.tracking;

import me.filoghost.holographicdisplays.nms.common.IndividualTextPacketGroup;
import me.filoghost.holographicdisplays.core.tick.CachedPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

class TextLineViewer extends Viewer {

    private final DisplayText displayText;

    private String individualText;
    private String lastSentText;

    TextLineViewer(CachedPlayer player, DisplayText displayText) {
        super(player);
        this.displayText = displayText;
    }

    public void sendTextPackets(IndividualTextPacketGroup packets) {
        String text = getOrComputeText();
        this.lastSentText = text;
        sendIndividualPackets(packets, text);
    }

    public void sendTextPacketsIfNecessary(IndividualTextPacketGroup packets) {
        String text = getOrComputeText();
        if (Objects.equals(lastSentText, text)) {
            return; // Avoid sending unnecessary packets
        }
        this.lastSentText = text;
        sendIndividualPackets(packets, text);
    }

    private @Nullable String getOrComputeText() {
        if (displayText.containsIndividualPlaceholders()) {
            if (individualText == null) {
                individualText = displayText.computeIndividualText(this);
            }
            return individualText;
        } else {
            individualText = null;
            return displayText.getGlobalText();
        }
    }

    public boolean updateIndividualText() {
        String individualText = displayText.computeIndividualText(this);
        if (!Objects.equals(this.individualText, individualText)) {
            this.individualText = individualText;
            return true;
        } else {
            return false;
        }
    }

}
