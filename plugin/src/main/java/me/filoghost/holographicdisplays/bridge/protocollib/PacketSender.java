/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.bridge.protocollib;

import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import me.filoghost.holographicdisplays.bridge.protocollib.packet.AbstractPacket;
import me.filoghost.holographicdisplays.bridge.protocollib.packet.WrapperPlayServerAttachEntity;
import me.filoghost.holographicdisplays.bridge.protocollib.packet.WrapperPlayServerEntityDestroy;
import me.filoghost.holographicdisplays.bridge.protocollib.packet.WrapperPlayServerEntityMetadata;
import me.filoghost.holographicdisplays.bridge.protocollib.packet.WrapperPlayServerMount;
import me.filoghost.holographicdisplays.bridge.protocollib.packet.WrapperPlayServerSpawnEntity;
import me.filoghost.holographicdisplays.bridge.protocollib.packet.WrapperPlayServerSpawnEntity.ObjectTypes;
import me.filoghost.holographicdisplays.bridge.protocollib.packet.WrapperPlayServerSpawnEntityLiving;
import me.filoghost.holographicdisplays.core.hologram.StandardHologram;
import me.filoghost.holographicdisplays.core.hologram.StandardHologramLine;
import me.filoghost.holographicdisplays.core.hologram.StandardItemLine;
import me.filoghost.holographicdisplays.core.hologram.StandardTextLine;
import me.filoghost.holographicdisplays.core.hologram.StandardTouchableLine;
import me.filoghost.holographicdisplays.core.nms.entity.NMSArmorStand;
import me.filoghost.holographicdisplays.core.nms.entity.NMSEntity;
import me.filoghost.holographicdisplays.core.nms.entity.NMSItem;
import me.filoghost.holographicdisplays.core.nms.entity.NMSSlime;
import me.filoghost.holographicdisplays.util.NMSVersion;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

class PacketSender {
    
    private final MetadataHelper metadataHelper;
    
    public PacketSender(MetadataHelper metadataHelper) {
        this.metadataHelper = metadataHelper;
    }

    public void sendDestroyEntitiesPacket(Player player, StandardHologram hologram) {
        List<Integer> ids = new ArrayList<>();
        for (StandardHologramLine line : hologram.getLinesUnsafe()) { 
            line.collectEntityIDs(ids);
        }

        if (!ids.isEmpty()) {
            sendDestroyEntitiesPacket(player, ids);
        }
    }

    public void sendCreateEntitiesPacket(Player player, StandardHologram hologram) {
        for (StandardHologramLine line : hologram.getLinesUnsafe()) {
            sendCreateEntitiesPacket(player, line);
        }
    }

    private void sendCreateEntitiesPacket(Player player, StandardHologramLine line) {
        if (line instanceof StandardTextLine) {
            StandardTextLine textLine = (StandardTextLine) line;
            
            if (textLine.getNMSArmorStand() != null) {
                sendSpawnArmorStandPacket(player, textLine.getNMSArmorStand());
            }

        } else if (line instanceof StandardItemLine) {
            StandardItemLine itemLine = (StandardItemLine) line;

            if (itemLine.getNMSItem() != null && itemLine.getNMSItemVehicle() != null) {
                sendSpawnArmorStandPacket(player, itemLine.getNMSItemVehicle());
                sendSpawnItemPacket(player, itemLine.getNMSItem());
                sendVehicleAttachPacket(player, itemLine.getNMSItemVehicle(), itemLine.getNMSItem());
                sendItemMetadataPacket(player, itemLine.getNMSItem());
            }
        } else {
            throw new IllegalArgumentException("Unexpected hologram line type: " + line.getClass().getName());
        }

        // All sub-types of lines are touchable, no need to check instance type
        StandardTouchableLine touchableLine = (StandardTouchableLine) line;

        if (touchableLine.getNMSSlime() != null && touchableLine.getNMSSlimeVehicle() != null) {
            sendSpawnArmorStandPacket(player, touchableLine.getNMSSlimeVehicle());
            sendSpawnSlimePacket(player, touchableLine.getNMSSlime());
            sendVehicleAttachPacket(player, touchableLine.getNMSSlimeVehicle(), touchableLine.getNMSSlime());
        }
    }
    
