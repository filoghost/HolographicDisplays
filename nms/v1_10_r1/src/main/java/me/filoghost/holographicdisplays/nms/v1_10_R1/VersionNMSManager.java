/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.v1_10_R1;

import me.filoghost.fcommons.Preconditions;
import me.filoghost.fcommons.reflection.ClassToken;
import me.filoghost.fcommons.reflection.ReflectField;
import me.filoghost.fcommons.reflection.ReflectMethod;
import me.filoghost.holographicdisplays.core.hologram.StandardHologramLine;
import me.filoghost.holographicdisplays.core.hologram.StandardItemLine;
import me.filoghost.holographicdisplays.core.nms.NMSManager;
import me.filoghost.holographicdisplays.core.nms.ProtocolPacketSettings;
import me.filoghost.holographicdisplays.core.nms.SpawnFailedException;
import me.filoghost.holographicdisplays.core.nms.entity.NMSArmorStand;
import me.filoghost.holographicdisplays.core.nms.entity.NMSEntity;
import me.filoghost.holographicdisplays.core.nms.entity.NMSItem;
import net.minecraft.server.v1_10_R1.Entity;
import net.minecraft.server.v1_10_R1.EntityTypes;
import net.minecraft.server.v1_10_R1.MathHelper;
import net.minecraft.server.v1_10_R1.World;
import net.minecraft.server.v1_10_R1.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_10_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftEntity;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class VersionNMSManager implements NMSManager {
    
    private static final ReflectField<Map<Class<?>, String>> ENTITY_NAMES_BY_CLASS_FIELD
            = ReflectField.lookup(new ClassToken<Map<Class<?>, String>>(){}, EntityTypes.class, "d");
    private static final ReflectField<Map<Class<?>, Integer>> ENTITY_IDS_BY_CLASS_FIELD
            = ReflectField.lookup(new ClassToken<Map<Class<?>, Integer>>(){}, EntityTypes.class, "f");

    private static final ReflectMethod<?> REGISTER_ENTITY_METHOD = ReflectMethod.lookup(Object.class, World.class, "b", Entity.class);
    
    private final ProtocolPacketSettings protocolPacketSettings;

    public VersionNMSManager(ProtocolPacketSettings protocolPacketSettings) {
        this.protocolPacketSettings = protocolPacketSettings;
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
    public NMSItem spawnNMSItem(
            org.bukkit.World bukkitWorld, double x, double y, double z,
            StandardItemLine parentHologramLine,
            ItemStack stack) throws SpawnFailedException {
        WorldServer nmsWorld = ((CraftWorld) bukkitWorld).getHandle();
        EntityNMSItem item = new EntityNMSItem(nmsWorld, parentHologramLine);
        item.setLocationNMS(x, y, z);
        item.setItemStackNMS(stack);
        addEntityToWorld(nmsWorld, item);
        return item;
    }
    
    @Override
    public EntityNMSSlime spawnNMSSlime(
            org.bukkit.World bukkitWorld, double x, double y, double z,
            StandardHologramLine parentHologramLine) throws SpawnFailedException {
        WorldServer nmsWorld = ((CraftWorld) bukkitWorld).getHandle();
        EntityNMSSlime slime = new EntityNMSSlime(nmsWorld, parentHologramLine);
        slime.setLocationNMS(x, y, z);
        addEntityToWorld(nmsWorld, slime);
        return slime;
    }
    
    @Override
    public NMSArmorStand spawnNMSArmorStand(
            org.bukkit.World world, double x, double y, double z,
            StandardHologramLine parentHologramLine) throws SpawnFailedException {
        WorldServer nmsWorld = ((CraftWorld) world).getHandle();
        EntityNMSArmorStand armorStand = new EntityNMSArmorStand(nmsWorld, parentHologramLine, protocolPacketSettings);
        armorStand.setLocationNMS(x, y, z);
        addEntityToWorld(nmsWorld, armorStand);
        return armorStand;
    }
    
    private void addEntityToWorld(WorldServer nmsWorld, Entity nmsEntity) throws SpawnFailedException {
        Preconditions.checkState(Bukkit.isPrimaryThread(), "Async entity add");
        
        final int chunkX = MathHelper.floor(nmsEntity.locX / 16.0);
        final int chunkZ = MathHelper.floor(nmsEntity.locZ / 16.0);
        
        if (!nmsWorld.getChunkProviderServer().isLoaded(chunkX, chunkZ)) {
            // This should never happen
            nmsEntity.dead = true;
            throw new SpawnFailedException(SpawnFailedException.CHUNK_NOT_LOADED);
        }
        
        nmsWorld.getChunkAt(chunkX, chunkZ).a(nmsEntity);
        nmsWorld.entityList.add(nmsEntity);
        
        try {
            REGISTER_ENTITY_METHOD.invoke(nmsWorld, nmsEntity);
        } catch (ReflectiveOperationException e) {
            nmsEntity.dead = true;
            throw new SpawnFailedException(SpawnFailedException.REGISTER_ENTITY_FAIL, e);
        }
    }
    
    @Override
    public boolean isNMSEntityBase(org.bukkit.entity.Entity bukkitEntity) {
        return ((CraftEntity) bukkitEntity).getHandle() instanceof NMSEntity;
    }

    @Override
    public NMSEntity getNMSEntityBase(org.bukkit.entity.Entity bukkitEntity) {
        Entity nmsEntity = ((CraftEntity) bukkitEntity).getHandle();
        
        if (nmsEntity instanceof NMSEntity) {
            return (NMSEntity) nmsEntity;
        } else {
            return null;
        }
    }
    
    @Override
    public NMSEntity getNMSEntityBaseFromID(org.bukkit.World bukkitWorld, int entityID) {
        WorldServer nmsWorld = ((CraftWorld) bukkitWorld).getHandle();
        Entity nmsEntity = nmsWorld.getEntity(entityID);
        
        if (nmsEntity instanceof NMSEntity) {
            return (NMSEntity) nmsEntity;
        } else {
            return null;
        }
    }

    @Override
    public Object createCustomNameNMSObject(String customName) {
        return EntityNMSArmorStand.createCustomNameNMSObject(customName);
    }

}
