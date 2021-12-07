/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.hologram.tracking;

import me.filoghost.holographicdisplays.nms.common.IndividualTextPacketGroup;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

class TextLineViewer extends Viewer {

    private final DisplayText displayText;

    private String individualText;
    private String lastSentText;

    TextLineViewer(Player player, DisplayText displayText) {
        super(player);
        this.displayText = displayText;
    }

    public void sendTextPackets(IndividualTextPacketGroup packets) {
        String text = getOrComputeText();
        this.lastSentText = text;
        packets.sendTo(getPlayer(), text);
    }

    public void sendTextPacketsIfNecessary(IndividualTextPacketGroup packets) {
        String text = getOrComputeText();
        if (Objects.equals(lastSentText, text)) {
            return; // Avoid sending unnecessary packets
        }
        this.lastSentText = text;
        packets.sendTo(getPlayer(), text);
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