    private void sendSpawnArmorStandPacket(Player receiver, NMSArmorStand armorStand) {        
        if (NMSVersion.isGreaterEqualThan(NMSVersion.v1_11_R1)) {
            AbstractPacket spawnPacket;
            if (NMSVersion.isGreaterEqualThan(NMSVersion.v1_14_R1)) {
                spawnPacket = new WrapperPlayServerSpawnEntityLiving(armorStand.getBukkitEntityNMS());
            } else {
                spawnPacket = new WrapperPlayServerSpawnEntity(armorStand.getBukkitEntityNMS(), WrapperPlayServerSpawnEntity.ObjectTypes.ARMOR_STAND, 1);
            }
            spawnPacket.sendPacket(receiver);
            
            WrapperPlayServerEntityMetadata dataPacket = new WrapperPlayServerEntityMetadata();
            WrappedDataWatcher dataWatcher = new WrappedDataWatcher();
            
            metadataHelper.setEntityStatus(dataWatcher, (byte) 0x20); // Invisible

            String customName = armorStand.getCustomNameStringNMS();
            if (customName != null && !customName.isEmpty()) {
                metadataHelper.setCustomNameNMSObject(dataWatcher, armorStand.getCustomNameObjectNMS());
                metadataHelper.setCustomNameVisible(dataWatcher, true);
            }
            
            metadataHelper.setNoGravity(dataWatcher, true);
            metadataHelper.setArmorStandStatus(dataWatcher, (byte) (0x01 | 0x08 | 0x10)); // Small, no base plate, marker
            
            dataPacket.setEntityMetadata(dataWatcher.getWatchableObjects());
            dataPacket.setEntityID(armorStand.getIdNMS());
            dataPacket.sendPacket(receiver);
            
        } else {
            WrapperPlayServerSpawnEntityLiving spawnPacket = new WrapperPlayServerSpawnEntityLiving(armorStand.getBukkitEntityNMS());
            spawnPacket.sendPacket(receiver);
        }
    }
    
    
    private void sendSpawnItemPacket(Player receiver, NMSItem item) {
        AbstractPacket packet = new WrapperPlayServerSpawnEntity(item.getBukkitEntityNMS(), ObjectTypes.ITEM_STACK, 1);
        packet.sendPacket(receiver);
    }
    
    
    private void sendSpawnSlimePacket(Player receiver, NMSSlime slime) {
        AbstractPacket spawnPacket = new WrapperPlayServerSpawnEntityLiving(slime.getBukkitEntityNMS());
        spawnPacket.sendPacket(receiver);
        
        if (NMSVersion.isGreaterEqualThan(NMSVersion.v1_15_R1)) {
            WrapperPlayServerEntityMetadata dataPacket = new WrapperPlayServerEntityMetadata();
            WrappedDataWatcher dataWatcher = new WrappedDataWatcher();
            
            metadataHelper.setEntityStatus(dataWatcher, (byte) 0x20); // Invisible
            metadataHelper.setSlimeSize(dataWatcher, 1); // Size 1 = small
            
            dataPacket.setEntityMetadata(dataWatcher.getWatchableObjects());
            dataPacket.setEntityID(slime.getIdNMS());
            dataPacket.sendPacket(receiver);
        }
    }
    
    
    private void sendItemMetadataPacket(Player receiver, NMSItem item) {
        WrapperPlayServerEntityMetadata packet = new WrapperPlayServerEntityMetadata();
        
        WrappedDataWatcher dataWatcher = new WrappedDataWatcher();
        metadataHelper.setItemMetadata(dataWatcher, item.getRawItemStack());
        packet.setEntityMetadata(dataWatcher.getWatchableObjects());
        
        packet.setEntityID(item.getIdNMS());
        packet.sendPacket(receiver);
    }
    
    
    private void sendVehicleAttachPacket(Player receiver, NMSEntity vehicle, NMSEntity passenger) {        
        if (NMSVersion.isGreaterEqualThan(NMSVersion.v1_9_R1)) {
            WrapperPlayServerMount packet = new WrapperPlayServerMount();
            packet.setVehicleId(vehicle.getIdNMS());
            packet.setPassengers(new int[] {passenger.getIdNMS()});
            packet.sendPacket(receiver);
        } else {
            WrapperPlayServerAttachEntity packet = new WrapperPlayServerAttachEntity();
            packet.setVehicleId(vehicle.getIdNMS());
            packet.setEntityId(passenger.getIdNMS());
            packet.sendPacket(receiver);
        }
    }
    
    
    private void sendDestroyEntitiesPacket(Player player, List<Integer> ids) {
        WrapperPlayServerEntityDestroy packet = new WrapperPlayServerEntityDestroy();
        packet.setEntities(ids);
        packet.sendPacket(player);
    }
    

}