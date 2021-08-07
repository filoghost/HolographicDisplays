/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.hologram.tracking;

import me.filoghost.holographicdisplays.common.nms.EntityID;
import me.filoghost.holographicdisplays.common.nms.NMSManager;
import me.filoghost.holographicdisplays.common.nms.NMSPacketList;
import me.filoghost.holographicdisplays.plugin.hologram.base.BaseTextLine;
import me.filoghost.holographicdisplays.plugin.listener.LineClickListener;
import me.filoghost.holographicdisplays.plugin.placeholder.tracking.PlaceholderTracker;
import org.jetbrains.annotations.MustBeInvokedByOverriders;

import java.util.Objects;

public class TextLineTracker extends ClickableLineTracker<BaseTextLine> {

    private final EntityID armorStandEntityID;

    private final DisplayText displayText;
    private boolean displayTextChanged;
    private boolean allowPlaceholders;

    public TextLineTracker(
            BaseTextLine line,
            NMSManager nmsManager,
            LineClickListener lineClickListener,
            PlaceholderTracker placeholderTracker) {
        super(line, nmsManager, lineClickListener);
        this.armorStandEntityID = nmsManager.newEntityID();
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
            packetList.addArmorStandSpawnPackets(armorStandEntityID, positionX, getArmorStandPositionY(), positionZ, displayText.getWithoutReplacements());
        } else if (displayText.containsIndividualPlaceholders()) {
            packetList.addArmorStandSpawnPackets(armorStandEntityID, positionX, getArmorStandPositionY(), positionZ, displayText::getWithIndividualReplacements);
        } else {
            packetList.addArmorStandSpawnPackets(armorStandEntityID, positionX, getArmorStandPositionY(), positionZ, displayText.getWithGlobalReplacements());
        }
    }

    @MustBeInvokedByOverriders
    @Override
    protected void addDestroyPackets(NMSPacketList packetList) {
        super.addDestroyPackets(packetList);
        packetList.addEntityDestroyPackets(armorStandEntityID);
    }

    @Override
    protected void addChangesPackets(NMSPacketList packetList) {
        super.addChangesPackets(packetList);

        if (displayTextChanged) {
            if (!allowPlaceholders) {
                packetList.addArmorStandNameChangePackets(armorStandEntityID, displayText.getWithoutReplacements());
            } else if (displayText.containsIndividualPlaceholders()) {
                packetList.addArmorStandNameChangePackets(armorStandEntityID, displayText::getWithIndividualReplacements);
            } else {
                packetList.addArmorStandNameChangePackets(armorStandEntityID, displayText.getWithGlobalReplacements());
            }
        }
    }

    @MustBeInvokedByOverriders
    @Override
    protected void addPositionChangePackets(NMSPacketList packetList) {
        super.addPositionChangePackets(packetList);
        packetList.addTeleportPackets(armorStandEntityID, positionX, getArmorStandPositionY(), positionZ);
    }

    private double getArmorStandPositionY() {
        return positionY - 0.29;
    }

}
