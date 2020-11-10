/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.interfaces;

import me.filoghost.holographicdisplays.api.line.HologramLine;
import me.filoghost.holographicdisplays.api.line.ItemLine;
import me.filoghost.holographicdisplays.nms.interfaces.entity.NMSArmorStand;
import me.filoghost.holographicdisplays.nms.interfaces.entity.NMSEntityBase;
import me.filoghost.holographicdisplays.nms.interfaces.entity.NMSItem;
import me.filoghost.holographicdisplays.nms.interfaces.entity.NMSSlime;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

public interface NMSManager {
    
    // A method to register all the custom entities of the plugin, it may fail.
    public void setup() throws Exception;
    
    public NMSArmorStand spawnNMSArmorStand(org.bukkit.World world, double x, double y, double z, HologramLine parentPiece, boolean broadcastLocationPacket);
    
    public NMSItem spawnNMSItem(org.bukkit.World bukkitWorld, double x, double y, double z, ItemLine parentPiece, ItemStack stack, ItemPickupManager itemPickupManager);
    
    public NMSSlime spawnNMSSlime(org.bukkit.World bukkitWorld, double x, double y, double z, HologramLine parentPiece);
    
    public boolean isNMSEntityBase(org.bukkit.entity.Entity bukkitEntity);

    public NMSEntityBase getNMSEntityBase(org.bukkit.entity.Entity bukkitEntity);

    public NMSEntityBase getNMSEntityBaseFromID(World bukkitWorld, int entityID);

    public Object replaceCustomNameText(Object customNameObject, String target, String replacement);

}
