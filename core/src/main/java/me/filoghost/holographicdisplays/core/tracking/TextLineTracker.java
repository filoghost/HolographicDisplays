/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.core.tracking;

import me.filoghost.holographicdisplays.common.PositionCoordinates;
import me.filoghost.holographicdisplays.core.base.BaseTextHologramLine;
import me.filoghost.holographicdisplays.core.listener.LineClickListener;
import me.filoghost.holographicdisplays.core.placeholder.tracking.ActivePlaceholderTracker;
import me.filoghost.holographicdisplays.core.tick.CachedPlayer;
import me.filoghost.holographicdisplays.nms.common.IndividualTextPacketGroup;
import me.filoghost.holographicdisplays.nms.common.NMSManager;
import me.filoghost.holographicdisplays.nms.common.entity.TextNMSPacketEntity;
import org.jetbrains.annotations.MustBeInvokedByOverriders;

import java.util.Objects;

public class TextLineTracker extends ClickableLineTracker<TextLineViewer> {

    private final BaseTextHologramLine line;
    private final TextNMSPacketEntity textEntity;

    private final DisplayText displayText;
    private boolean displayTextChanged;

    public TextLineTracker(
            BaseTextHologramLine line,
            NMSManager nmsManager,
            LineClickListener lineClickListener,
            ActivePlaceholderTracker placeholderTracker) {
        super(line, nmsManager, lineClickListener);
        this.line = line;
        this.textEntity = nmsManager.newTextPacketEntity();
        this.displayText = new DisplayText(placeholderTracker);
    }

    @Override
    public BaseTextHologramLine getLine() {
        return line;
    }

    @Override
    protected boolean updatePlaceholders() {
        boolean placeholdersChanged = displayText.updateReplacements(getViewers());
        if (placeholdersChanged) {
            displayTextChanged = true; // Mark as changed to trigger a packet send with updated placeholders
        }
        return placeholdersChanged;
    }

    @Override
    protected TextLineViewer createViewer(CachedPlayer cachedPlayer) {
        return new TextLineViewer(cachedPlayer, displayText);
    }

    @MustBeInvokedByOverriders
    @Override
    protected void detectChanges() {
        super.detectChanges();

        String displayText = line.getText();
        if (!Objects.equals(this.displayText.getUnreplacedText(), displayText)) {
            this.displayText.setUnreplacedText(displayText);
            this.displayTextChanged = true;
        }

        boolean allowPlaceholders = line.isAllowPlaceholders();
        if (this.displayText.isAllowPlaceholders() != allowPlaceholders) {
            this.displayText.setAllowPlaceholders(allowPlaceholders);
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
    protected void sendSpawnPackets(Viewers<TextLineViewer> viewers) {
        super.sendSpawnPackets(viewers);

        // Copy for async use
        PositionCoordinates positionCoordinates = this.positionCoordinates;
        viewers.forEach(TextLineViewer::updateNextTextToSend);
        PacketSenderExecutor.execute(() -> {
            IndividualTextPacketGroup spawnPackets = textEntity.newSpawnPackets(positionCoordinates);
            viewers.forEach(viewer -> viewer.sendTextPackets(spawnPackets));
        });
    }

    @MustBeInvokedByOverriders
    @Override
    protected void sendDestroyPackets(Viewers<TextLineViewer> viewers) {
        super.sendDestroyPackets(viewers);
        PacketSenderExecutor.execute(() -> {
            viewers.sendPackets(textEntity.newDestroyPackets());
        });
    }

    @Override
    protected void sendChangesPackets(Viewers<TextLineViewer> viewers) {
        super.sendChangesPackets(viewers);

        if (displayTextChanged) {
            viewers.forEach(TextLineViewer::updateNextTextToSend);
            PacketSenderExecutor.execute(() -> {
                IndividualTextPacketGroup changePackets = textEntity.newChangePackets();
                viewers.forEach(viewer -> viewer.sendTextPacketsIfNecessary(changePackets));
            });
        }
    }

    @MustBeInvokedByOverriders
    @Override
    protected void sendPositionChangePackets(Viewers<TextLineViewer> viewers) {
        super.sendPositionChangePackets(viewers);
        // Copy for async use
        PositionCoordinates positionCoordinates = this.positionCoordinates;
        PacketSenderExecutor.execute(() -> {
            viewers.sendPackets(textEntity.newTeleportPackets(positionCoordinates));
        });
    }

    @Override
    protected double getViewRange() {
        return 64;
    }

}
