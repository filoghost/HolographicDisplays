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
import java.util.Optional;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.comphenix.net.sf.cglib.proxy.Factory;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketAdapter.AdapterParameteters;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedDataWatcher.Registry;
import com.comphenix.protocol.wrappers.WrappedDataWatcher.Serializer;
import com.comphenix.protocol.wrappers.WrappedDataWatcher.WrappedDataWatcherObject;
import com.comphenix.protocol.wrappers.WrappedWatchableObject;
import com.gmail.filoghost.holographicdisplays.HolographicDisplays;
import com.gmail.filoghost.holographicdisplays.bridge.protocollib.ProtocolLibHook;
import com.gmail.filoghost.holographicdisplays.bridge.protocollib.current.packet.AbstractPacket;
import com.gmail.filoghost.holographicdisplays.bridge.protocollib.current.packet.EntityRelatedPacketWrapper;
import com.gmail.filoghost.holographicdisplays.bridge.protocollib.current.packet.WrapperPlayServerAttachEntity;
import com.gmail.filoghost.holographicdisplays.bridge.protocollib.current.packet.WrapperPlayServerEntityDestroy;
import com.gmail.filoghost.holographicdisplays.bridge.protocollib.current.packet.WrapperPlayServerEntityMetadata;
import com.gmail.filoghost.holographicdisplays.bridge.protocollib.current.packet.WrapperPlayServerMount;
import com.gmail.filoghost.holographicdisplays.bridge.protocollib.current.packet.WrapperPlayServerSpawnEntity;
import com.gmail.filoghost.holographicdisplays.bridge.protocollib.current.packet.WrapperPlayServerSpawnEntityLiving;
import com.gmail.filoghost.holographicdisplays.bridge.protocollib.current.packet.WrapperPlayServerSpawnEntity.ObjectTypes;
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
	
	private Serializer
		itemSerializer,
		intSerializer,
		byteSerializer,
		stringSerializer,
		booleanSerializer,
		chatComponentSerializer;
	
	private int itemstackMetadataWatcherIndex;
	private int customNameWatcherIndex;
	
	private boolean useGetEntityWorkaround;
	
	@Override
	public boolean hook(Plugin plugin, NMSManager nmsManager) {		
		this.nmsManager = nmsManager;
		
		if (NMSVersion.isGreaterEqualThan(NMSVersion.v1_9_R1)) {
			if (NMSVersion.isGreaterEqualThan(NMSVersion.v1_10_R1)) {
				itemstackMetadataWatcherIndex = 6;
			} else {
				itemstackMetadataWatcherIndex = 5;
			}
		} else {
			itemstackMetadataWatcherIndex = 10;
		}
		
		customNameWatcherIndex = 2;
		
		if (NMSVersion.isGreaterEqualThan(NMSVersion.v1_9_R1)) {
			itemSerializer = Registry.get(MinecraftReflection.getItemStackClass());
			intSerializer = Registry.get(Integer.class);
			byteSerializer = Registry.get(Byte.class);
			stringSerializer = Registry.get(String.class);
			booleanSerializer = Registry.get(Boolean.class);
		}
		
		if (NMSVersion.isGreaterEqualThan(NMSVersion.v1_13_R1)) {
			chatComponentSerializer = Registry.get(MinecraftReflection.getIChatBaseComponentClass(), true);
		}

		AdapterParameteters params = PacketAdapter
				.params()
				.plugin(plugin)
				.types(	PacketType.Play.Server.SPAWN_ENTITY_LIVING,
						PacketType.Play.Server.SPAWN_ENTITY,
						PacketType.Play.Server.ENTITY_METADATA)
				.serverSide()
				.listenerPriority(ListenerPriority.NORMAL);
		
		ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(params) {
					  
				@Override
				public void onPacketSending(PacketEvent event) {
					
					PacketContainer packet = event.getPacket();
					Player player = event.getPlayer();
					
					if (player instanceof Factory) {
						return; // Ignore temporary players (reference: https://github.com/dmulloy2/ProtocolLib/issues/349)
					}

					// Spawn entity packet
					if (packet.getType() == PacketType.Play.Server.SPAWN_ENTITY_LIVING) {

						WrapperPlayServerSpawnEntityLiving spawnEntityPacket = new WrapperPlayServerSpawnEntityLiving(packet);
						Entity entity = getEntity(event, spawnEntityPacket);
						
						CraftHologramLine hologramLine = getHologramLine(entity);
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
						
						spawnEntityPacket = new WrapperPlayServerSpawnEntityLiving(packet.deepClone());
						WrappedWatchableObject customNameWatchableObject = spawnEntityPacket.getMetadata().getWatchableObject(customNameWatcherIndex);
						replaceRelativePlaceholders(customNameWatchableObject, player, hologramLine.getRelativePlaceholders());
						event.setPacket(spawnEntityPacket.getHandle());

					} else if (packet.getType() == PacketType.Play.Server.SPAWN_ENTITY) {

						WrapperPlayServerSpawnEntity spawnEntityPacket = new WrapperPlayServerSpawnEntity(packet);
						Entity entity = getEntity(event, spawnEntityPacket);
						
						CraftHologramLine hologramLine = getHologramLine(entity);
						if (hologramLine == null) {
							return;
						}
						
						if (!hologramLine.getParent().getVisibilityManager().isVisibleTo(player)) {
							event.setCancelled(true);
							return;
						}
					
					} else if (packet.getType() == PacketType.Play.Server.ENTITY_METADATA) {

						WrapperPlayServerEntityMetadata entityMetadataPacket = new WrapperPlayServerEntityMetadata(packet);
						Entity entity = getEntity(event, entityMetadataPacket);
						
						CraftHologramLine hologramLine = getHologramLine(entity);
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
						List<WrappedWatchableObject> dataWatcherValues = entityMetadataPacket.getEntityMetadata();
						
						for (int i = 0; i < dataWatcherValues.size(); i++) {
							WrappedWatchableObject watchableObject = dataWatcherValues.get(i);
							
							if (watchableObject.getIndex() == customNameWatcherIndex) {
								if (replaceRelativePlaceholders(watchableObject, player, hologramLine.getRelativePlaceholders())) {
									event.setPacket(entityMetadataPacket.getHandle());
								}
								
								// No reason to check further.
								return;
							}
						}
					}
				}
			});
		
		return true;
	}
	
	
	private Entity getEntity(PacketEvent packetEvent, EntityRelatedPacketWrapper packetWrapper) {
		if (!useGetEntityWorkaround) {
			try {
				return packetWrapper.getEntity(packetEvent);
			} catch (RuntimeException e) {
				useGetEntityWorkaround = true;
			}
		}
		
		// Use workaround, get entity from its ID through NMS
		return HolographicDisplays.getNMSManager().getEntityFromID(packetEvent.getPlayer().getWorld(), packetWrapper.getEntityID());
	}
	
	
	private boolean replaceRelativePlaceholders(WrappedWatchableObject customNameWatchableObject, Player player, Collection<RelativePlaceholder> relativePlaceholders) {
		if (customNameWatchableObject == null) {
			return true;
		}
		
		Object customNameWatchableObjectValue = customNameWatchableObject.getValue();
		String customName;
		
		if (NMSVersion.isGreaterEqualThan(NMSVersion.v1_13_R1)) {
			if (!(customNameWatchableObjectValue instanceof Optional)) {
				return false;
			}
			
			Optional<?> customNameOptional = (Optional<?>) customNameWatchableObjectValue;
			if (!customNameOptional.isPresent()) {
				return false;
			}
			
			WrappedChatComponent componentWrapper = WrappedChatComponent.fromHandle(customNameOptional.get());
			customName = componentWrapper.getJson();
			
		} else {
			if (!(customNameWatchableObjectValue instanceof String)) {
				return false;
			}
			
			customName = (String) customNameWatchableObjectValue;
		}
		
		for (RelativePlaceholder relativePlaceholder : relativePlaceholders) {
			customName = customName.replace(relativePlaceholder.getTextPlaceholder(), relativePlaceholder.getReplacement(player));
		}
			
		if (NMSVersion.isGreaterEqualThan(NMSVersion.v1_13_R1)) {
			customNameWatchableObject.setValue(Optional.of(WrappedChatComponent.fromJson(customName).getHandle()));
		} else {
			customNameWatchableObject.setValue(customName);
		}
		
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
			WrapperPlayServerEntityDestroy packet = new WrapperPlayServerEntityDestroy();
			packet.setEntities(ids);
			packet.sendPacket(player);
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
			WrapperPlayServerEntityDestroy packet = new WrapperPlayServerEntityDestroy();
			packet.setEntities(ids);
			packet.sendPacket(player);
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
		
		if (line instanceof CraftTextLine) {
			CraftTextLine textLine = (CraftTextLine) line;
			
			if (textLine.isSpawned()) {
				sendSpawnArmorStandPacket(player, (NMSArmorStand) textLine.getNmsNameable());
			}
			
		} else if (line instanceof CraftItemLine) {
			CraftItemLine itemLine = (CraftItemLine) line;
			
			if (itemLine.isSpawned()) {
				AbstractPacket itemPacket = new WrapperPlayServerSpawnEntity(itemLine.getNmsItem().getBukkitEntityNMS(), ObjectTypes.ITEM_STACK, 1);
				itemPacket.sendPacket(player);
				
				sendSpawnArmorStandPacket(player, (NMSArmorStand) itemLine.getNmsVehicle());
				sendVehicleAttachPacket(player, itemLine.getNmsVehicle().getIdNMS(), itemLine.getNmsItem().getIdNMS());
				
				WrapperPlayServerEntityMetadata itemDataPacket = new WrapperPlayServerEntityMetadata();
				WrappedDataWatcher dataWatcher = new WrappedDataWatcher();
				
				if (NMSVersion.isGreaterEqualThan(NMSVersion.v1_9_R1)) {
					Object itemStackObject = NMSVersion.isGreaterEqualThan(NMSVersion.v1_11_R1) ? itemLine.getNmsItem().getRawItemStack() : com.google.common.base.Optional.of(itemLine.getNmsItem().getRawItemStack());
					dataWatcher.setObject(new WrappedDataWatcherObject(itemstackMetadataWatcherIndex, itemSerializer), itemStackObject);
					dataWatcher.setObject(new WrappedDataWatcherObject(1, intSerializer), 300);
					dataWatcher.setObject(new WrappedDataWatcherObject(0, byteSerializer), (byte) 0);
				} else {
					dataWatcher.setObject(itemstackMetadataWatcherIndex, itemLine.getNmsItem().getRawItemStack());
					dataWatcher.setObject(1, 300);
					dataWatcher.setObject(0, (byte) 0);
				}

				itemDataPacket.setEntityMetadata(dataWatcher.getWatchableObjects());
				itemDataPacket.setEntityID(itemLine.getNmsItem().getIdNMS());
				itemDataPacket.sendPacket(player);
			}
		}
		
		// Unsafe cast, however both CraftTextLine and CraftItemLine are touchable.
		CraftTouchableLine touchableLine = (CraftTouchableLine) line;
		
		if (touchableLine.isSpawned() && touchableLine.getTouchSlime() != null) {
			CraftTouchSlimeLine touchSlime = touchableLine.getTouchSlime();
			
			if (touchSlime.isSpawned()) {
				sendSpawnArmorStandPacket(player, (NMSArmorStand) touchSlime.getNmsVehicle());

				AbstractPacket slimePacket = new WrapperPlayServerSpawnEntityLiving(touchSlime.getNmsSlime().getBukkitEntityNMS());
				slimePacket.sendPacket(player);
				
				sendVehicleAttachPacket(player, touchSlime.getNmsVehicle().getIdNMS(), touchSlime.getNmsSlime().getIdNMS());
			}
		}
	}
	
	
	private void sendSpawnArmorStandPacket(Player receiver, NMSArmorStand armorStand) {
        WrapperPlayServerSpawnEntityLiving spawnPacket = new WrapperPlayServerSpawnEntityLiving(armorStand.getBukkitEntityNMS());
        spawnPacket.sendPacket(receiver);

		if (NMSVersion.isGreaterEqualThan(NMSVersion.v1_11_R1)) {
			WrapperPlayServerEntityMetadata dataPacket = new WrapperPlayServerEntityMetadata();
			WrappedDataWatcher dataWatcher = new WrappedDataWatcher();
			
			dataWatcher.setObject(new WrappedDataWatcherObject(0, byteSerializer), (byte) 0x20); // Entity status: invisible

			String customName = armorStand.getCustomNameNMS();
			if (customName != null && !customName.isEmpty()) {
				if (NMSVersion.isGreaterEqualThan(NMSVersion.v1_13_R1)) {
					dataWatcher.setObject(new WrappedDataWatcherObject(customNameWatcherIndex, chatComponentSerializer), Optional.of(WrappedChatComponent.fromText(customName).getHandle()));
				} else {
					dataWatcher.setObject(new WrappedDataWatcherObject(customNameWatcherIndex, stringSerializer), customName);
				}
				dataWatcher.setObject(new WrappedDataWatcherObject(3, booleanSerializer), true); // Custom name visible
			}

			dataWatcher.setObject(new WrappedDataWatcherObject(5, booleanSerializer), true); // No gravity
			dataWatcher.setObject(new WrappedDataWatcherObject(11, byteSerializer), (byte) (0x01 | 0x08 | 0x10)); // Armor stand data: small, no base plate, marker
			
			dataPacket.setEntityMetadata(dataWatcher.getWatchableObjects());
			dataPacket.setEntityID(armorStand.getIdNMS());
			dataPacket.sendPacket(receiver);
			
		}
	}
	
	
	private void sendVehicleAttachPacket(Player receiver, int vehicleId, int passengerId) {
		if (NMSVersion.isGreaterEqualThan(NMSVersion.v1_9_R1)) {
			WrapperPlayServerMount attachPacket = new WrapperPlayServerMount();
			attachPacket.setVehicleId(vehicleId);
			attachPacket.setPassengers(new int[] {passengerId});
			attachPacket.sendPacket(receiver);
		} else {
			WrapperPlayServerAttachEntity attachPacket = new WrapperPlayServerAttachEntity();
			attachPacket.setVehicleId(vehicleId);
			attachPacket.setEntityId(passengerId);
			attachPacket.sendPacket(receiver);
		}
	}
	
	
	private CraftHologramLine getHologramLine(Entity bukkitEntity) {
		if (bukkitEntity != null && isHologramType(bukkitEntity.getType())) {		
			NMSEntityBase entity = nmsManager.getNMSEntityBase(bukkitEntity);
			if (entity != null) {
				return (CraftHologramLine) entity.getHologramLine();
			}
		}
		
		return null;
	}
	
	
	private boolean isHologramType(EntityType type) {
		return type == EntityType.ARMOR_STAND || type == EntityType.DROPPED_ITEM || type == EntityType.SLIME;
	}
}
