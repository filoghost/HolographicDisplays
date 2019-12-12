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

import com.comphenix.net.sf.cglib.proxy.Factory;
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
import com.gmail.filoghost.holographicdisplays.hook.PlaceholderAPIHook;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.NMSManager;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.entity.NMSArmorStand;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.entity.NMSEntityBase;
import com.gmail.filoghost.holographicdisplays.object.CraftHologram;
import com.gmail.filoghost.holographicdisplays.object.line.*;
import com.gmail.filoghost.holographicdisplays.placeholder.RelativePlaceholder;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * This is for the ProtocolLib versions containing the WrappedDataWatcher.WrappedDataWatcherObject class.
 */
public class ProtocolLibHookImpl implements ProtocolLibHook {

	private NMSManager nmsManager;
	private PacketHelper packetHelper;
	private MetadataHelper metadataHelper;
	private boolean useGetEntityWorkaround;


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

					if (!hologramLine.getParent().isAllowPlaceholders()) {
						return;
					}

					spawnEntityPacket = new WrapperPlayServerSpawnEntityLiving(packet.deepClone());
					WrappedWatchableObject customNameWatchableObject = metadataHelper.getCustomNameWacthableObject(spawnEntityPacket.getMetadata());

					if (customNameWatchableObject == null) {
						return;
					}

					boolean modifiedRelative = replaceRelativePlaceholders(customNameWatchableObject, player, hologramLine.getRelativePlaceholders());
					boolean modifiedPapi = replacePlaceholderAPI(customNameWatchableObject, player);
					if (modifiedRelative || modifiedPapi) {
						event.setPacket(spawnEntityPacket.getHandle());
					}

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

					if (!hologramLine.getParent().isAllowPlaceholders()) {
						return;
					}

					entityMetadataPacket = new WrapperPlayServerEntityMetadata(packet.deepClone());
					WrappedWatchableObject customNameWatchableObject = metadataHelper.getCustomNameWatchableObject(entityMetadataPacket.getEntityMetadata());

					if (customNameWatchableObject == null) {
						return;
					}

					boolean modifiedRelative = replaceRelativePlaceholders(customNameWatchableObject, player, hologramLine.getRelativePlaceholders());
					boolean modifiedPapi = replacePlaceholderAPI(customNameWatchableObject, player);
					if (modifiedRelative || modifiedPapi) {
						event.setPacket(entityMetadataPacket.getHandle());
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

		String customName = metadataHelper.getSerializedCustomName(customNameWatchableObject);
		if (customName == null) {
			return false;
		}

		if (relativePlaceholders == null) return false;

		for (RelativePlaceholder relativePlaceholder : relativePlaceholders) {
			customName = customName.replace(relativePlaceholder.getTextPlaceholder(), relativePlaceholder.getReplacement(player));
		}

		metadataHelper.setSerializedCustomName(customNameWatchableObject, customName);
		return true;
	}

	private boolean replacePlaceholderAPI(WrappedWatchableObject customNameWatchableObject, Player player) {
		if (customNameWatchableObject == null) {
			return true;
		}

		String customName = metadataHelper.getSerializedCustomName(customNameWatchableObject);
		if (customName == null) {
			return false;
		}

		customName = PlaceholderAPIHook.translate(player, customName);

		metadataHelper.setSerializedCustomName(customNameWatchableObject, customName);
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