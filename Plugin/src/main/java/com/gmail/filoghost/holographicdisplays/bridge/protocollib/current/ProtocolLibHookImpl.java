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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketAdapter.AdapterParameteters;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedWatchableObject;
import com.gmail.filoghost.holographicdisplays.HolographicDisplays;
import com.gmail.filoghost.holographicdisplays.bridge.protocollib.ProtocolLibHook;
import com.gmail.filoghost.holographicdisplays.bridge.protocollib.current.packet.EntityRelatedPacketWrapper;
import com.gmail.filoghost.holographicdisplays.bridge.protocollib.current.packet.WrapperPlayServerEntityMetadata;
import com.gmail.filoghost.holographicdisplays.bridge.protocollib.current.packet.WrapperPlayServerSpawnEntity;
import com.gmail.filoghost.holographicdisplays.bridge.protocollib.current.packet.WrapperPlayServerSpawnEntityLiving;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.NMSManager;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.entity.NMSArmorStand;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.entity.NMSEntityBase;
import com.gmail.filoghost.holographicdisplays.object.CraftHologram;
import com.gmail.filoghost.holographicdisplays.object.line.CraftHologramLine;
import com.gmail.filoghost.holographicdisplays.object.line.CraftItemLine;
import com.gmail.filoghost.holographicdisplays.object.line.CraftTextLine;
import com.gmail.filoghost.holographicdisplays.object.line.CraftTouchSlimeLine;
import com.gmail.filoghost.holographicdisplays.object.line.CraftTouchableLine;
import com.gmail.filoghost.holographicdisplays.placeholder.RelativePlaceholder;
import com.gmail.filoghost.holographicdisplays.util.NMSVersion;

/**
 * This is for the ProtocolLib versions containing the WrappedDataWatcher.WrappedDataWatcherObject class.
 */
public class ProtocolLibHookImpl implements ProtocolLibHook {
	
