package com.gmail.filoghost.holograms.protocol;

import static com.gmail.filoghost.holograms.HolographicDisplays.nmsManager;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedWatchableObject;
import com.gmail.filoghost.holograms.HolographicDisplays;
import com.gmail.filoghost.holograms.object.HologramBase;
import com.gmail.filoghost.holograms.utils.VersionUtils;

public class ProtocolLibHook {
	
	private static int customNameWatcherIndex;
	
	public static void initialize() {
		if (Bukkit.getPluginManager().isPluginEnabled("ProtocolLib")) {
			
			HolographicDisplays.getInstance().getLogger().info("Found ProtocolLib, adding support for player relative variables.");
			if (HolographicDisplays.is1_8) {
				customNameWatcherIndex = 2;
			} else {
				customNameWatcherIndex = 10;
			}
			

			ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(HolographicDisplays.getInstance(), ListenerPriority.NORMAL, PacketType.Play.Server.SPAWN_ENTITY_LIVING, PacketType.Play.Server.SPAWN_ENTITY, PacketType.Play.Server.ENTITY_METADATA) {
						  
					
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
							
							HologramBase hologramBase = getHologram(entity);
							if (hologramBase == null) {
								return;
							}
							
							Player player = event.getPlayer();
							if (hologramBase.hasVisibilityManager() && !hologramBase.getVisibilityManager().isVisibleTo(player)) {
								event.setCancelled(true);
								return;
							}
							
							if (entity.getType() != EntityType.HORSE && !VersionUtils.isArmorstand(entity.getType())) {
								// Enough, only horses and armor stands are used with custom names.
								return;
							}
							
							WrappedDataWatcher dataWatcher = spawnEntityPacket.getMetadata();
							String customName = dataWatcher.getString(customNameWatcherIndex);
								
							if (customName.contains("{player}") || customName.contains("{displayname}")) {

								WrappedDataWatcher dataWatcherClone = dataWatcher.deepClone();
								dataWatcherClone.setObject(customNameWatcherIndex, customName.replace("{player}", player.getName()).replace("{displayname}", player.getDisplayName()));
								spawnEntityPacket.setMetadata(dataWatcherClone);
								event.setPacket(spawnEntityPacket.getHandle());
									
							}

						} else if (packet.getType() == PacketType.Play.Server.SPAWN_ENTITY) {
							
							WrapperPlayServerSpawnEntity spawnEntityPacket = new WrapperPlayServerSpawnEntity(packet);
							if (spawnEntityPacket.getType() != WrapperPlayServerSpawnEntity.ObjectTypes.ITEM_STACK) {
								return;
							}
							
							Entity entity = spawnEntityPacket.getEntity(event);
							if (entity == null) {
								return;
							}
							
							HologramBase hologramBase = getHologram(entity);
							if (hologramBase == null) {
								return;
							}
							
							Player player = event.getPlayer();
							if (hologramBase.hasVisibilityManager() && !hologramBase.getVisibilityManager().isVisibleTo(player)) {
								event.setCancelled(true);
								return;
							}
						
						} else if (packet.getType() == PacketType.Play.Server.ENTITY_METADATA) {
							
							WrapperPlayServerEntityMetadata entityMetadataPacket = new WrapperPlayServerEntityMetadata(packet);
							Entity entity = entityMetadataPacket.getEntity(event);
							
							if (entity == null || !isHologramType(entity.getType())) {
								return;
							}
							
							HologramBase hologramBase = getHologram(entity);
							if (hologramBase == null) {
								return;
							}
							
							Player player = event.getPlayer();
							if (hologramBase.hasVisibilityManager() && !hologramBase.getVisibilityManager().isVisibleTo(player)) {
								event.setCancelled(true);
								return;
							}
							
							if (entity.getType() != EntityType.HORSE && !VersionUtils.isArmorstand(entity.getType())) {
								// Enough, only horses and armorstands are used with custom names.
								return;
							}

							List<WrappedWatchableObject> dataWatcherValues = entityMetadataPacket.getEntityMetadata();
								
							for (int i = 0; i < dataWatcherValues.size(); i++) {	
								
								if (dataWatcherValues.get(i).getIndex() == customNameWatcherIndex) {
										
									Object customNameObject = dataWatcherValues.get(i).deepClone().getValue();
									if (customNameObject instanceof String == false) {
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
		}
	}
	
	private static boolean isHologramType(EntityType type) {
		return type == EntityType.HORSE || type == EntityType.WITHER_SKULL || type == EntityType.DROPPED_ITEM || type == EntityType.SLIME || VersionUtils.isArmorstand(type); // To maintain backwards compatibility
	}
	
	private static HologramBase getHologram(Entity bukkitEntity) {
		return nmsManager.getParentHologram(bukkitEntity);
	}
}
