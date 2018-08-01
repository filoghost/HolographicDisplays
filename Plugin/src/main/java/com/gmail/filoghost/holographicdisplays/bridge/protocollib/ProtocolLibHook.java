package com.gmail.filoghost.holographicdisplays.bridge.protocollib;

import com.comphenix.packetwrapper.*;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketAdapter.AdapterParameteters;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.ComponentConverter;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedDataWatcher.Registry;
import com.comphenix.protocol.wrappers.WrappedDataWatcher.Serializer;
import com.comphenix.protocol.wrappers.WrappedDataWatcher.WrappedDataWatcherObject;
import com.comphenix.protocol.wrappers.WrappedWatchableObject;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.NMSManager;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.entity.NMSArmorStand;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.entity.NMSEntityBase;
import com.gmail.filoghost.holographicdisplays.object.CraftHologram;
import com.gmail.filoghost.holographicdisplays.object.line.*;
import com.gmail.filoghost.holographicdisplays.util.Utils;
import com.gmail.filoghost.holographicdisplays.util.bukkit.BukkitVersion;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.Optional;

/**
 * This is for the ProtocolLib versions containing the WrappedDataWatcher.WrappedDataWatcherObject class.
 */
public class ProtocolLibHook {

	private NMSManager nmsManager;

	private Serializer
			itemSerializer,
			intSerializer,
			byteSerializer,
			stringSerializer,
			booleanSerializer;

	private int itemstackMetadataWatcherIndex;

	private boolean checkHologram(PacketEvent event, Entity entity) {
		if (entity == null || !isHologramType(entity.getType())) {
			return false;
		}
		Hologram hologram = getHologram(entity);
		if (hologram == null) {
			return false;
		}
		Player player = event.getPlayer();
		if (!hologram.getVisibilityManager().isVisibleTo(player)) {
			event.setCancelled(true);
			return false;
		}
		return true;
	}

	private boolean handleHologramText(PacketEvent event, WrappedWatchableObject wrappedObject) {
		String message;
		Object object = wrappedObject.getValue();
		if (BukkitVersion.isAtLeast(BukkitVersion.v1_13_R1)) {
			if (!(object instanceof WrappedChatComponent)) {
				return false;
			}
			WrappedChatComponent componentWrapper = (WrappedChatComponent) object;
			message = BaseComponent.toLegacyText(ComponentConverter.fromWrapper(componentWrapper));
		} else {
			if (!(object instanceof String)) {
				return false;
			}
			message = (String) object;
		}
		if (!message.contains("{player}") && !message.contains("{displayname}")) {
			return false;
		}
		message = message.replace("{player}", event.getPlayer().getName()).replace("{displayname}", event.getPlayer().getDisplayName());
		wrappedObject.setValue(BukkitVersion.isAtLeast(BukkitVersion.v1_13_R1) ?
				ComponentConverter.fromBaseComponent(TextComponent.fromLegacyText(message)) : message);
		return true;
	}

