package com.gmail.filoghost.holographicdisplays.bridge.protocollib.pre1_9;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedWatchableObject;
import com.gmail.filoghost.holographicdisplays.HolographicDisplays;
import com.gmail.filoghost.holographicdisplays.bridge.protocollib.pre1_9.WrapperPlayServerSpawnEntity.ObjectTypes;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.NMSManager;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.entity.NMSEntityBase;
import com.gmail.filoghost.holographicdisplays.object.CraftHologram;
import com.gmail.filoghost.holographicdisplays.object.line.CraftHologramLine;
import com.gmail.filoghost.holographicdisplays.object.line.CraftItemLine;
import com.gmail.filoghost.holographicdisplays.object.line.CraftTextLine;
import com.gmail.filoghost.holographicdisplays.object.line.CraftTouchSlimeLine;
import com.gmail.filoghost.holographicdisplays.object.line.CraftTouchableLine;
import com.gmail.filoghost.holographicdisplays.util.Utils;
import com.gmail.filoghost.holographicdisplays.util.VersionUtils;

public class ProtocolLibHook {
	
	private static boolean hasProtocolLib;
	private static NMSManager nmsManager;
	
	private static int customNameWatcherIndex;
	
	public static boolean load(NMSManager nmsManager, Plugin plugin, boolean is1_8) {
		ProtocolLibHook.nmsManager = nmsManager;
		
		if (Bukkit.getPluginManager().isPluginEnabled("ProtocolLib")) {
			
			hasProtocolLib = true;
						
			plugin.getLogger().info("Found ProtocolLib, adding support for player relative variables.");
			if (is1_8) {
				customNameWatcherIndex = 2;
			} else {
				customNameWatcherIndex = 10;
			}
			

			ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(plugin, ListenerPriority.NORMAL, PacketType.Play.Server.SPAWN_ENTITY_LIVING, PacketType.Play.Server.SPAWN_ENTITY, PacketType.Play.Server.ENTITY_METADATA) {
						  
					@Override
					public void onPacketSending(PacketEvent event) {
						
						PacketContainer packet = event.getPacket();

						// Spawn entity packet
						if (packet.getType() == PacketType.Play.Server.SPAWN_ENTITY_LIVING) {

							WrapperPlayServerSpawnEntityLiving spawnEntityPacket = new WrapperPlayServerSpawnEntityLiving(packet);
							Entity entity = spawnEntityPacket.getEntity(event);
							
							if (entity == null || !isHologramType(entity.getType())) {
								return;
							}
							
							CraftHologram hologram = getHologram(entity);
							if (hologram == null) {
								return;
							}
							
							Player player = event.getPlayer();
							if (!hologram.getVisibilityManager().isVisibleTo(player)) {
								event.setCancelled(true);
								return;
							}
							
							WrappedDataWatcher dataWatcher = spawnEntityPacket.getMetadata();
							String customName = dataWatcher.getString(customNameWatcherIndex);
							
							if (customName == null) {
								return;
							}
							
							if (customName.contains("{player}") || customName.contains("{displayname}")) {

								WrappedDataWatcher dataWatcherClone = dataWatcher.deepClone();
								dataWatcherClone.setObject(customNameWatcherIndex, customName.replace("{player}", player.getName()).replace("{displayname}", player.getDisplayName()));
								spawnEntityPacket.setMetadata(dataWatcherClone);
								event.setPacket(spawnEntityPacket.getHandle());
								
							}

						} else if (packet.getType() == PacketType.Play.Server.SPAWN_ENTITY) {
							
							WrapperPlayServerSpawnEntity spawnEntityPacket = new WrapperPlayServerSpawnEntity(packet);
							int objectId = spawnEntityPacket.getType();
							if (objectId != ObjectTypes.ITEM_STACK && objectId != ObjectTypes.WITHER_SKULL && objectId != ObjectTypes.ARMOR_STAND) {
								return;
							}
							
							Entity entity = spawnEntityPacket.getEntity(event);
							if (entity == null) {
								return;
							}
							
							CraftHologram hologram = getHologram(entity);
							if (hologram == null) {
								return;
							}
							
							Player player = event.getPlayer();
							if (!hologram.getVisibilityManager().isVisibleTo(player)) {
								event.setCancelled(true);
								return;
							}
						
						} else if (packet.getType() == PacketType.Play.Server.ENTITY_METADATA) {
							
							WrapperPlayServerEntityMetadata entityMetadataPacket = new WrapperPlayServerEntityMetadata(packet);
							Entity entity = entityMetadataPacket.getEntity(event);
							
							if (entity == null) {
								return;
							}

							if (entity.getType() != EntityType.HORSE && !VersionUtils.isArmorstand(entity.getType())) {
								// Enough, only horses and armorstands are used with custom names.
								return;
							}
							
							CraftHologram hologram = getHologram(entity);
							if (hologram == null) {
								return;
							}
							
							Player player = event.getPlayer();
							if (!hologram.getVisibilityManager().isVisibleTo(player)) {
								event.setCancelled(true);
								return;
							}

							List<WrappedWatchableObject> dataWatcherValues = entityMetadataPacket.getEntityMetadata();
								
							for (int i = 0; i < dataWatcherValues.size(); i++) {
								
								if (dataWatcherValues.get(i).getIndex() == customNameWatcherIndex && dataWatcherValues.get(i).getValue() != null) {
										
									Object customNameObject = dataWatcherValues.get(i).deepClone().getValue();
									if (customNameObject == null || customNameObject instanceof String == false) {
										return;
									}
									
									String customName = (String) customNameObject;
										
									if (customName.contains("{player}") || customName.contains("{displayname}")) {
										
										entityMetadataPacket = new WrapperPlayServerEntityMetadata(packet.deepClone());
										List<WrappedWatchableObject> clonedList = entityMetadataPacket.getEntityMetadata();
										WrappedWatchableObject clonedElement = clonedList.get(i);
										clonedElement.setValue(customName.replace("{player}", player.getName()).replace("{displayname}", player.getDisplayName()));
										entityMetadataPacket.setEntityMetadata(clonedList);
										event.setPacket(entityMetadataPacket.getHandle());
										return;
											
									}
								}
							}
						}
					}
				});
			
			return true;
		}
		
