/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.object.line;

import me.filoghost.holographicdisplays.HolographicDisplays;
import me.filoghost.holographicdisplays.nms.interfaces.entity.NMSArmorStand;
import me.filoghost.holographicdisplays.nms.interfaces.entity.NMSEntityBase;
import me.filoghost.holographicdisplays.nms.interfaces.entity.NMSSlime;
import me.filoghost.holographicdisplays.object.CraftHologram;
import me.filoghost.holographicdisplays.util.Offsets;
import org.bukkit.World;

/**
 * A touch slime that can be applied to a line.
 */
public class CraftTouchSlimeLine extends CraftHologramLine {
    
    // The touchable piece associated with this piece
    private CraftTouchableLine touchablePiece;
    
    private NMSSlime nmsSlime;
    private NMSArmorStand nmsVehicle;

    
    protected CraftTouchSlimeLine(CraftHologram parent, CraftTouchableLine touchablePiece) {
        super(0.5, parent);
        this.touchablePiece = touchablePiece;
    }

    public CraftTouchableLine getTouchablePiece() {
        return touchablePiece;
    }


    @Override
    public void spawn(World world, double x, double y, double z) {
        super.spawn(world, x, y, z);
        
        double offset = getSlimeOffset();
        
        nmsSlime = HolographicDisplays.getNMSManager().spawnNMSSlime(world, x, y + offset, z, this);
        nmsVehicle = HolographicDisplays.getNMSManager().spawnNMSArmorStand(world, x, y + offset, z, this, HolographicDisplays.hasProtocolLibHook());
        
        nmsSlime.setPassengerOfNMS(nmsVehicle);
    }

    
    @Override
    public void despawn() {
        super.despawn();
        
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
    public void teleport(double x, double y, double z) {
        
        double offset = getSlimeOffset();
        
        if (nmsVehicle != null) {
            nmsVehicle.setLocationNMS(x, y + offset, z, HolographicDisplays.hasProtocolLibHook());
        }
        
        if (nmsSlime != null) {
            nmsSlime.setLocationNMS(x, y + offset, z);
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
        return "CraftTouchSlimeLine [touchablePiece=" + touchablePiece + "]";
    }
    
}
