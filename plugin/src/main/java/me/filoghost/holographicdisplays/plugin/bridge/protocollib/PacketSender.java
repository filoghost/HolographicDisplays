/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.bridge.protocollib;

import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import me.filoghost.holographicdisplays.plugin.lib.packetwrapper.AbstractPacket;
import me.filoghost.holographicdisplays.plugin.lib.packetwrapper.WrapperPlayServerAttachEntity;
import me.filoghost.holographicdisplays.plugin.lib.packetwrapper.WrapperPlayServerEntityDestroy;
import me.filoghost.holographicdisplays.plugin.lib.packetwrapper.WrapperPlayServerEntityMetadata;
import me.filoghost.holographicdisplays.plugin.lib.packetwrapper.WrapperPlayServerMount;
import me.filoghost.holographicdisplays.plugin.lib.packetwrapper.WrapperPlayServerSpawnEntity;
import me.filoghost.holographicdisplays.plugin.lib.packetwrapper.WrapperPlayServerSpawnEntity.ObjectTypes;
import me.filoghost.holographicdisplays.plugin.lib.packetwrapper.WrapperPlayServerSpawnEntityLiving;
import me.filoghost.holographicdisplays.common.hologram.StandardHologram;
import me.filoghost.holographicdisplays.common.hologram.StandardHologramLine;
import me.filoghost.holographicdisplays.common.hologram.StandardItemLine;
import me.filoghost.holographicdisplays.common.hologram.StandardTextLine;
import me.filoghost.holographicdisplays.common.hologram.StandardTouchableLine;
import me.filoghost.holographicdisplays.common.nms.entity.NMSArmorStand;
import me.filoghost.holographicdisplays.common.nms.entity.NMSEntity;
import me.filoghost.holographicdisplays.common.nms.entity.NMSItem;
import me.filoghost.holographicdisplays.common.nms.entity.NMSSlime;
import me.filoghost.holographicdisplays.plugin.util.NMSVersion;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

class PacketSender {
    
    private final MetadataHelper metadataHelper;
    
    PacketSender(MetadataHelper metadataHelper) {
        this.metadataHelper = metadataHelper;
    }

    public void sendDestroyEntitiesPacket(Player player, StandardHologram hologram) {
        List<Integer> ids = new ArrayList<>();
        for (StandardHologramLine line : hologram.getLines()) {
            line.collectTrackedEntityIDs(player, ids);
        }

        if (!ids.isEmpty()) {
            sendDestroyEntitiesPacket(player, ids);
        }
    }

    public void sendCreateEntitiesPacket(Player player, StandardHologram hologram) {
        for (StandardHologramLine line : hologram.getLines()) {
            sendCreateEntitiesPacket(player, line);
        }
    }

    private void sendCreateEntitiesPacket(Player player, StandardHologramLine line) {
        if (line instanceof StandardTextLine) {
            StandardTextLine textLine = (StandardTextLine) line;

            NMSArmorStand armorStand = textLine.getNMSArmorStand();
            if (armorStand != null && armorStand.isTrackedBy(player)) {
                sendSpawnArmorStandPacket(player, armorStand);
            }

        } else if (line instanceof StandardItemLine) {
            StandardItemLine itemLine = (StandardItemLine) line;
            NMSArmorStand itemVehicle = itemLine.getNMSItemVehicle();
            NMSItem item = itemLine.getNMSItem();

            if (itemVehicle != null && itemVehicle.isTrackedBy(player)) {
                sendSpawnArmorStandPacket(player, itemVehicle);
            }

            if (item != null && item.isTrackedBy(player)) {
                sendSpawnItemPacket(player, item);
                sendVehicleAttachPacket(player, itemVehicle, item);
                sendItemMetadataPacket(player, item);
            }
        } else {
            throw new IllegalArgumentException("Unexpected hologram line type: " + line.getClass().getName());
        }

        // All sub-types of lines are touchable, no need to check instance type
        StandardTouchableLine touchableLine = (StandardTouchableLine) line;
        NMSArmorStand slimeVehicle = touchableLine.getNMSSlimeVehicle();
        NMSSlime slime = touchableLine.getNMSSlime();
        
        if (slimeVehicle != null && slimeVehicle.isTrackedBy(player)) {
            sendSpawnArmorStandPacket(player, slimeVehicle);
        }

        if (slime != null && slime.isTrackedBy(player)) {
            sendSpawnSlimePacket(player, slime);
            sendVehicleAttachPacket(player, slimeVehicle, slime);
        }
    }
    
    private void sendSpawnArmorStandPacket(Player receiver, NMSArmorStand armorStand) {
        if (NMSVersion.isGreaterEqualThan(NMSVersion.v1_11_R1)) {
            AbstractPacket spawnPacket;
            if (NMSVersion.isGreaterEqualThan(NMSVersion.v1_14_R1)) {
                spawnPacket = new WrapperPlayServerSpawnEntityLiving(armorStand.getBukkitEntityNMS());
            } else {
                spawnPacket = new WrapperPlayServerSpawnEntity(
                        armorStand.getBukkitEntityNMS(), 
                        WrapperPlayServerSpawnEntity.ObjectTypes.ARMOR_STAND, 1);
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
