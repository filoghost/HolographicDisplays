/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.object.base;

import me.filoghost.holographicdisplays.core.nms.entity.NMSArmorStand;
import me.filoghost.holographicdisplays.core.object.base.BaseHologram;
import me.filoghost.holographicdisplays.core.object.base.BaseTouchableLine;
import me.filoghost.holographicdisplays.placeholder.PlaceholdersManager;
import me.filoghost.holographicdisplays.placeholder.RelativePlaceholder;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class BaseTextLine extends BaseTouchableLine {

    private String text;
    private List<RelativePlaceholder> relativePlaceholders;
    private NMSArmorStand nameableEntity;
    
    
    public BaseTextLine(BaseHologram parent, String text) {
        super(parent);
        setText(text);
    }
    
    public String getText() {
        return text;
    }
    
    public void setText(String text) {
        this.text = text;
        
        if (nameableEntity != null) {
            if (text != null && !text.isEmpty()) {
                nameableEntity.setCustomNameNMS(text);
                if (getBaseParent().isAllowPlaceholders()) {
                    PlaceholdersManager.trackIfNecessary(this);
                }
            } else {
                nameableEntity.setCustomNameNMS(""); // It will not appear
                if (getBaseParent().isAllowPlaceholders()) {
                    PlaceholdersManager.untrack(this);
                }
            }
        }
        
        if (text != null) {
            for (RelativePlaceholder relativePlaceholder : RelativePlaceholder.getRegistry()) {
                if (text.contains(relativePlaceholder.getTextPlaceholder())) {
                    if (relativePlaceholders == null) {
                        relativePlaceholders = new ArrayList<>();
                    }
                    relativePlaceholders.add(relativePlaceholder);
                }
            }
        }
        
        // Deallocate the list if unused
        if (relativePlaceholders != null && relativePlaceholders.isEmpty()) {
            relativePlaceholders = null;
        }
    }

    @Override
    public void spawnEntities(World world, double x, double y, double z) {
        super.spawnEntities(world, x, y, z);
            
        nameableEntity = getNMSManager().spawnNMSArmorStand(world, x, y + getTextSpawnOffset(), z, this);

        if (text != null && !text.isEmpty()) {
            nameableEntity.setCustomNameNMS(text);
        }

        if (getBaseParent().isAllowPlaceholders()) {
            PlaceholdersManager.trackIfNecessary(this);
        }
    }
    
    @Override
    public void teleportEntities(double x, double y, double z) {
        super.teleportEntities(x, y, z);

        if (nameableEntity != null) {
            nameableEntity.setLocationNMS(x, y + getTextSpawnOffset(), z);
        }
    }

    @Override
    public void despawnEntities() {
        super.despawnEntities();
        
        if (nameableEntity != null) {
            nameableEntity.killEntityNMS();
            nameableEntity = null;
        }
    }

    public Collection<RelativePlaceholder> getRelativePlaceholders() {
        return relativePlaceholders;
    }

    public boolean hasRelativePlaceholders() {
        return getRelativePlaceholders() != null && !getRelativePlaceholders().isEmpty();
    }

    @Override
    public double getHeight() {
        return 0.23;
    }
    
    @Override
    public void collectEntityIDs(Collection<Integer> collector) {
        super.collectEntityIDs(collector);
        
        if (nameableEntity != null) {
            collector.add(nameableEntity.getIdNMS());
        }
    }

    public NMSArmorStand getNMSNameable() {
        return nameableEntity;
    }

    private double getTextSpawnOffset() {
        return -0.29;
    }

    @Override
    public String toString() {
        return "TextLine [text=" + text + "]";
    }
    
}