	private NMSManager nmsManager;
	private PacketHelper packetHelper;
	private MetadataHelper metadataHelper;
	
	
	@Override
	public boolean hook(Plugin plugin, NMSManager nmsManager) {
		this.nmsManager = nmsManager;
		this.metadataHelper = new MetadataHelper();
		this.packetHelper = new PacketHelper(metadataHelper);

		AdapterParameteters params = PacketAdapter
			.params()
			.plugin(plugin)
			.types(
				PacketType.Play.Server.SPAWN_ENTITY_LIVING,
				PacketType.Play.Server.SPAWN_ENTITY,
				PacketType.Play.Server.ENTITY_METADATA,
				PacketType.Play.Server.REL_ENTITY_MOVE,
				PacketType.Play.Server.REL_ENTITY_MOVE_LOOK)
			.serverSide()
			.listenerPriority(ListenerPriority.NORMAL);
		
		ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(params) {
					  
				@Override
				public void onPacketSending(PacketEvent event) {
					if (event.isPlayerTemporary() || !Bukkit.isPrimaryThread()) {
						return;
					}

					PacketContainer packet = event.getPacket();
					Player player = event.getPlayer();

					// Spawn entity packet
					if (packet.getType() == PacketType.Play.Server.SPAWN_ENTITY_LIVING) {
						WrapperPlayServerSpawnEntityLiving spawnEntityPacket = new WrapperPlayServerSpawnEntityLiving(packet);
						CraftHologramLine hologramLine = getHologramLine(event, spawnEntityPacket);
						
						if (hologramLine == null) {
							return;
						}
						
						if (!hologramLine.getParent().getVisibilityManager().isVisibleTo(player)) {
							event.setCancelled(true);
							return;
						}
						
						if (NMSVersion.isGreaterEqualThan(NMSVersion.v1_15_R1)) {
							// There's no metadata field in 1.15+ on the spawn entity packet
							return;
						}
						
						if (!hologramLine.getParent().isAllowPlaceholders() || !hologramLine.hasRelativePlaceholders()) {
							return;
						}
						
						spawnEntityPacket = new WrapperPlayServerSpawnEntityLiving(packet.deepClone());
						WrappedWatchableObject customNameWatchableObject = metadataHelper.getCustomNameWacthableObject(spawnEntityPacket.getMetadata());
						
						if (customNameWatchableObject == null) {
							return;
						}
						
						replaceRelativePlaceholders(customNameWatchableObject, player, hologramLine.getRelativePlaceholders());
						event.setPacket(spawnEntityPacket.getHandle());

					} else if (packet.getType() == PacketType.Play.Server.SPAWN_ENTITY) {
						WrapperPlayServerSpawnEntity spawnEntityPacket = new WrapperPlayServerSpawnEntity(packet);
						CraftHologramLine hologramLine = getHologramLine(event, spawnEntityPacket);
						
						if (hologramLine == null) {
							return;
						}
						
						if (!hologramLine.getParent().getVisibilityManager().isVisibleTo(player)) {
							event.setCancelled(true);
							return;
						}
					
					} else if (packet.getType() == PacketType.Play.Server.ENTITY_METADATA) {
						WrapperPlayServerEntityMetadata entityMetadataPacket = new WrapperPlayServerEntityMetadata(packet);
						CraftHologramLine hologramLine = getHologramLine(event, entityMetadataPacket);
						
						if (hologramLine == null) {
							return;
						}
						
						if (!hologramLine.getParent().getVisibilityManager().isVisibleTo(player)) {
							event.setCancelled(true);
							return;
						}
						
						if (!hologramLine.getParent().isAllowPlaceholders() || !hologramLine.hasRelativePlaceholders()) {
							return;
						}
						
						entityMetadataPacket = new WrapperPlayServerEntityMetadata(packet.deepClone());
						WrappedWatchableObject customNameWatchableObject = metadataHelper.getCustomNameWatchableObject(entityMetadataPacket.getEntityMetadata());
						
						if (customNameWatchableObject == null) {
							return;
						}
						
						boolean modified = replaceRelativePlaceholders(customNameWatchableObject, player, hologramLine.getRelativePlaceholders());
						if (modified) {
							event.setPacket(entityMetadataPacket.getHandle());
						}
						
					} else if (packet.getType() == PacketType.Play.Server.REL_ENTITY_MOVE || packet.getType() == PacketType.Play.Server.REL_ENTITY_MOVE_LOOK) {
						int entityID = packet.getIntegers().read(0);
						NMSEntityBase nmsEntityBase = getNMSEntityBase(event.getPlayer().getWorld(), entityID);
						
						if (nmsEntityBase instanceof NMSArmorStand) {
							event.setCancelled(true); // Don't send relative movement packets for armor stands, only keep precise teleport packets.
						}
					}
				}
			});
		