		return false;
	}
	
	public static void sendDestroyEntitiesPacket(Player player, CraftHologram hologram) {
		if (!hasProtocolLib) {
			return;
		}
		
		List<Integer> ids = Utils.newList();
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
	
	public static void sendCreateEntitiesPacket(Player player, CraftHologram hologram) {
		if (!hasProtocolLib) {
			return;
		}
		
		for (CraftHologramLine line : hologram.getLinesUnsafe()) {
			if (line.isSpawned()) {
				
				if (line instanceof CraftTextLine) {
					CraftTextLine textLine = (CraftTextLine) line;
					
					if (textLine.isSpawned()) {
						
						AbstractPacket nameablePacket = new WrapperPlayServerSpawnEntityLiving(textLine.getNmsNameble().getBukkitEntityNMS());
						nameablePacket.sendPacket(player);
						
						if (textLine.getNmsSkullVehicle() != null) {
							AbstractPacket vehiclePacket = new WrapperPlayServerSpawnEntity(textLine.getNmsSkullVehicle().getBukkitEntityNMS(), ObjectTypes.WITHER_SKULL, 0);
							vehiclePacket.sendPacket(player);
							
							WrapperPlayServerAttachEntity attachPacket = new WrapperPlayServerAttachEntity();
							attachPacket.setVehicleId(textLine.getNmsSkullVehicle().getIdNMS());
							attachPacket.setEntityId(textLine.getNmsNameble().getIdNMS());
							attachPacket.sendPacket(player);
						}
					}
					
				} else if (line instanceof CraftItemLine) {
					CraftItemLine itemLine = (CraftItemLine) line;
					
					if (itemLine.isSpawned()) {
						AbstractPacket itemPacket = new WrapperPlayServerSpawnEntity(itemLine.getNmsItem().getBukkitEntityNMS(), ObjectTypes.ITEM_STACK, 1);
						itemPacket.sendPacket(player);
						
						AbstractPacket vehiclePacket;
						if (HolographicDisplays.is18orGreater()) {
							// In 1.8 we have armor stands, that are living entities.
							vehiclePacket = new WrapperPlayServerSpawnEntityLiving(itemLine.getNmsVehicle().getBukkitEntityNMS());
						} else {
							vehiclePacket = new WrapperPlayServerSpawnEntity(itemLine.getNmsVehicle().getBukkitEntityNMS(), ObjectTypes.WITHER_SKULL, 0);
						}
						
						vehiclePacket.sendPacket(player);
							
						WrapperPlayServerAttachEntity attachPacket = new WrapperPlayServerAttachEntity();
						attachPacket.setVehicleId(itemLine.getNmsVehicle().getIdNMS());
						attachPacket.setEntityId(itemLine.getNmsItem().getIdNMS());
						attachPacket.sendPacket(player);
						
						WrapperPlayServerEntityMetadata itemDataPacket = new WrapperPlayServerEntityMetadata();
						
						List<WrappedWatchableObject> metadata = Utils.newList();
						metadata.add(new WrappedWatchableObject(10, itemLine.getItemStack()));
						metadata.add(new WrappedWatchableObject(1, (short) 300));
						metadata.add(new WrappedWatchableObject(0, (byte) 0));
						itemDataPacket.setEntityMetadata(metadata);
						itemDataPacket.setEntityId(itemLine.getNmsItem().getIdNMS());
						itemDataPacket.sendPacket(player);
					}
				}
				
				// Unsafe cast, however both CraftTextLine and CraftItemLine are touchable.
				CraftTouchableLine touchableLine = (CraftTouchableLine) line;
				
				if (touchableLine.isSpawned() && touchableLine.getTouchSlime() != null) {
						
					CraftTouchSlimeLine touchSlime = touchableLine.getTouchSlime();
					
					if (touchSlime.isSpawned()) {
						AbstractPacket vehiclePacket;
							
						if (HolographicDisplays.is18orGreater()) {
							// Armor stand vehicle
							vehiclePacket = new WrapperPlayServerSpawnEntityLiving(touchSlime.getNmsVehicle().getBukkitEntityNMS());
						} else {
							// Wither skull vehicle
							vehiclePacket = new WrapperPlayServerSpawnEntity(touchSlime.getNmsVehicle().getBukkitEntityNMS(), ObjectTypes.WITHER_SKULL, 0);
						}
						vehiclePacket.sendPacket(player);
							
						AbstractPacket slimePacket = new WrapperPlayServerSpawnEntityLiving(touchSlime.getNmsSlime().getBukkitEntityNMS());
						slimePacket.sendPacket(player);
							
						WrapperPlayServerAttachEntity attachPacket = new WrapperPlayServerAttachEntity();
						attachPacket.setVehicleId(touchSlime.getNmsVehicle().getIdNMS());
						attachPacket.setEntityId(touchSlime.getNmsSlime().getIdNMS());
						attachPacket.sendPacket(player);
					}
				}
			}
		}
	}

	private static boolean isHologramType(EntityType type) {
		return type == EntityType.HORSE || type == EntityType.WITHER_SKULL || type == EntityType.DROPPED_ITEM || type == EntityType.SLIME || VersionUtils.isArmorstand(type); // To maintain backwards compatibility
	}
	
	private static CraftHologram getHologram(Entity bukkitEntity) {
		NMSEntityBase entity = nmsManager.getNMSEntityBase(bukkitEntity);
		if (entity != null) {
			return entity.getHologramLine().getParent();
		}
		
		return null;
	}
}
