/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.object.line;

import me.filoghost.holographicdisplays.api.handler.TouchHandler;
import me.filoghost.holographicdisplays.api.line.TextLine;
import me.filoghost.holographicdisplays.nms.interfaces.entity.NMSArmorStand;
import me.filoghost.holographicdisplays.nms.interfaces.entity.NMSNameable;
import me.filoghost.holographicdisplays.object.BaseHologram;
import me.filoghost.holographicdisplays.placeholder.PlaceholdersManager;
import me.filoghost.holographicdisplays.placeholder.RelativePlaceholder;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TextLineImpl extends TouchableLineImpl implements TextLine {

    private String text;
    private List<RelativePlaceholder> relativePlaceholders;
    private NMSArmorStand nmsNameable;
    
    
    public TextLineImpl(BaseHologram parent, String text) {
        super(parent);
        setText(text);
    }
    
    @Override
    public String getText() {
        return text;
    }
    
    @Override
    public void setText(String text) {
        this.text = text;
        
        if (nmsNameable != null) {
            if (text != null && !text.isEmpty()) {
                nmsNameable.setCustomNameNMS(text);
                if (getParent().isAllowPlaceholders()) {
                    PlaceholdersManager.trackIfNecessary(this);
                }
            } else {
                nmsNameable.setCustomNameNMS(""); // It will not appear
                if (getParent().isAllowPlaceholders()) {
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
    public void setTouchHandler(TouchHandler touchHandler) {
        if (nmsNameable != null) {
            Location loc = nmsNameable.getBukkitEntityNMS().getLocation();
            super.setTouchHandler(touchHandler, loc.getWorld(), loc.getX(), loc.getY() - getTextOffset(), loc.getZ());
        } else {
            super.setTouchHandler(touchHandler, null, 0, 0, 0);
        }
    }

    @Override
    public void spawnEntities(World world, double x, double y, double z) {
        super.spawnEntities(world, x, y, z);
            
        nmsNameable = getNMSManager().spawnNMSArmorStand(world, x, y + getTextOffset(), z, this);

        if (text != null && !text.isEmpty()) {
            nmsNameable.setCustomNameNMS(text);
        }
    }

    
    @Override
    public void despawnEntities() {
        super.despawnEntities();
        
        if (nmsNameable != null) {
            nmsNameable.killEntityNMS();
            nmsNameable = null;
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
    public void teleport(double x, double y, double z) {
        super.teleport(x, y, z);
        
        if (nmsNameable != null) {
            nmsNameable.setLocationNMS(x, y + getTextOffset(), z);
        }
    }
    
    @Override
    public int[] getEntitiesIDs() {
        if (isSpawned()) {
            if (touchSlime != null) {
                return ArrayUtils.add(touchSlime.getEntitiesIDs(), nmsNameable.getIdNMS());
            } else {
                return new int[] {nmsNameable.getIdNMS()};
            }
        } else {
            return new int[0];
        }
    }

    public NMSNameable getNmsNameable() {
        return nmsNameable;
    }

    private double getTextOffset() {
        return Offsets.ARMOR_STAND_ALONE;
    }

    @Override
    public String toString() {
        return "TextLine [text=" + text + "]";
    }
    
}
