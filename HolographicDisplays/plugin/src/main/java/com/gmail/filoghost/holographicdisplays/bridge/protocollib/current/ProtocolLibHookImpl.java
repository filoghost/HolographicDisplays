package com.gmail.filoghost.holographicdisplays.bridge.protocollib.current;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketAdapter.AdapterParameteters;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedDataWatcher.Registry;
import com.comphenix.protocol.wrappers.WrappedDataWatcher.Serializer;
import com.comphenix.protocol.wrappers.WrappedDataWatcher.WrappedDataWatcherObject;
import com.comphenix.protocol.wrappers.WrappedWatchableObject;
import com.gmail.filoghost.holographicdisplays.bridge.protocollib.ProtocolLibHook;
import com.gmail.filoghost.holographicdisplays.bridge.protocollib.current.WrapperPlayServerSpawnEntity.ObjectTypes;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.NMSManager;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.entity.NMSArmorStand;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.entity.NMSEntityBase;
import com.gmail.filoghost.holographicdisplays.object.CraftHologram;
import com.gmail.filoghost.holographicdisplays.object.line.CraftHologramLine;
import com.gmail.filoghost.holographicdisplays.object.line.CraftItemLine;
import com.gmail.filoghost.holographicdisplays.object.line.CraftTextLine;
import com.gmail.filoghost.holographicdisplays.object.line.CraftTouchSlimeLine;
import com.gmail.filoghost.holographicdisplays.object.line.CraftTouchableLine;
import com.gmail.filoghost.holographicdisplays.util.MinecraftVersion;
import com.gmail.filoghost.holographicdisplays.util.Utils;
import com.google.common.base.Optional;

/**
 * This is for the ProtocolLib versions containing the WrappedDataWatcher.WrappedDataWatcherObject class.
 * 
 * These versions are only used from 1.8, there is no need to handle 1.7 entities.
 */
public class ProtocolLibHookImpl implements ProtocolLibHook {
	
	private NMSManager nmsManager;
	
	private Serializer
		itemSerializer,
		intSerializer,
		byteSerializer,
		stringSerializer,
		booleanSerializer;
	
	private int itemstackMetadataWatcherIndex;
	
