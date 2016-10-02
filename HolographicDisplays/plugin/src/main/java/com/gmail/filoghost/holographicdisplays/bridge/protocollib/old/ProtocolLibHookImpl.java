package com.gmail.filoghost.holographicdisplays.bridge.protocollib.old;

import java.lang.reflect.Constructor;
import java.util.List;

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
import com.gmail.filoghost.holographicdisplays.bridge.protocollib.ProtocolLibHook;
import com.gmail.filoghost.holographicdisplays.bridge.protocollib.old.WrapperPlayServerSpawnEntity.ObjectTypes;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.NMSManager;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.entity.NMSEntityBase;
import com.gmail.filoghost.holographicdisplays.object.CraftHologram;
import com.gmail.filoghost.holographicdisplays.object.line.CraftHologramLine;
import com.gmail.filoghost.holographicdisplays.object.line.CraftItemLine;
import com.gmail.filoghost.holographicdisplays.object.line.CraftTextLine;
import com.gmail.filoghost.holographicdisplays.object.line.CraftTouchSlimeLine;
import com.gmail.filoghost.holographicdisplays.object.line.CraftTouchableLine;
import com.gmail.filoghost.holographicdisplays.util.MinecraftVersion;
import com.gmail.filoghost.holographicdisplays.util.Utils;
import com.gmail.filoghost.holographicdisplays.util.VersionUtils;

/**
 * This is for the ProtocolLib versions without the WrappedDataWatcher.WrappedDataWatcherObject class.
 * 
 * These versions are only used for 1.7 and 1.8.
 */
public class ProtocolLibHookImpl implements ProtocolLibHook {

	private NMSManager nmsManager;

	private int customNameWatcherIndex;

	@Override
	public boolean hook(Plugin plugin, NMSManager nmsManager) {
		this.nmsManager = nmsManager;

		if (MinecraftVersion.isGreaterEqualThan(MinecraftVersion.v1_8)) {
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
							WrappedWatchableObject dataWatcherValue = dataWatcherValues.get(i);

							if (dataWatcherValue.getIndex() == customNameWatcherIndex && dataWatcherValue.getValue() != null) {

								Object customNameObject = dataWatcherValue.getValue();
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

	public void sendCreateEntitiesPacket(Player player, CraftHologram hologram) {
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
						if (MinecraftVersion.isGreaterEqualThan(MinecraftVersion.v1_8)) {
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
						metadata.add(createWrappedWatchableObject(10, itemLine.getItemStack()));
						metadata.add(createWrappedWatchableObject(1, (short) 300));
						metadata.add(createWrappedWatchableObject(0, (byte) 0));
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

						if (MinecraftVersion.isGreaterEqualThan(MinecraftVersion.v1_8)) {
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
	
	// This is just for compiling
	private Constructor<?> wrappedWatchableObjectConstructor;
	private WrappedWatchableObject createWrappedWatchableObject(int index, Object value) {
		try {
			if (wrappedWatchableObjectConstructor == null) {
				wrappedWatchableObjectConstructor = WrappedWatchableObject.class.getConstructor(int.class, Object.class);
			}
			
			return (WrappedWatchableObject) wrappedWatchableObjectConstructor.newInstance(index, value);
		} catch (Exception ex) {
			throw new IllegalStateException("Could not invoke WrappedWatchableObject constructor", ex);
		}
	}

	private boolean isHologramType(EntityType type) {
		return type == EntityType.HORSE || type == EntityType.WITHER_SKULL || type == EntityType.DROPPED_ITEM || type == EntityType.SLIME || VersionUtils.isArmorstand(type); // To maintain backwards compatibility
	}

	private CraftHologram getHologram(Entity bukkitEntity) {
		NMSEntityBase entity = nmsManager.getNMSEntityBase(bukkitEntity);
		if (entity != null) {
			return entity.getHologramLine().getParent();
		}

		return null;
	}
}
