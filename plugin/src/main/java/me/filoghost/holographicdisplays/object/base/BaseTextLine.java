/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.object.base;

import me.filoghost.holographicdisplays.core.hologram.StandardTextLine;
import me.filoghost.holographicdisplays.core.nms.SpawnFailedException;
import me.filoghost.holographicdisplays.core.nms.entity.NMSArmorStand;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public abstract class BaseTextLine extends BaseTouchableLine implements StandardTextLine {
    
    private String text;
    private NMSArmorStand textEntity;
    
    public BaseTextLine(BaseHologram<?> hologram, String text) {
        super(hologram);
        setText(text);
    }
    
    @Override
    public @Nullable String getText() {
        return text;
    }
    
    public void setText(@Nullable String text) {
        this.text = text;
        
        if (textEntity != null) {
            textEntity.setCustomNameNMS(text);
            getPlaceholderManager().updateTracking(this);
        }
    }

    @Override
    public void spawnEntities(World world, double x, double y, double z) throws SpawnFailedException {
        super.spawnEntities(world, x, y, z);

        textEntity = getNMSManager().spawnNMSArmorStand(world, x, y + getTextSpawnOffset(), z, this);

        if (text != null && !text.isEmpty()) {
            textEntity.setCustomNameNMS(text);
        }

        getPlaceholderManager().updateTracking(this);
    }
    
    @Override
    public void teleportEntities(double x, double y, double z) {
        super.teleportEntities(x, y, z);

        if (textEntity != null) {
            textEntity.setLocationNMS(x, y + getTextSpawnOffset(), z);
        }
    }

    @Override
    public void despawnEntities() {
        super.despawnEntities();
        
        if (textEntity != null) {
            textEntity.killEntityNMS();
            textEntity = null;
        }
    }

    @Override
    public double getHeight() {
        return 0.23;
    }
    
    @Override
    public void collectTrackedEntityIDs(Player player, Collection<Integer> collector) {
        super.collectTrackedEntityIDs(player, collector);
        
        if (textEntity != null && textEntity.isTrackedBy(player)) {
            collector.add(textEntity.getIdNMS());
        }
    }

    @Override
    public NMSArmorStand getNMSArmorStand() {
        return textEntity;
    }

    private double getTextSpawnOffset() {
        return -0.29;
    }

    @Override
    public String toString() {
        return "TextLine [text=" + text + "]";
    }
    
}
