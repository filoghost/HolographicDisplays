/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.bridge.protocollib;

import com.comphenix.net.sf.cglib.proxy.Factory;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedWatchableObject;
import me.filoghost.holographicdisplays.bridge.protocollib.packet.EntityRelatedPacketWrapper;
import me.filoghost.holographicdisplays.bridge.protocollib.packet.WrapperPlayServerEntityMetadata;
import me.filoghost.holographicdisplays.bridge.protocollib.packet.WrapperPlayServerSpawnEntity;
import me.filoghost.holographicdisplays.bridge.protocollib.packet.WrapperPlayServerSpawnEntityLiving;
import me.filoghost.holographicdisplays.core.hologram.StandardHologramLine;
import me.filoghost.holographicdisplays.core.hologram.StandardTextLine;
import me.filoghost.holographicdisplays.core.nms.NMSManager;
import me.filoghost.holographicdisplays.core.nms.entity.NMSArmorStand;
import me.filoghost.holographicdisplays.core.nms.entity.NMSEntityBase;
import me.filoghost.holographicdisplays.core.placeholder.RelativePlaceholder;
import me.filoghost.holographicdisplays.util.NMSVersion;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Collection;

class PacketListener extends PacketAdapter {
    
    private final NMSManager nmsManager;
    private final MetadataHelper metadataHelper;

    public PacketListener(Plugin plugin, NMSManager nmsManager, MetadataHelper metadataHelper) {
        super(PacketAdapter.params()
                .plugin(plugin)
                .types(
                        PacketType.Play.Server.SPAWN_ENTITY_LIVING,
                        PacketType.Play.Server.SPAWN_ENTITY,
                        PacketType.Play.Server.ENTITY_METADATA,
                        PacketType.Play.Server.REL_ENTITY_MOVE,
                        PacketType.Play.Server.REL_ENTITY_MOVE_LOOK)
                .serverSide()
                .listenerPriority(ListenerPriority.NORMAL));

        this.nmsManager = nmsManager;
        this.metadataHelper = metadataHelper;
    }

    public void registerListener() {
        ProtocolLibrary.getProtocolManager().addPacketListener(this);
    }

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
            StandardHologramLine hologramLine = getHologramLine(event, spawnEntityPacket);

            if (hologramLine == null) {
                return;
            }

            if (!hologramLine.getHologram().isVisibleTo(player)) {
                event.setCancelled(true);
                return;
            }

            if (NMSVersion.isGreaterEqualThan(NMSVersion.v1_15_R1)) {
                // There's no metadata field in 1.15+ on the spawn entity packet
                return;
            }

            if (!(hologramLine instanceof StandardTextLine)) {
                return;
            }
            
            StandardTextLine textLine = (StandardTextLine) hologramLine;
            Collection<RelativePlaceholder> relativePlaceholders = textLine.getRelativePlaceholders();
            
            if (relativePlaceholders == null || relativePlaceholders.isEmpty()) {
                return;
            }

            spawnEntityPacket = new WrapperPlayServerSpawnEntityLiving(packet.deepClone());
            WrappedWatchableObject customNameWatchableObject = metadataHelper.getCustomNameWacthableObject(spawnEntityPacket.getMetadata());

            if (customNameWatchableObject == null) {
                return;
            }

            replaceRelativePlaceholders(customNameWatchableObject, player, relativePlaceholders);
            event.setPacket(spawnEntityPacket.getHandle());

        } else if (packet.getType() == PacketType.Play.Server.SPAWN_ENTITY) {
            WrapperPlayServerSpawnEntity spawnEntityPacket = new WrapperPlayServerSpawnEntity(packet);
            StandardHologramLine hologramLine = getHologramLine(event, spawnEntityPacket);

            if (hologramLine == null) {
                return;
            }

            if (!hologramLine.getHologram().isVisibleTo(player)) {
                event.setCancelled(true);
                return;
            }

        } else if (packet.getType() == PacketType.Play.Server.ENTITY_METADATA) {
            WrapperPlayServerEntityMetadata entityMetadataPacket = new WrapperPlayServerEntityMetadata(packet);
            StandardHologramLine hologramLine = getHologramLine(event, entityMetadataPacket);

            if (hologramLine == null) {
                return;
            }

            if (!hologramLine.getHologram().isVisibleTo(player)) {
                event.setCancelled(true);
                return;
            }
            
            if (!(hologramLine instanceof StandardTextLine)) {
                return;
            }
            StandardTextLine textLine = (StandardTextLine) hologramLine;
            Collection<RelativePlaceholder> relativePlaceholders = textLine.getRelativePlaceholders();

            if (relativePlaceholders == null || relativePlaceholders.isEmpty()) {
                return;
            }

            entityMetadataPacket = new WrapperPlayServerEntityMetadata(packet.deepClone());
            WrappedWatchableObject customNameWatchableObject = metadataHelper.getCustomNameWatchableObject(entityMetadataPacket.getEntityMetadata());

            if (customNameWatchableObject == null) {
                return;
            }

            boolean modified = replaceRelativePlaceholders(customNameWatchableObject, player, relativePlaceholders);
            if (modified) {
                event.setPacket(entityMetadataPacket.getHandle());
            }

        } else if (packet.getType() == PacketType.Play.Server.REL_ENTITY_MOVE || packet.getType() == PacketType.Play.Server.REL_ENTITY_MOVE_LOOK) {
            int entityID = packet.getIntegers().read(0);
            NMSEntityBase nmsEntityBase = nmsManager.getNMSEntityBaseFromID(event.getPlayer().getWorld(), entityID);

            if (nmsEntityBase instanceof NMSArmorStand) {
                event.setCancelled(true); // Don't send relative movement packets for armor stands, only keep precise teleport packets.
            }
        }
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
            replacedCustomNameNMSObject = nmsManager.getCustomNameChatComponentEditor().replaceCustomName(
                    replacedCustomNameNMSObject, 
                    relativePlaceholder.getTextPlaceholder(), 
                    relativePlaceholder.getReplacement(player));
        }
        
        if (replacedCustomNameNMSObject == originalCustomNameNMSObject) {
            // It means nothing has been replaced, since original custom name has been returned.
            return false;
        }
        
        metadataHelper.setCustomNameNMSObject(customNameWatchableObject, replacedCustomNameNMSObject);
        return true;
    }    
    
    private StandardHologramLine getHologramLine(PacketEvent packetEvent, EntityRelatedPacketWrapper packetWrapper) {
        return getHologramLine(packetEvent.getPlayer().getWorld(), packetWrapper.getEntityID());
    }
    
    private StandardHologramLine getHologramLine(World world, int entityID) {
        if (entityID < 0) {
            return null;
        }

        NMSEntityBase nmsEntity = nmsManager.getNMSEntityBaseFromID(world, entityID);
        if (nmsEntity == null) {
            return null; // Entity not existing or not related to holograms.
        }
        
        return nmsEntity.getHologramLine();
    }

}
