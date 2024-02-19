/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.core.tracking;

import me.filoghost.holographicdisplays.common.PositionCoordinates;
import me.filoghost.holographicdisplays.core.base.BaseItemHologramLine;
import me.filoghost.holographicdisplays.core.listener.LineClickListener;
import me.filoghost.holographicdisplays.core.tick.CachedPlayer;
import me.filoghost.holographicdisplays.nms.common.NMSManager;
import me.filoghost.holographicdisplays.nms.common.entity.ItemNMSPacketEntity;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.MustBeInvokedByOverriders;

import java.util.List;
import java.util.Objects;

public class ItemLineTracker extends ClickableLineTracker<Viewer> {

    private final BaseItemHologramLine line;
    private final ItemNMSPacketEntity itemEntity;

    private ItemStack itemStack;
    private boolean itemStackChanged;

    private boolean spawnItemEntity;
    private boolean spawnItemEntityChanged;

    public ItemLineTracker(
            BaseItemHologramLine line,
            NMSManager nmsManager,
            LineClickListener lineClickListener) {
        super(line, nmsManager, lineClickListener);
        this.line = line;
        this.itemEntity = nmsManager.newItemPacketEntity();
    }

    @Override
    public BaseItemHologramLine getLine() {
        return line;
    }

    @MustBeInvokedByOverriders
    @Override
    protected void update(List<CachedPlayer> onlinePlayers, List<CachedPlayer> movedPlayers, int maxViewRange) {
        super.update(onlinePlayers, movedPlayers, maxViewRange);

        if (spawnItemEntity && hasViewers() && line.hasPickupCallback()) {
            for (Viewer viewer : getViewers()) {
                invokePickupIfNecessary(viewer);
            }
        }
    }

    private void invokePickupIfNecessary(Viewer viewer) {
        Location location = viewer.getLocation();
        Player player = viewer.getBukkitPlayer();

        if (location != null && CollisionHelper.isInPickupRange(location, positionCoordinates) && canInteract(player)) {
            line.onPickup(player);
        }
    }

    @Override
    protected boolean updatePlaceholders() {
        return false;
    }

    @Override
    protected Viewer createViewer(CachedPlayer cachedPlayer) {
        return new Viewer(cachedPlayer);
    }

    @MustBeInvokedByOverriders
    @Override
    protected void detectChanges() {
        super.detectChanges();

        ItemStack itemStack = line.getItemStack();
        if (!Objects.equals(this.itemStack, itemStack)) {
            this.itemStack = itemStack;
            this.itemStackChanged = true;
        }

        boolean spawnItemEntity = itemStack != null;
        if (this.spawnItemEntity != spawnItemEntity) {
            this.spawnItemEntity = spawnItemEntity;
            this.spawnItemEntityChanged = true;
        }
    }

    @MustBeInvokedByOverriders
    @Override
    protected void clearDetectedChanges() {
        super.clearDetectedChanges();
        this.itemStackChanged = false;
        this.spawnItemEntityChanged = false;
    }

    @MustBeInvokedByOverriders
    @Override
    protected void sendSpawnPackets(Viewers<Viewer> viewers) {
        super.sendSpawnPackets(viewers);

        if (spawnItemEntity) {
            // Copy for async use
            PositionCoordinates positionCoordinates = this.positionCoordinates;
            ItemStack itemStack = this.itemStack;
            PacketSenderExecutor.execute(() -> {
                viewers.sendPackets(itemEntity.newSpawnPackets(positionCoordinates, itemStack));
            });
        }
    }

    @MustBeInvokedByOverriders
    @Override
    protected void sendDestroyPackets(Viewers<Viewer> viewers) {
        super.sendDestroyPackets(viewers);

        if (spawnItemEntity) {
            PacketSenderExecutor.execute(() -> {
                viewers.sendPackets(itemEntity.newDestroyPackets());
            });
        }
    }

    @MustBeInvokedByOverriders
    @Override
    protected void sendChangesPackets(Viewers<Viewer> viewers) {
        super.sendChangesPackets(viewers);

        if (spawnItemEntityChanged) {
            if (spawnItemEntity) {
                // Copy for async use
                PositionCoordinates positionCoordinates = this.positionCoordinates;
                ItemStack itemStack = this.itemStack;
                PacketSenderExecutor.execute(() -> {
                    viewers.sendPackets(itemEntity.newSpawnPackets(positionCoordinates, itemStack));
                });
            } else {
                PacketSenderExecutor.execute(() -> {
                    viewers.sendPackets(itemEntity.newDestroyPackets());
                });
            }
        } else if (itemStackChanged) {
            // Only send item changes if full spawn/destroy packets were not sent
            // Copy for async use
            ItemStack itemStack = this.itemStack;
            PacketSenderExecutor.execute(() -> {
                viewers.sendPackets(itemEntity.newChangePackets(itemStack));
            });
        }
    }

    @MustBeInvokedByOverriders
    @Override
    protected void sendPositionChangePackets(Viewers<Viewer> viewers) {
        super.sendPositionChangePackets(viewers);

        if (spawnItemEntity) {
            // Copy for async use
            PositionCoordinates positionCoordinates = this.positionCoordinates;
            PacketSenderExecutor.execute(() -> {
                viewers.sendPackets(itemEntity.newTeleportPackets(positionCoordinates));
            });
        }
    }

    @Override
    protected double getViewRange() {
        return 16;
    }

}
