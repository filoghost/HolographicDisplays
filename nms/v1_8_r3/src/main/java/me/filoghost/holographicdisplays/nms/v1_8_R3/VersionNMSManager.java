/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.v1_8_R3;

import me.filoghost.fcommons.Preconditions;
import me.filoghost.fcommons.reflection.ClassToken;
import me.filoghost.fcommons.reflection.ReflectField;
import me.filoghost.fcommons.reflection.ReflectMethod;
import me.filoghost.holographicdisplays.api.line.HologramLine;
import me.filoghost.holographicdisplays.api.line.ItemLine;
import me.filoghost.holographicdisplays.core.DebugLogger;
import me.filoghost.holographicdisplays.core.nms.CustomNameHelper;
import me.filoghost.holographicdisplays.core.nms.ItemPickupManager;
import me.filoghost.holographicdisplays.core.nms.NMSManager;
import me.filoghost.holographicdisplays.core.nms.PacketController;
import me.filoghost.holographicdisplays.core.nms.entity.NMSArmorStand;
import me.filoghost.holographicdisplays.core.nms.entity.NMSEntityBase;
import me.filoghost.holographicdisplays.core.nms.entity.NMSItem;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityTypes;
import net.minecraft.server.v1_8_R3.MathHelper;
import net.minecraft.server.v1_8_R3.World;
import net.minecraft.server.v1_8_R3.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class VersionNMSManager implements NMSManager {
    
    private static final ReflectField<Map<Class<?>, String>> ENTITY_NAMES_BY_CLASS_FIELD = ReflectField.lookup(new ClassToken<Map<Class<?>, String>>(){}, EntityTypes.class, "d");
    private static final ReflectField<Map<Class<?>, Integer>> ENTITY_IDS_BY_CLASS_FIELD = ReflectField.lookup(new ClassToken<Map<Class<?>, Integer>>(){}, EntityTypes.class, "f");

    private static final ReflectMethod<?> VALIDATE_ENTITY_METHOD = ReflectMethod.lookup(Object.class, World.class, "a", Entity.class);

    private final ItemPickupManager itemPickupManager;
    private final PacketController packetController;

    public VersionNMSManager(ItemPickupManager itemPickupManager, PacketController packetController) {
        this.itemPickupManager = itemPickupManager;
        this.packetController = packetController;
    }
    
    @Override
    public void setup() throws Exception {
        registerCustomEntity(EntityNMSArmorStand.class, "ArmorStand", 30);
        registerCustomEntity(EntityNMSItem.class, "Item", 1);
        registerCustomEntity(EntityNMSSlime.class, "Slime", 55);
    }
    
    public void registerCustomEntity(Class<?> entityClass, String name, int id) throws Exception {
        ENTITY_NAMES_BY_CLASS_FIELD.getStatic().put(entityClass, name);
        ENTITY_IDS_BY_CLASS_FIELD.getStatic().put(entityClass, id);
    }
    
    @Override
    public NMSItem spawnNMSItem(org.bukkit.World bukkitWorld, double x, double y, double z, ItemLine parentPiece, ItemStack stack) {
        WorldServer nmsWorld = ((CraftWorld) bukkitWorld).getHandle();
        EntityNMSItem customItem = new EntityNMSItem(nmsWorld, parentPiece, itemPickupManager);
        customItem.setLocationNMS(x, y, z);
        customItem.setItemStackNMS(stack);
        if (!addEntityToWorld(nmsWorld, customItem)) {
            DebugLogger.handleSpawnFail(parentPiece);
        }
        return customItem;
    }
    
    @Override
    public EntityNMSSlime spawnNMSSlime(org.bukkit.World bukkitWorld, double x, double y, double z, HologramLine parentPiece) {
        WorldServer nmsWorld = ((CraftWorld) bukkitWorld).getHandle();
        EntityNMSSlime touchSlime = new EntityNMSSlime(nmsWorld, parentPiece);
        touchSlime.setLocationNMS(x, y, z);
        if (!addEntityToWorld(nmsWorld, touchSlime)) {
            DebugLogger.handleSpawnFail(parentPiece);
        }
        return touchSlime;
    }
    
    @Override
    public NMSArmorStand spawnNMSArmorStand(org.bukkit.World world, double x, double y, double z, HologramLine parentPiece) {
        WorldServer nmsWorld = ((CraftWorld) world).getHandle();
        EntityNMSArmorStand invisibleArmorStand = new EntityNMSArmorStand(nmsWorld, parentPiece, packetController);
        invisibleArmorStand.setLocationNMS(x, y, z);
        if (!addEntityToWorld(nmsWorld, invisibleArmorStand)) {
            DebugLogger.handleSpawnFail(parentPiece);
        }
        return invisibleArmorStand;
    }
    
    private boolean addEntityToWorld(WorldServer nmsWorld, Entity nmsEntity) {
        Preconditions.checkState(Bukkit.isPrimaryThread(), "Async entity add");
        
        if (!VALIDATE_ENTITY_METHOD.isValid()) {
            return nmsWorld.addEntity(nmsEntity, SpawnReason.CUSTOM);
        }
        
        final int chunkX = MathHelper.floor(nmsEntity.locX / 16.0);
        final int chunkZ = MathHelper.floor(nmsEntity.locZ / 16.0);
        
        if (!nmsWorld.chunkProviderServer.isChunkLoaded(chunkX, chunkZ)) {
            // This should never happen
            nmsEntity.dead = true;
            return false;
        }
        
        nmsWorld.getChunkAt(chunkX, chunkZ).a(nmsEntity);
        nmsWorld.entityList.add(nmsEntity);
        
        try {
            VALIDATE_ENTITY_METHOD.invoke(nmsWorld, nmsEntity);
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    @Override
    public boolean isNMSEntityBase(org.bukkit.entity.Entity bukkitEntity) {
        return ((CraftEntity) bukkitEntity).getHandle() instanceof NMSEntityBase;
    }

    @Override
    public NMSEntityBase getNMSEntityBase(org.bukkit.entity.Entity bukkitEntity) {
        Entity nmsEntity = ((CraftEntity) bukkitEntity).getHandle();
        
        if (nmsEntity instanceof NMSEntityBase) {
            return ((NMSEntityBase) nmsEntity);
        } else {
            return null;
        }
    }
    
    @Override
    public NMSEntityBase getNMSEntityBaseFromID(org.bukkit.World bukkitWorld, int entityID) {
        WorldServer nmsWorld = ((CraftWorld) bukkitWorld).getHandle();
        Entity nmsEntity = nmsWorld.a(entityID);
        
        if (nmsEntity instanceof NMSEntityBase) {
            return ((NMSEntityBase) nmsEntity);
        } else {
            return null;
        }
    }
    
    @Override
    public Object replaceCustomNameText(Object customNameObject, String target, String replacement) {
        return CustomNameHelper.replaceCustomNameString(customNameObject, target, replacement);
    }

}