	public boolean hook(Plugin plugin, NMSManager nmsManager) {
		this.nmsManager = nmsManager;

		if (BukkitVersion.isAtLeast(BukkitVersion.v1_9_R1)) {
			if (BukkitVersion.isAtLeast(BukkitVersion.v1_10_R1)) {
				itemstackMetadataWatcherIndex = 6;
			} else {
				itemstackMetadataWatcherIndex = 5;
			}
		} else {
			itemstackMetadataWatcherIndex = 10;
		}

		if (BukkitVersion.isAtLeast(BukkitVersion.v1_9_R1)) {
			itemSerializer = Registry.get(MinecraftReflection.getItemStackClass());
			intSerializer = Registry.get(Integer.class);
			byteSerializer = Registry.get(Byte.class);
			stringSerializer = Registry.get(String.class);
			booleanSerializer = Registry.get(Boolean.class);
		}

		AdapterParameteters params = PacketAdapter
				.params()
				.plugin(plugin)
				.types(PacketType.Play.Server.SPAWN_ENTITY_LIVING,
						PacketType.Play.Server.SPAWN_ENTITY,
						PacketType.Play.Server.ENTITY_METADATA)
				.serverSide()
				.listenerPriority(ListenerPriority.NORMAL);

		ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(params) {

			@Override
			public void onPacketSending(PacketEvent event) {
				PacketContainer packet = event.getPacket();

				// See https://github.com/dmulloy2/ProtocolLib/issues/349
				if (event.isPlayerTemporary()) {
					return;
				}

				// Spawn entity packet
				if (packet.getType() == PacketType.Play.Server.SPAWN_ENTITY_LIVING) {
					WrapperPlayServerSpawnEntityLiving spawnEntityPacket = new WrapperPlayServerSpawnEntityLiving(packet);
					Entity entity = spawnEntityPacket.getEntity(event);

					if (!checkHologram(event, entity)) {
						return;
					}

					WrappedWatchableObject customNameWatchableObject = spawnEntityPacket.getMetadata().getWatchableObject(2);
					if (customNameWatchableObject == null) {
						return; // Should never happen!
					}

					handleHologramText(event, customNameWatchableObject);
				} else if (packet.getType() == PacketType.Play.Server.SPAWN_ENTITY) {
					WrapperPlayServerSpawnEntity spawnEntityPacket = new WrapperPlayServerSpawnEntity(packet);
					Entity entity = spawnEntityPacket.getEntity(event);

					checkHologram(event, entity);
				} else if (packet.getType() == PacketType.Play.Server.ENTITY_METADATA) {
					WrapperPlayServerEntityMetadata entityMetadataPacket = new WrapperPlayServerEntityMetadata(packet);
					Entity entity = entityMetadataPacket.getEntity(event);

					if (!checkHologram(event, entity)) {
						return;
					}

					List<WrappedWatchableObject> dataWatcherValues = entityMetadataPacket.getMetadata();
					WrappedWatchableObject customNameWatchableObject = dataWatcherValues.get(2);
					if (customNameWatchableObject == null) {
						return; // Should never happen!
					}

					if (handleHologramText(event, customNameWatchableObject)) {
						entityMetadataPacket.setMetadata(dataWatcherValues);
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
			int[] entityArray = new int[ids.size()];
			for (int i = 0; i < ids.size(); i++) {
				entityArray[i] = ids.get(i);
			}
			packet.setEntityIds(entityArray);
			packet.sendPacket(player);
		}
	}

	public void sendCreateEntitiesPacket(Player player, CraftHologram hologram) {
		for (CraftHologramLine line : hologram.getLinesUnsafe()) {
			if (line.isSpawned()) {
				if (line instanceof CraftTextLine) {
					CraftTextLine textLine = (CraftTextLine) line;
					sendSpawnArmorStandPacket(player, (NMSArmorStand) textLine.getNmsNameble());
				} else if (line instanceof CraftItemLine) {
					CraftItemLine itemLine = (CraftItemLine) line;

					AbstractPacket itemPacket = new WrapperPlayServerSpawnEntity(itemLine.getNmsItem().getBukkitEntityNMS(), WrapperPlayServerSpawnEntity.ObjectTypes.ITEM_STACK, 1);
					itemPacket.sendPacket(player);

					sendSpawnArmorStandPacket(player, (NMSArmorStand) itemLine.getNmsVehicle());
					sendVehicleAttachPacket(player, itemLine.getNmsVehicle().getIdNMS(), itemLine.getNmsItem().getIdNMS());

					WrapperPlayServerEntityMetadata itemDataPacket = new WrapperPlayServerEntityMetadata();
					WrappedDataWatcher dataWatcher = new WrappedDataWatcher();

					if (BukkitVersion.isAtLeast(BukkitVersion.v1_9_R1)) {
						Object itemStackObject = BukkitVersion.isAtLeast(BukkitVersion.v1_11_R1) ? itemLine.getNmsItem().getRawItemStack() : Optional.of(itemLine.getNmsItem().getRawItemStack());
						dataWatcher.setObject(new WrappedDataWatcherObject(itemstackMetadataWatcherIndex, itemSerializer), itemStackObject);
						dataWatcher.setObject(new WrappedDataWatcherObject(1, intSerializer), 300);
						dataWatcher.setObject(new WrappedDataWatcherObject(0, byteSerializer), (byte) 0);
					} else {
						dataWatcher.setObject(itemstackMetadataWatcherIndex, itemLine.getNmsItem().getRawItemStack());
						dataWatcher.setObject(1, 300);
						dataWatcher.setObject(0, (byte) 0);
					}

					itemDataPacket.setMetadata(dataWatcher.getWatchableObjects());
					itemDataPacket.setEntityID(itemLine.getNmsItem().getIdNMS());
					itemDataPacket.sendPacket(player);
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
		if (BukkitVersion.isAtLeast(BukkitVersion.v1_11_R1)) {
			WrapperPlayServerSpawnEntity spawnPacket = new WrapperPlayServerSpawnEntity(armorStand.getBukkitEntityNMS(), WrapperPlayServerSpawnEntity.ObjectTypes.ARMORSTAND, 1);
			spawnPacket.sendPacket(receiver);

			WrapperPlayServerEntityMetadata dataPacket = new WrapperPlayServerEntityMetadata();
			WrappedDataWatcher dataWatcher = new WrappedDataWatcher();

			dataWatcher.setObject(new WrappedDataWatcherObject(0, byteSerializer), (byte) 0x20); // Entity status

			String customName = armorStand.getCustomNameNMS();
			if (customName != null && !customName.isEmpty()) {
				// Custom name
				if (BukkitVersion.isAtLeast(BukkitVersion.v1_13_R1)) {
					dataWatcher.setObject(2, ComponentConverter.fromBaseComponent(TextComponent.fromLegacyText(customName)));
				} else {
					dataWatcher.setObject(new WrappedDataWatcherObject(2, stringSerializer), customName);
				}
				dataWatcher.setObject(new WrappedDataWatcherObject(3, booleanSerializer), true); // Custom name visible
			}

			dataWatcher.setObject(new WrappedDataWatcherObject(5, booleanSerializer), true); // No gravity
			dataWatcher.setObject(new WrappedDataWatcherObject(11, byteSerializer), (byte) (0x01 | 0x08 | 0x10)); // Armor stand data: small, no base plate, marker

			dataPacket.setMetadata(dataWatcher.getWatchableObjects());
			dataPacket.setEntityID(armorStand.getIdNMS());
			dataPacket.sendPacket(receiver);
		} else {
			WrapperPlayServerSpawnEntityLiving spawnPacket = new WrapperPlayServerSpawnEntityLiving(armorStand.getBukkitEntityNMS());
			spawnPacket.sendPacket(receiver);
		}
	}

	private void sendVehicleAttachPacket(Player receiver, int vehicleId, int passengerId) {
		if (BukkitVersion.isAtLeast(BukkitVersion.v1_9_R1)) {
			WrapperPlayServerMount attachPacket = new WrapperPlayServerMount();
			attachPacket.setEntityID(vehicleId);
			attachPacket.setPassengerIds(new int[]{passengerId});
			attachPacket.sendPacket(receiver);
		} else {
			WrapperPlayServerAttachEntity attachPacket = new WrapperPlayServerAttachEntity();
			attachPacket.setVehicleId(vehicleId);
			attachPacket.setEntityID(passengerId);
			attachPacket.sendPacket(receiver);
		}
	}

	private boolean isHologramType(EntityType type) {
		return type == EntityType.ARMOR_STAND || type == EntityType.DROPPED_ITEM || type == EntityType.SLIME;
	}

	private Hologram getHologram(Entity bukkitEntity) {
		NMSEntityBase entity = nmsManager.getNMSEntityBase(bukkitEntity);
		if (entity != null) {
			return entity.getHologramLine().getParent();
		}

		return null;
	}
}
