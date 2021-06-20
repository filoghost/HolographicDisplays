/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.common.nms;

import me.filoghost.holographicdisplays.common.hologram.StandardHologramLine;
import me.filoghost.holographicdisplays.common.hologram.StandardItemLine;
import me.filoghost.holographicdisplays.common.nms.entity.NMSArmorStand;
import me.filoghost.holographicdisplays.common.nms.entity.NMSEntity;
import me.filoghost.holographicdisplays.common.nms.entity.NMSItem;
import me.filoghost.holographicdisplays.common.nms.entity.NMSSlime;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

public interface NMSManager {

    /**
     * Register all the custom entities of the plugin.
     *
     * @throws Exception if anything during the process fails
     */
    void setup() throws Exception;

    NMSArmorStand spawnNMSArmorStand(World bukkitWorld, double x, double y, double z, StandardHologramLine parentHologramLine)
            throws SpawnFailedException;

    NMSItem spawnNMSItem(World bukkitWorld, double x, double y, double z, StandardItemLine parentHologramLine, ItemStack stack)
            throws SpawnFailedException;

    NMSSlime spawnNMSSlime(World bukkitWorld, double x, double y, double z, StandardHologramLine parentHologramLine)
            throws SpawnFailedException;

    boolean isNMSEntityBase(Entity bukkitEntity);

    NMSEntity getNMSEntityBase(Entity bukkitEntity);

    NMSEntity getNMSEntityBaseFromID(World bukkitWorld, int entityID);

    Object createCustomNameNMSObject(String customName);

}
