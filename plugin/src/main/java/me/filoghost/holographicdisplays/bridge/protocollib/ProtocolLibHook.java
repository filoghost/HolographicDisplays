/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.bridge.protocollib;

import me.filoghost.fcommons.Preconditions;
import me.filoghost.fcommons.logging.ErrorCollector;
import me.filoghost.fcommons.logging.Log;
import me.filoghost.holographicdisplays.core.Utils;
import me.filoghost.holographicdisplays.core.hologram.StandardHologram;
import me.filoghost.holographicdisplays.core.nms.NMSManager;
import me.filoghost.holographicdisplays.core.nms.ProtocolPacketSettings;
import me.filoghost.holographicdisplays.util.VersionUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProtocolLibHook {

    private static boolean enabled;
    private static PacketSender packetSender;

    public static void setup(Plugin plugin, NMSManager nmsManager, ProtocolPacketSettings packetSettings, ErrorCollector errorCollector) {
        if (!Bukkit.getPluginManager().isPluginEnabled("ProtocolLib")) {
            return;
        }

        try {
            String protocolVersion = Bukkit.getPluginManager().getPlugin("ProtocolLib").getDescription().getVersion();
            Matcher versionNumbersMatcher = Pattern.compile("([0-9.])+").matcher(protocolVersion);

            if (!versionNumbersMatcher.find()) {
                throw new IllegalArgumentException("unable to find pattern match");
            }

            String versionNumbers = versionNumbersMatcher.group();

            if (!VersionUtils.isVersionGreaterEqual(versionNumbers, "4.1")) {
                errorCollector.add("detected old version of ProtocolLib, support disabled. You must use ProtocolLib 4.1 or higher");
                return;
            }

        } catch (Exception e) {
            errorCollector.add(e, "could not detect ProtocolLib version (" + e.getMessage() + ")," 
                    + " enabling support anyway and hoping for the best");
        }

        try {
            MetadataHelper metadataHelper = new MetadataHelper();
            new PacketListener(plugin, nmsManager, metadataHelper, packetSettings).registerListener();
            packetSender = new PacketSender(metadataHelper);
            Log.info("Enabled player relative placeholders with ProtocolLib.");
        } catch (Exception e) {
            errorCollector.add(e, "failed to load ProtocolLib support, is it updated?");
            return;
        }
        
        enabled = true;
    }

    public static void sendDestroyEntitiesPacket(Player player, StandardHologram hologram) {
        checkState();
        
        if (shouldReceivePacket(player, hologram)) {
            packetSender.sendDestroyEntitiesPacket(player, hologram);
        }
    }

    public static void sendCreateEntitiesPacket(Player player, StandardHologram hologram) {
        checkState();

        if (shouldReceivePacket(player, hologram)) {
            packetSender.sendCreateEntitiesPacket(player, hologram);
        }
    }

    private static boolean shouldReceivePacket(Player player, StandardHologram hologram) {
        if (!player.isOnline()) {
            return false;
        }
        
        if (!player.getWorld().equals(hologram.getWorld())) {
            return false;
        }

        Location playerLocation = player.getLocation();
        double distanceSquared = Utils.distanceSquared(playerLocation.getX(), hologram.getX(), playerLocation.getZ(), hologram.getZ());

        // Approximate, more checks are done for single entities
        return distanceSquared < 128 * 128;
    }

    public static boolean isEnabled() {
        return enabled;
    }

    private static void checkState() {
        Preconditions.checkState(isEnabled(), "hook not enabled");
    }

}