	@Override
	public boolean hook(Plugin plugin, NMSManager nmsManager) {
		
		String version = Bukkit.getPluginManager().getPlugin("ProtocolLib").getDescription().getVersion();
		if (version.startsWith("3.7-SNAPSHOT")) {
			Bukkit.getConsoleSender().sendMessage(
					ChatColor.RED + "[Holographic Displays] Detected development version of ProtocolLib, support disabled. " +
					"Related functions (the placeholders {player} {displayname} and the visibility API) will not work.\n" +
					"The reason is that this version of ProtocolLib is unstable and partly broken. " +
					"Please update ProtocolLib.");
			return false;
		}
		
		this.nmsManager = nmsManager;
		
		if (MinecraftVersion.isGreaterEqualThan(MinecraftVersion.v1_9)) {
			if (MinecraftVersion.isGreaterEqualThan(MinecraftVersion.v1_10)) {
				itemstackMetadataWatcherIndex = 6;
			} else {
				itemstackMetadataWatcherIndex = 5;
			}
		} else {
			itemstackMetadataWatcherIndex = 10;
		}
		
		if (MinecraftVersion.isGreaterEqualThan(MinecraftVersion.v1_9)) {
			itemSerializer = Registry.get(MinecraftReflection.getItemStackClass());
			intSerializer = Registry.get(Integer.class);
			byteSerializer = Registry.get(Byte.class);
			stringSerializer = Registry.get(String.class);
			booleanSerializer = Registry.get(Boolean.class);
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
						
						WrappedWatchableObject customNameWatchableObject = spawnEntityPacket.getMetadata().getWatchableObject(2);
						if (customNameWatchableObject == null || !(customNameWatchableObject.getValue() instanceof String)) {
							return;
						}
						
						String customName = (String) customNameWatchableObject.getValue();
						if (customName.contains("{player}") || customName.contains("{displayname}")) {
							customNameWatchableObject.setValue(customName.replace("{player}", player.getName()).replace("{displayname}", player.getDisplayName()));
						}

					} else if (packet.getType() == PacketType.Play.Server.SPAWN_ENTITY) {

						WrapperPlayServerSpawnEntity spawnEntityPacket = new WrapperPlayServerSpawnEntity(packet);
						Entity entity = spawnEntityPacket.getEntity(event);
						
						if (entity == null) {
							return;
						}
						
						if (!isHologramType(entity.getType())) {
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

						if (!isHologramType(entity.getType())) {
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
							
							WrappedWatchableObject watchableObject = dataWatcherValues.get(i);
							if (watchableObject.getIndex() == 2) { // Custom name index
									
								Object customNameObject = watchableObject.getValue();
								if (!(customNameObject instanceof String)) {
									return;
								}
								
								String customName = (String) customNameObject;
								if (customName.contains("{player}") || customName.contains("{displayname}")) {
									String replacement = customName.replace("{player}", player.getName()).replace("{displayname}", player.getDisplayName());

									WrappedWatchableObject newWatchableObject;
									if (MinecraftVersion.isGreaterEqualThan(MinecraftVersion.v1_9)) {
										// The other constructor does not work in 1.9+.
										newWatchableObject = new WrappedWatchableObject(watchableObject.getWatcherObject(), replacement);
									} else {
										newWatchableObject = new WrappedWatchableObject(watchableObject.getIndex(), replacement);
									}
									
									dataWatcherValues.set(i, newWatchableObject);
									PacketContainer clone = packet.shallowClone();
									clone.getWatchableCollectionModifier().write(0, dataWatcherValues);
									event.setPacket(clone);
									return;
								}
							}
						}
					}
				}
			});
		
		return true;
	}
	
	
	@Override
	public void sendDestroyEntitiesPacket(Player player, CraftHologram hologram) {
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
	
	
	@Override
	public void sendCreateEntitiesPacket(Player player, CraftHologram hologram) {
		for (CraftHologramLine line : hologram.getLinesUnsafe()) {
			if (line.isSpawned()) {
				
				if (line instanceof CraftTextLine) {
					CraftTextLine textLine = (CraftTextLine) line;
					
					if (textLine.isSpawned()) {
						sendSpawnArmorStandPacket(player, (NMSArmorStand) textLine.getNmsNameble());
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
						
						if (MinecraftVersion.isGreaterEqualThan(MinecraftVersion.v1_9)) {
							Object itemStackObject = MinecraftVersion.isGreaterEqualThan(MinecraftVersion.v1_11) ? itemLine.getNmsItem().getRawItemStack() : Optional.of(itemLine.getNmsItem().getRawItemStack());
							dataWatcher.setObject(new WrappedDataWatcherObject(itemstackMetadataWatcherIndex, itemSerializer), itemStackObject);
							dataWatcher.setObject(new WrappedDataWatcherObject(1, intSerializer), 300);
							dataWatcher.setObject(new WrappedDataWatcherObject(0, byteSerializer), (byte) 0);
						} else {
							dataWatcher.setObject(itemstackMetadataWatcherIndex, itemLine.getNmsItem().getRawItemStack());
							dataWatcher.setObject(1, 300);
							dataWatcher.setObject(0, (byte) 0);
						}

						itemDataPacket.setEntityMetadata(dataWatcher.getWatchableObjects());
						itemDataPacket.setEntityId(itemLine.getNmsItem().getIdNMS());
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
		}
	}
	
	
	private void sendSpawnArmorStandPacket(Player receiver, NMSArmorStand armorStand) {
		if (MinecraftVersion.isGreaterEqualThan(MinecraftVersion.v1_11)) {
			WrapperPlayServerSpawnEntity spawnPacket = new WrapperPlayServerSpawnEntity(armorStand.getBukkitEntityNMS(), WrapperPlayServerSpawnEntity.ObjectTypes.ARMOR_STAND, 1);
			spawnPacket.sendPacket(receiver);
			
			WrapperPlayServerEntityMetadata dataPacket = new WrapperPlayServerEntityMetadata();
			WrappedDataWatcher dataWatcher = new WrappedDataWatcher();
			
			dataWatcher.setObject(new WrappedDataWatcherObject(0, byteSerializer), (byte) 0x20); // Entity status

			String customName = armorStand.getCustomNameNMS();
			if (customName != null && !customName.isEmpty()) {
				dataWatcher.setObject(new WrappedDataWatcherObject(2, stringSerializer), customName); // Custom name
				dataWatcher.setObject(new WrappedDataWatcherObject(3, booleanSerializer), true); // Custom name visible
			}

			dataWatcher.setObject(new WrappedDataWatcherObject(5, booleanSerializer), true); // No gravity
			dataWatcher.setObject(new WrappedDataWatcherObject(11, byteSerializer), (byte) (0x01 | 0x08 | 0x10)); // Armor stand data: small, no base plate, marker
			
			dataPacket.setEntityMetadata(dataWatcher.getWatchableObjects());
			dataPacket.setEntityId(armorStand.getIdNMS());
			dataPacket.sendPacket(receiver);
			
		} else {
			WrapperPlayServerSpawnEntityLiving spawnPacket = new WrapperPlayServerSpawnEntityLiving(armorStand.getBukkitEntityNMS());
			spawnPacket.sendPacket(receiver);
		}
	}
	
	
	private void sendVehicleAttachPacket(Player receiver, int vehicleId, int passengerId) {
		if (MinecraftVersion.isGreaterEqualThan(MinecraftVersion.v1_9)) {
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

	
	private boolean isHologramType(EntityType type) {
		return type == EntityType.ARMOR_STAND || type == EntityType.DROPPED_ITEM || type == EntityType.SLIME;
	}
	
	
	private CraftHologram getHologram(Entity bukkitEntity) {
		NMSEntityBase entity = nmsManager.getNMSEntityBase(bukkitEntity);
		if (entity != null) {
			return entity.getHologramLine().getParent();
		}
		
		return null;
	}
}
