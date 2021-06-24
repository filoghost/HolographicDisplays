/*
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package com.gmail.filoghost.holographicdisplays.bridge.protocollib.current;

import java.util.List;

import org.bukkit.entity.Player;

import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.gmail.filoghost.holographicdisplays.bridge.protocollib.current.packet.AbstractPacket;
import com.gmail.filoghost.holographicdisplays.bridge.protocollib.current.packet.WrapperPlayServerAttachEntity;
import com.gmail.filoghost.holographicdisplays.bridge.protocollib.current.packet.WrapperPlayServerEntityDestroy;
import com.gmail.filoghost.holographicdisplays.bridge.protocollib.current.packet.WrapperPlayServerEntityMetadata;
import com.gmail.filoghost.holographicdisplays.bridge.protocollib.current.packet.WrapperPlayServerMount;
import com.gmail.filoghost.holographicdisplays.bridge.protocollib.current.packet.WrapperPlayServerSpawnEntity;
import com.gmail.filoghost.holographicdisplays.bridge.protocollib.current.packet.WrapperPlayServerSpawnEntityLiving;
import com.gmail.filoghost.holographicdisplays.bridge.protocollib.current.packet.WrapperPlayServerSpawnEntity.ObjectTypes;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.entity.NMSArmorStand;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.entity.NMSEntityBase;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.entity.NMSItem;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.entity.NMSSlime;
import com.gmail.filoghost.holographicdisplays.util.NMSVersion;

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
		if (NMSVersion.isGreaterEqualThan(NMSVersion.v1_17_R1)) {
			// Requires multiple packets
			for (Integer id : ids) {
				WrapperPlayServerEntityDestroy packet = new WrapperPlayServerEntityDestroy();
				packet.setEntity_1_17(id);
				packet.sendPacket(player);
			}
		} else {
			WrapperPlayServerEntityDestroy packet = new WrapperPlayServerEntityDestroy();
			packet.setEntities(ids);
			packet.sendPacket(player);
		}
	}
	

}
