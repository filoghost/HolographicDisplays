/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.util;

import me.filoghost.fcommons.Preconditions;
import me.filoghost.holographicdisplays.core.nms.NMSManager;
import me.filoghost.holographicdisplays.core.nms.ProtocolPacketSettings;
import org.bukkit.Bukkit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The NMS version is the name of the main package under net.minecraft.server, for example "v1_13_R2".
 */
public enum NMSVersion {
    
    // Not using shorter method reference syntax here because it initializes the class, causing a ClassNotFoundException
    v1_8_R2(packetSettings -> new me.filoghost.holographicdisplays.nms.v1_8_R2.VersionNMSManager(packetSettings)),
    v1_8_R3(packetSettings -> new me.filoghost.holographicdisplays.nms.v1_8_R3.VersionNMSManager(packetSettings)),
    v1_9_R1(packetSettings -> new me.filoghost.holographicdisplays.nms.v1_9_R1.VersionNMSManager(packetSettings)),
    v1_9_R2(packetSettings -> new me.filoghost.holographicdisplays.nms.v1_9_R2.VersionNMSManager(packetSettings)),
    v1_10_R1(packetSettings -> new me.filoghost.holographicdisplays.nms.v1_10_R1.VersionNMSManager(packetSettings)),
    v1_11_R1(packetSettings -> new me.filoghost.holographicdisplays.nms.v1_11_R1.VersionNMSManager(packetSettings)),
    v1_12_R1(packetSettings -> new me.filoghost.holographicdisplays.nms.v1_12_R1.VersionNMSManager(packetSettings)),
    v1_13_R1(packetSettings -> new me.filoghost.holographicdisplays.nms.v1_13_R1.VersionNMSManager(packetSettings)),
    v1_13_R2(packetSettings -> new me.filoghost.holographicdisplays.nms.v1_13_R2.VersionNMSManager(packetSettings)),
    v1_14_R1(packetSettings -> new me.filoghost.holographicdisplays.nms.v1_14_R1.VersionNMSManager(packetSettings)),
    v1_15_R1(packetSettings -> new me.filoghost.holographicdisplays.nms.v1_15_R1.VersionNMSManager(packetSettings)),
    v1_16_R1(packetSettings -> new me.filoghost.holographicdisplays.nms.v1_16_R1.VersionNMSManager(packetSettings)),
    v1_16_R2(packetSettings -> new me.filoghost.holographicdisplays.nms.v1_16_R2.VersionNMSManager(packetSettings)),
    v1_16_R3(packetSettings -> new me.filoghost.holographicdisplays.nms.v1_16_R3.VersionNMSManager(packetSettings));
    
    private static final NMSVersion CURRENT_VERSION = extractCurrentVersion();
    
    private final NMSManagerConstructor nmsManagerConstructor;

    NMSVersion(NMSManagerConstructor nmsManagerConstructor) {
        this.nmsManagerConstructor = nmsManagerConstructor;
    }

    public static NMSManager createNMSManager(ProtocolPacketSettings protocolPacketSettings) {
        return getValid().nmsManagerConstructor.create(protocolPacketSettings);
    }

    private static NMSVersion extractCurrentVersion() {
        Matcher matcher = Pattern.compile("v\\d+_\\d+_R\\d+").matcher(Bukkit.getServer().getClass().getPackage().getName());
        if (!matcher.find()) {
            return null;
        }

        String nmsVersionName = matcher.group();
        try {
            return valueOf(nmsVersionName);
        } catch (IllegalArgumentException e) {
            return null; // Unknown version
        }
    }
    
    public static boolean isValid() {
        return CURRENT_VERSION != null;
    }

    private static NMSVersion getValid() {
        Preconditions.checkState(isValid(), "Current version is not valid");
        return CURRENT_VERSION;
    }

    public static boolean isGreaterEqualThan(NMSVersion other) {
        return getValid().ordinal() >= other.ordinal();
    }


    private interface NMSManagerConstructor {

        NMSManager create(ProtocolPacketSettings protocolPacketSettings);
        
    }
    
}
