/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.util;

import me.filoghost.fcommons.Preconditions;
import me.filoghost.holographicdisplays.nms.interfaces.ItemPickupManager;
import me.filoghost.holographicdisplays.nms.interfaces.NMSManager;
import me.filoghost.holographicdisplays.nms.interfaces.PacketController;
import org.bukkit.Bukkit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The NMS version is the name of the main package under net.minecraft.server, for example "v1_13_R2".
 */
public enum NMSVersion {
    
    v1_8_R2(me.filoghost.holographicdisplays.nms.v1_8_R2.VersionNMSManager::new),
    v1_8_R3(me.filoghost.holographicdisplays.nms.v1_8_R3.VersionNMSManager::new),
    v1_9_R1(me.filoghost.holographicdisplays.nms.v1_9_R1.VersionNMSManager::new),
    v1_9_R2(me.filoghost.holographicdisplays.nms.v1_9_R2.VersionNMSManager::new),
    v1_10_R1(me.filoghost.holographicdisplays.nms.v1_10_R1.VersionNMSManager::new),
    v1_11_R1(me.filoghost.holographicdisplays.nms.v1_11_R1.VersionNMSManager::new),
    v1_12_R1(me.filoghost.holographicdisplays.nms.v1_12_R1.VersionNMSManager::new),
    v1_13_R1(me.filoghost.holographicdisplays.nms.v1_13_R1.VersionNMSManager::new),
    v1_13_R2(me.filoghost.holographicdisplays.nms.v1_13_R2.VersionNMSManager::new),
    v1_14_R1(me.filoghost.holographicdisplays.nms.v1_14_R1.VersionNMSManager::new),
    v1_15_R1(me.filoghost.holographicdisplays.nms.v1_15_R1.VersionNMSManager::new),
    v1_16_R1(me.filoghost.holographicdisplays.nms.v1_16_R1.VersionNMSManager::new),
    v1_16_R2(me.filoghost.holographicdisplays.nms.v1_16_R2.VersionNMSManager::new),
    v1_16_R3(me.filoghost.holographicdisplays.nms.v1_16_R3.VersionNMSManager::new);
    
    private static final NMSVersion CURRENT_VERSION = extractCurrentVersion();
    
    private final NMSManagerConstructor nmsManagerConstructor;

    NMSVersion(NMSManagerConstructor nmsManagerConstructor) {
        this.nmsManagerConstructor = nmsManagerConstructor;
    }

    public static NMSManager createNMSManager(ItemPickupManager itemPickupManager, PacketController packetController) {
        return getValid().nmsManagerConstructor.create(itemPickupManager, packetController);
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

        NMSManager create(ItemPickupManager itemPickupManager, PacketController packetController);
        
    }
    
}
