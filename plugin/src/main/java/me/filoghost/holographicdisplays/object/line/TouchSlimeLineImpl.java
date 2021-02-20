/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.object.line;

import me.filoghost.holographicdisplays.nms.interfaces.entity.NMSArmorStand;
import me.filoghost.holographicdisplays.nms.interfaces.entity.NMSEntityBase;
import me.filoghost.holographicdisplays.nms.interfaces.entity.NMSSlime;
import me.filoghost.holographicdisplays.object.BaseHologram;
import org.bukkit.World;

/**
 * A touch slime that can be applied to a line.
 */
public class TouchSlimeLineImpl extends HologramLineImpl {
    
    // The touchable piece associated with this piece
    private final TouchableLineImpl touchablePiece;
    
    private NMSSlime nmsSlime;
    private NMSArmorStand nmsVehicle;

    
    protected TouchSlimeLineImpl(BaseHologram parent, TouchableLineImpl touchablePiece) {
        super(parent);
        this.touchablePiece = touchablePiece;
    }

    public TouchableLineImpl getTouchablePiece() {
        return touchablePiece;
    }


    @Override
    public void spawnEntities(World world, double x, double y, double z) {
        nmsSlime = getNMSManager().spawnNMSSlime(world, x, y + getSlimeOffset(), z, this);
        nmsVehicle = getNMSManager().spawnNMSArmorStand(world, x, y + getSlimeOffset(), z, this);
        
        nmsSlime.setPassengerOfNMS(nmsVehicle);
    }

    
    @Override
    public void despawnEntities() {
        if (nmsSlime != null) {
            nmsSlime.killEntityNMS();
            nmsSlime = null;
        }
        
        if (nmsVehicle != null) {
            nmsVehicle.killEntityNMS();
            nmsVehicle = null;
        }
    }

    @Override
    public double getHeight() {
        return 0.5;
    }

    @Override
    public void teleport(double x, double y, double z) {
        if (nmsVehicle != null) {
            nmsVehicle.setLocationNMS(x, y + getSlimeOffset(), z);
        }
        if (nmsSlime != null) {
            nmsSlime.setLocationNMS(x, y + getSlimeOffset(), z);
        }
    }

    @Override
    public int[] getEntitiesIDs() {
        if (isSpawned()) {
            return new int[] {nmsVehicle.getIdNMS(), nmsSlime.getIdNMS()};
        } else {
            return new int[0];
        }
    }

    public NMSSlime getNmsSlime() {
        return nmsSlime;
    }

    public NMSEntityBase getNmsVehicle() {
        return nmsVehicle;
    }
    
    private double getSlimeOffset() {
        return Offsets.ARMOR_STAND_WITH_SLIME;
    }

    @Override
    public String toString() {
        return "TouchSlimeLine [touchablePiece=" + touchablePiece + "]";
    }
    
}
