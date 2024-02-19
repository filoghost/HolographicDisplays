/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.core.tracking;

import me.filoghost.holographicdisplays.core.tick.CachedPlayer;
import me.filoghost.holographicdisplays.nms.common.IndividualTextPacketGroup;

import java.util.Objects;

class TextLineViewer extends Viewer {

    private final DisplayText displayText;

    // Access to these variables must be synchronized, they are accessed from multiple threads
    private String individualText;
    private String lastSentText;
    private String nextTextToSend;

    TextLineViewer(CachedPlayer player, DisplayText displayText) {
        super(player);
        this.displayText = displayText;
    }

    public void sendTextPackets(IndividualTextPacketGroup packets) {
        String text;
        synchronized (this) {
            text = nextTextToSend;
            this.lastSentText = text;
        }
        sendIndividualPackets(packets, text);
    }

    public void sendTextPacketsIfNecessary(IndividualTextPacketGroup packets) {
        String text;
        synchronized (this) {
            text = nextTextToSend;
            if (Objects.equals(lastSentText, text)) {
                return; // Avoid sending unnecessary packets
            }
            this.lastSentText = text;
        }
        sendIndividualPackets(packets, text);
    }

    public synchronized void updateNextTextToSend() {
        if (displayText.containsIndividualPlaceholders()) {
            if (individualText == null) {
                individualText = displayText.computeIndividualText(this);
            }
            nextTextToSend = individualText;
        } else {
            individualText = null;
            nextTextToSend = displayText.getGlobalText();
        }
    }

    public boolean updateIndividualText() {
        String individualText = displayText.computeIndividualText(this);
        synchronized (this) {
            if (!Objects.equals(this.individualText, individualText)) {
                this.individualText = individualText;
                return true;
            } else {
                return false;
            }
        }
    }

}
