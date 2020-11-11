/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.bridge.protocollib.current;

import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import me.filoghost.holographicdisplays.bridge.protocollib.current.packet.AbstractPacket;
import me.filoghost.holographicdisplays.bridge.protocollib.current.packet.WrapperPlayServerAttachEntity;
import me.filoghost.holographicdisplays.bridge.protocollib.current.packet.WrapperPlayServerEntityDestroy;
import me.filoghost.holographicdisplays.bridge.protocollib.current.packet.WrapperPlayServerEntityMetadata;
import me.filoghost.holographicdisplays.bridge.protocollib.current.packet.WrapperPlayServerMount;
import me.filoghost.holographicdisplays.bridge.protocollib.current.packet.WrapperPlayServerSpawnEntity;
import me.filoghost.holographicdisplays.bridge.protocollib.current.packet.WrapperPlayServerSpawnEntity.ObjectTypes;
import me.filoghost.holographicdisplays.bridge.protocollib.current.packet.WrapperPlayServerSpawnEntityLiving;
import me.filoghost.holographicdisplays.nms.interfaces.entity.NMSArmorStand;
import me.filoghost.holographicdisplays.nms.interfaces.entity.NMSEntityBase;
import me.filoghost.holographicdisplays.nms.interfaces.entity.NMSItem;
import me.filoghost.holographicdisplays.nms.interfaces.entity.NMSSlime;
import me.filoghost.holographicdisplays.common.NMSVersion;
import org.bukkit.entity.Player;

import java.util.List;

public class PacketHelper {
    
    private MetadataHelper metadataHelper;
    
    public PacketHelper(MetadataHelper metadataHelper) {
        this.metadataHelper = metadataHelper;
    }
    
    public void sendSpawnArmorStandPacket(Player receiver, NMSArmorStand armorStand) {        
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
    
    
    public void sendSpawnItemPacket(Player receiver, NMSItem item) {
        AbstractPacket packet = new WrapperPlayServerSpawnEntity(item.getBukkitEntityNMS(), ObjectTypes.ITEM_STACK, 1);
        packet.sendPacket(receiver);
    }
    
    
    public void sendSpawnSlimePacket(Player receiver, NMSSlime slime) {
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
    
    
    public void sendItemMetadataPacket(Player receiver, NMSItem item) {
        WrapperPlayServerEntityMetadata packet = new WrapperPlayServerEntityMetadata();
        
        WrappedDataWatcher dataWatcher = new WrappedDataWatcher();
        metadataHelper.setItemMetadata(dataWatcher, item.getRawItemStack());
        packet.setEntityMetadata(dataWatcher.getWatchableObjects());
        
        packet.setEntityID(item.getIdNMS());
        packet.sendPacket(receiver);
    }
    
    
    public void sendVehicleAttachPacket(Player receiver, NMSEntityBase vehicle, NMSEntityBase passenger) {        
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
    
    
    public void sendDestroyEntitiesPacket(Player player, List<Integer> ids) {
        WrapperPlayServerEntityDestroy packet = new WrapperPlayServerEntityDestroy();
        packet.setEntities(ids);
        packet.sendPacket(player);
    }
    

}
