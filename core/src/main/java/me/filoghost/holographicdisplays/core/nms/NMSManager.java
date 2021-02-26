/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.core.nms;

import me.filoghost.holographicdisplays.api.line.HologramLine;
import me.filoghost.holographicdisplays.api.line.ItemLine;
import me.filoghost.holographicdisplays.core.nms.entity.NMSArmorStand;
import me.filoghost.holographicdisplays.core.nms.entity.NMSEntityBase;
import me.filoghost.holographicdisplays.core.nms.entity.NMSItem;
import me.filoghost.holographicdisplays.core.nms.entity.NMSSlime;
import org.bukkit.inventory.ItemStack;

public interface NMSManager {
    
    // A method to register all the custom entities of the plugin, it may fail.
    void setup() throws Exception;
    
    NMSArmorStand spawnNMSArmorStand(org.bukkit.World world, double x, double y, double z, HologramLine parentPiece);
    
    NMSItem spawnNMSItem(org.bukkit.World bukkitWorld, double x, double y, double z, ItemLine parentPiece, ItemStack stack);
    
    NMSSlime spawnNMSSlime(org.bukkit.World bukkitWorld, double x, double y, double z, HologramLine parentPiece);
    
    boolean isNMSEntityBase(org.bukkit.entity.Entity bukkitEntity);

    NMSEntityBase getNMSEntityBase(org.bukkit.entity.Entity bukkitEntity);

    NMSEntityBase getNMSEntityBaseFromID(org.bukkit.World bukkitWorld, int entityID);

    Object replaceCustomNameText(Object customNameObject, String target, String replacement);

}