		return true;
	}
	
	
	private boolean replaceRelativePlaceholders(WrappedWatchableObject customNameWatchableObject, Player player, Collection<RelativePlaceholder> relativePlaceholders) {
		if (customNameWatchableObject == null) {
			return false;
		}
		
		final Object originalCustomNameNMSObject = metadataHelper.getCustomNameNMSObject(customNameWatchableObject);
		if (originalCustomNameNMSObject == null) {
			return false;
		}
		
		Object replacedCustomNameNMSObject = originalCustomNameNMSObject;
		for (RelativePlaceholder relativePlaceholder : relativePlaceholders) {
			replacedCustomNameNMSObject = nmsManager.replaceCustomNameText(replacedCustomNameNMSObject, relativePlaceholder.getTextPlaceholder(), relativePlaceholder.getReplacement(player));
		}
		
		if (replacedCustomNameNMSObject == originalCustomNameNMSObject) {
			// It means nothing has been replaced, since original custom name has been returned.
			return false;
		}
		
		metadataHelper.setCustomNameNMSObject(customNameWatchableObject, replacedCustomNameNMSObject);
		return true;
	}
	
	
	@Override
	public void sendDestroyEntitiesPacket(Player player, CraftHologram hologram) {
		List<Integer> ids = new ArrayList<>();
		for (CraftHologramLine line : hologram.getLinesUnsafe()) {
			if (line.isSpawned()) {
				for (int id : line.getEntitiesIDs()) {
					ids.add(id);
				}
			}
		}
		
		if (!ids.isEmpty()) {
			packetHelper.sendDestroyEntitiesPacket(player, ids);
		}
	}
	
	
	@Override
	public void sendDestroyEntitiesPacket(Player player, CraftHologramLine line) {
		if (!line.isSpawned()) {
			return;
		}
		
		List<Integer> ids = new ArrayList<>();
		for (int id : line.getEntitiesIDs()) {
			ids.add(id);
		}
		
		if (!ids.isEmpty()) {
			packetHelper.sendDestroyEntitiesPacket(player, ids);
		}
	}
	
	
	@Override
	public void sendCreateEntitiesPacket(Player player, CraftHologram hologram) {
		for (CraftHologramLine line : hologram.getLinesUnsafe()) {
			sendCreateEntitiesPacket(player, line);
		}
	}
	
	
	@Override
	public void sendCreateEntitiesPacket(Player player, CraftHologramLine line) {
		if (!line.isSpawned()) {
			return;
		}
		
		CraftTouchableLine touchableLine;
		
		if (line instanceof CraftTextLine) {
			CraftTextLine textLine = (CraftTextLine) line;
			touchableLine = textLine;
			
			if (textLine.isSpawned()) {
				packetHelper.sendSpawnArmorStandPacket(player, (NMSArmorStand) textLine.getNmsNameable());
			}
			
		} else if (line instanceof CraftItemLine) {
			CraftItemLine itemLine = (CraftItemLine) line;
			touchableLine = itemLine;
			
			if (itemLine.isSpawned()) {
				packetHelper.sendSpawnArmorStandPacket(player, (NMSArmorStand) itemLine.getNmsVehicle());
				packetHelper.sendSpawnItemPacket(player, itemLine.getNmsItem());
				packetHelper.sendVehicleAttachPacket(player, itemLine.getNmsVehicle(), itemLine.getNmsItem());
				packetHelper.sendItemMetadataPacket(player, itemLine.getNmsItem());
			}
		} else {
			throw new IllegalArgumentException("Unexpected hologram line type: " + line.getClass().getName());
		}
		
		if (touchableLine != null && touchableLine.isSpawned() && touchableLine.getTouchSlime() != null) {
			CraftTouchSlimeLine touchSlime = touchableLine.getTouchSlime();
			
			if (touchSlime.isSpawned()) {
				packetHelper.sendSpawnArmorStandPacket(player, (NMSArmorStand) touchSlime.getNmsVehicle());
				packetHelper.sendSpawnSlimePacket(player, touchSlime.getNmsSlime());				
				packetHelper.sendVehicleAttachPacket(player, touchSlime.getNmsVehicle(), touchSlime.getNmsSlime());
			}
		}
	}
	
	
	private CraftHologramLine getHologramLine(PacketEvent packetEvent, EntityRelatedPacketWrapper packetWrapper) {
		return getHologramLine(packetEvent.getPlayer().getWorld(), packetWrapper.getEntityID());
	}
	
	private CraftHologramLine getHologramLine(World world, int entityID) {
		if (entityID < 0) {
			return null;
		}

		NMSEntityBase nmsEntity = getNMSEntityBase(world, entityID);
		if (nmsEntity == null) {
			return null; // Entity not existing or not related to holograms.
		}
		
		return (CraftHologramLine) nmsEntity.getHologramLine();
	}
	
	private NMSEntityBase getNMSEntityBase(World world, int entityID) {
		return HolographicDisplays.getNMSManager().getNMSEntityBaseFromID(world, entityID);
	}
	
}
