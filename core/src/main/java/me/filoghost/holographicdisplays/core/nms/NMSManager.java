/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.core.nms;

import me.filoghost.holographicdisplays.core.hologram.StandardHologramLine;
import me.filoghost.holographicdisplays.core.hologram.StandardItemLine;
import me.filoghost.holographicdisplays.core.nms.entity.NMSArmorStand;
import me.filoghost.holographicdisplays.core.nms.entity.NMSEntity;
import me.filoghost.holographicdisplays.core.nms.entity.NMSItem;
import me.filoghost.holographicdisplays.core.nms.entity.NMSSlime;
import org.bukkit.inventory.ItemStack;

public interface NMSManager {
    
    // A method to register all the custom entities of the plugin, it may fail.
    void setup() throws Exception;
    
    NMSArmorStand spawnNMSArmorStand(org.bukkit.World world, double x, double y, double z, StandardHologramLine parentHologramLine) throws SpawnFailedException;
    
    NMSItem spawnNMSItem(org.bukkit.World bukkitWorld, double x, double y, double z, StandardItemLine parentHologramLine, ItemStack stack) throws SpawnFailedException;
    
    NMSSlime spawnNMSSlime(org.bukkit.World bukkitWorld, double x, double y, double z, StandardHologramLine parentHologramLine) throws SpawnFailedException;
    
    boolean isNMSEntityBase(org.bukkit.entity.Entity bukkitEntity);

    NMSEntity getNMSEntityBase(org.bukkit.entity.Entity bukkitEntity);

    NMSEntity getNMSEntityBaseFromID(org.bukkit.World bukkitWorld, int entityID);

    CustomNameEditor getCustomNameEditor();

}
