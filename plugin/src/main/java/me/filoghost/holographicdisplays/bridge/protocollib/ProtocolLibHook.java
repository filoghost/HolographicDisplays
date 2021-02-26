/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.bridge.protocollib;

import com.google.common.base.Preconditions;
import me.filoghost.fcommons.logging.Log;
import me.filoghost.holographicdisplays.nms.interfaces.NMSManager;
import me.filoghost.holographicdisplays.object.base.BaseHologram;
import me.filoghost.holographicdisplays.util.VersionUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProtocolLibHook {

    private static boolean enabled;
    private static PacketSender packetSender;

    public static void setup(Plugin plugin, NMSManager nmsManager) {
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
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[Holographic Displays] Detected old version of ProtocolLib, support disabled. You must use ProtocolLib 4.1 or higher.");
                return;
            }

        } catch (Exception e) {
            Log.warning("Could not detect ProtocolLib version (" + e.getMessage() + "), enabling support anyway and hoping for the best.", e);
        }

        try {
            MetadataHelper metadataHelper = new MetadataHelper();
            new PacketListener(plugin, nmsManager, metadataHelper).registerListener();
            packetSender = new PacketSender(metadataHelper);
            Log.info("Enabled player relative placeholders with ProtocolLib.");
        } catch (Exception e) {
            Log.warning("Failed to load ProtocolLib support. Is it updated?", e);
            return;
        }
        
        enabled = true;
    }

    public static void sendDestroyEntitiesPacket(Player player, BaseHologram hologram) {
        checkState();
        packetSender.sendDestroyEntitiesPacket(player, hologram);
    }

    public static void sendCreateEntitiesPacket(Player player, BaseHologram hologram) {
        checkState();
        packetSender.sendCreateEntitiesPacket(player, hologram);
    }

    public static boolean isEnabled() {
        return enabled;
    }

    private static void checkState() {
        Preconditions.checkState(isEnabled(), "hook not enabled");
    }

}
