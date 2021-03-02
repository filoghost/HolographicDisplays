/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.core.nms;

import me.filoghost.holographicdisplays.core.nms.entity.NMSArmorStand;
import me.filoghost.holographicdisplays.core.nms.entity.NMSEntityBase;
import me.filoghost.holographicdisplays.core.nms.entity.NMSItem;
import me.filoghost.holographicdisplays.core.nms.entity.NMSSlime;
import me.filoghost.holographicdisplays.core.hologram.StandardItemLine;
import me.filoghost.holographicdisplays.core.hologram.StandardHologramLine;
import org.bukkit.inventory.ItemStack;

public interface NMSManager {
    
    // A method to register all the custom entities of the plugin, it may fail.
    void setup() throws Exception;
    
    NMSArmorStand spawnNMSArmorStand(org.bukkit.World world, double x, double y, double z, StandardHologramLine parentPiece);
    
    NMSItem spawnNMSItem(org.bukkit.World bukkitWorld, double x, double y, double z, StandardItemLine parentPiece, ItemStack stack);
    
    NMSSlime spawnNMSSlime(org.bukkit.World bukkitWorld, double x, double y, double z, StandardHologramLine parentPiece);
    
    boolean isNMSEntityBase(org.bukkit.entity.Entity bukkitEntity);

    NMSEntityBase getNMSEntityBase(org.bukkit.entity.Entity bukkitEntity);

    NMSEntityBase getNMSEntityBaseFromID(org.bukkit.World bukkitWorld, int entityID);

    Object replaceCustomNameText(Object customNameObject, String target, String replacement);

}
