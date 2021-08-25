/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.hologram.tracking;

import me.filoghost.holographicdisplays.nms.common.NMSManager;
import me.filoghost.holographicdisplays.nms.common.NMSPacketList;
import me.filoghost.holographicdisplays.nms.common.entity.TextNMSPacketEntity;
import me.filoghost.holographicdisplays.plugin.hologram.base.BaseTextHologramLine;
import me.filoghost.holographicdisplays.plugin.listener.LineClickListener;
import me.filoghost.holographicdisplays.plugin.placeholder.tracking.PlaceholderTracker;
import org.jetbrains.annotations.MustBeInvokedByOverriders;

import java.util.Objects;

public class TextLineTracker extends ClickableLineTracker<BaseTextHologramLine> {

    private final TextNMSPacketEntity textEntity;

    private final DisplayText displayText;
    private boolean displayTextChanged;
    private boolean allowPlaceholders;

    public TextLineTracker(
            BaseTextHologramLine line,
            NMSManager nmsManager,
            LineClickListener lineClickListener,
            PlaceholderTracker placeholderTracker) {
        super(line, nmsManager, lineClickListener);
        this.textEntity = nmsManager.newTextPacketEntity();
        this.displayText = new DisplayText(placeholderTracker);
    }

    @Override
    protected boolean updatePlaceholders() {
        if (!allowPlaceholders) {
            return false;
        }

        boolean placeholdersChanged = displayText.updateGlobalReplacements();
        if (placeholdersChanged) {
            displayTextChanged = true; // Mark as changed to trigger a packet send with updated placeholders
        }
        return placeholdersChanged;
    }

    @MustBeInvokedByOverriders
    @Override
    protected void detectChanges() {
        super.detectChanges();

        String displayText = line.getText();
        if (!Objects.equals(this.displayText.getWithoutReplacements(), displayText)) {
            this.displayText.setWithoutReplacements(displayText);
            this.displayTextChanged = true;
        }

        boolean allowPlaceholders = line.isAllowPlaceholders();
        if (this.allowPlaceholders != allowPlaceholders) {
            this.allowPlaceholders = allowPlaceholders;
            this.displayTextChanged = true;
        }
    }

    @MustBeInvokedByOverriders
    @Override
    protected void clearDetectedChanges() {
        super.clearDetectedChanges();
        this.displayTextChanged = false;
    }

    @MustBeInvokedByOverriders
    @Override
    protected void addSpawnPackets(NMSPacketList packetList) {
        super.addSpawnPackets(packetList);

        if (!allowPlaceholders) {
            textEntity.addSpawnPackets(packetList, position, displayText.getWithoutReplacements());
        } else if (displayText.containsIndividualPlaceholders()) {
            textEntity.addSpawnPackets(packetList, position, displayText::getWithIndividualReplacements);
        } else {
            textEntity.addSpawnPackets(packetList, position, displayText.getWithGlobalReplacements());
        }
    }

    @MustBeInvokedByOverriders
    @Override
    protected void addDestroyPackets(NMSPacketList packetList) {
        super.addDestroyPackets(packetList);
        textEntity.addDestroyPackets(packetList);
    }

    @Override
    protected void addChangesPackets(NMSPacketList packetList) {
        super.addChangesPackets(packetList);

        if (displayTextChanged) {
            if (!allowPlaceholders) {
                textEntity.addChangePackets(packetList, displayText.getWithoutReplacements());
            } else if (displayText.containsIndividualPlaceholders()) {
                textEntity.addChangePackets(packetList, displayText::getWithIndividualReplacements);
            } else {
                textEntity.addChangePackets(packetList, displayText.getWithGlobalReplacements());
            }
        }
    }

    @MustBeInvokedByOverriders
    @Override
    protected void addPositionChangePackets(NMSPacketList packetList) {
        super.addPositionChangePackets(packetList);
        textEntity.addTeleportPackets(packetList, position);
    }

}
