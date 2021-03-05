/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.object.base;

import me.filoghost.holographicdisplays.core.hologram.StandardHologram;
import me.filoghost.holographicdisplays.core.hologram.StandardTextLine;
import me.filoghost.holographicdisplays.core.nms.NMSManager;
import me.filoghost.holographicdisplays.core.nms.SpawnFailedException;
import me.filoghost.holographicdisplays.core.nms.entity.NMSArmorStand;
import me.filoghost.holographicdisplays.core.placeholder.RelativePlaceholder;
import me.filoghost.holographicdisplays.placeholder.PlaceholdersManager;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.Collection;

public abstract class BaseTextLine extends BaseTouchableLine implements StandardTextLine {

    private final ArrayList<RelativePlaceholder> relativePlaceholders;
    private String text;
    private NMSArmorStand textEntity;
    
    public BaseTextLine(StandardHologram hologram, NMSManager nmsManager, String text) {
        super(hologram, nmsManager);
        this.relativePlaceholders = new ArrayList<>();
        setText(text);
    }
    
    protected abstract boolean isAllowPlaceholders();
    
    @Override
    public String getText() {
        return text;
    }
    
    protected void setText(String text) {
        this.text = text;
        
        if (textEntity != null) {
            if (text != null && !text.isEmpty()) {
                textEntity.setCustomNameNMS(text);
                if (isAllowPlaceholders()) {
                    PlaceholdersManager.trackIfNecessary(this);
                }
            } else {
                textEntity.setCustomNameNMS(""); // It will not appear
                if (isAllowPlaceholders()) {
                    PlaceholdersManager.untrack(this);
                }
            }
        }
        
        relativePlaceholders.clear();
        if (text != null) {
            for (RelativePlaceholder relativePlaceholder : RelativePlaceholder.getRegistry()) {
                if (text.contains(relativePlaceholder.getTextPlaceholder())) {
                    relativePlaceholders.add(relativePlaceholder);
                }
            }
        }
    }

    @Override
    public void spawnEntities(World world, double x, double y, double z) throws SpawnFailedException {
        super.spawnEntities(world, x, y, z);

        textEntity = getNMSManager().spawnNMSArmorStand(world, x, y + getTextSpawnOffset(), z, this);

        if (text != null && !text.isEmpty()) {
            textEntity.setCustomNameNMS(text);
        }

        if (isAllowPlaceholders()) {
            PlaceholdersManager.trackIfNecessary(this);
        }
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
    public Collection<RelativePlaceholder> getRelativePlaceholders() {
        if (!isAllowPlaceholders()) {
            return null;
        }
        
        return relativePlaceholders;
    }

    @Override
    public double getHeight() {
        return 0.23;
    }
    
    @Override
    public void collectEntityIDs(Collection<Integer> collector) {
        super.collectEntityIDs(collector);
        
        if (textEntity != null) {
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
