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
import me.filoghost.holographicdisplays.bridge.protocollib.packet.AbstractPacket;
import me.filoghost.holographicdisplays.bridge.protocollib.packet.WrapperPlayServerEntityMetadata;
import me.filoghost.holographicdisplays.bridge.protocollib.packet.WrapperPlayServerSpawnEntityLiving;
import me.filoghost.holographicdisplays.core.hologram.StandardHologramLine;
import me.filoghost.holographicdisplays.core.hologram.StandardTextLine;
import me.filoghost.holographicdisplays.core.nms.NMSManager;
import me.filoghost.holographicdisplays.core.nms.ProtocolPacketSettings;
import me.filoghost.holographicdisplays.core.nms.entity.NMSArmorStand;
import me.filoghost.holographicdisplays.core.nms.entity.NMSEntity;
import me.filoghost.holographicdisplays.core.placeholder.RelativePlaceholder;
import me.filoghost.holographicdisplays.util.NMSVersion;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Collection;

class PacketListener extends PacketAdapter {
    
    private final NMSManager nmsManager;
    private final MetadataHelper metadataHelper;
    private final ProtocolPacketSettings packetSettings;

    PacketListener(Plugin plugin, NMSManager nmsManager, MetadataHelper metadataHelper, ProtocolPacketSettings packetSettings) {
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
        this.packetSettings = packetSettings;
    }

    public void registerListener() {
        ProtocolLibrary.getProtocolManager().addPacketListener(this);
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        PacketContainer packet = event.getPacket();
        PacketType packetType = packet.getType();
        Player player = event.getPlayer();

        if (player instanceof Factory) {
            return; // Ignore temporary players (reference: https://github.com/dmulloy2/ProtocolLib/issues/349)
        }

        int entityID = packet.getIntegers().read(0);
        if (entityID < 0) {
            return;
        }

        NMSEntity nmsEntity = nmsManager.getNMSEntityBaseFromID(event.getPlayer().getWorld(), entityID);
        if (nmsEntity == null) {
            return; // Entity not existing or not related to holograms
        }

        if (packetType == PacketType.Play.Server.REL_ENTITY_MOVE || packetType == PacketType.Play.Server.REL_ENTITY_MOVE_LOOK) {
            if (nmsEntity instanceof NMSArmorStand && packetSettings.sendAccurateLocationPackets()) {
                event.setCancelled(true); // Don't send relative movement packets for armor stands, only keep precise teleport packets
            }
            return;
        }

        StandardHologramLine hologramLine = nmsEntity.getHologramLine();
        
        if (!hologramLine.getHologram().isVisibleTo(player)) {
            event.setCancelled(true);
            return;
        }

        if (packetType == PacketType.Play.Server.SPAWN_ENTITY_LIVING && NMSVersion.isGreaterEqualThan(NMSVersion.v1_15_R1)) {
            // There's no metadata field in 1.15+ on the spawn entity packet, ignore it
            return;
        }
        
        if (packetType == PacketType.Play.Server.SPAWN_ENTITY_LIVING || packetType == PacketType.Play.Server.ENTITY_METADATA) {
            if (!(hologramLine instanceof StandardTextLine) || !(nmsEntity instanceof NMSArmorStand)) {
                return;
            }
            
            StandardTextLine textLine = (StandardTextLine) hologramLine;
            
            if (!textLine.isAllowPlaceholders()) {
                return;
            }

            NMSArmorStand nmsArmorStand = (NMSArmorStand) nmsEntity;
            String customName = nmsArmorStand.getCustomNameStringNMS();
            String customNameWithRelativePlaceholders = replaceRelativePlaceholders(textLine, customName, player);
            
            if (customNameWithRelativePlaceholders.equals(customName)) {
                return; // No need to modify packets, custom name doesn't need changes
            }

            WrappedWatchableObject customNameWatchableObject;
            AbstractPacket packetWrapper;

            if (packetType == PacketType.Play.Server.ENTITY_METADATA) {
                WrapperPlayServerEntityMetadata entityMetadataPacket = new WrapperPlayServerEntityMetadata(packet.deepClone());
                packetWrapper = entityMetadataPacket;
                customNameWatchableObject = metadataHelper.getCustomNameWatchableObject(entityMetadataPacket.getEntityMetadata());
            } else {
                WrapperPlayServerSpawnEntityLiving spawnEntityPacket = new WrapperPlayServerSpawnEntityLiving(packet.deepClone());
                packetWrapper = spawnEntityPacket;
                customNameWatchableObject = metadataHelper.getCustomNameWacthableObject(spawnEntityPacket.getMetadata());
            }
            
            if (customNameWatchableObject == null) {
                return;
            }

            Object customNameNMSObject = nmsManager.createCustomNameNMSObject(customNameWithRelativePlaceholders);
            metadataHelper.setCustomNameNMSObject(customNameWatchableObject, customNameNMSObject);
            event.setPacket(packetWrapper.getHandle());
        }
    }

    private String replaceRelativePlaceholders(StandardTextLine textLine, String text, Player player) {
        Collection<RelativePlaceholder> relativePlaceholders = textLine.getRelativePlaceholders();
        
        if (relativePlaceholders != null && !relativePlaceholders.isEmpty()) {
            for (RelativePlaceholder relativePlaceholder : relativePlaceholders) {
                if (text.contains(relativePlaceholder.getTextPlaceholder())) {
                    text = text.replace(
                            relativePlaceholder.getTextPlaceholder(),
                            relativePlaceholder.getReplacement(player));
                }
            }
        }

        return text;
    }

}
