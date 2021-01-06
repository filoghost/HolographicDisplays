/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.v1_11_R1;

import me.filoghost.fcommons.Preconditions;
import me.filoghost.fcommons.reflection.ClassToken;
import me.filoghost.fcommons.reflection.ReflectField;
import me.filoghost.fcommons.reflection.ReflectMethod;
import me.filoghost.holographicdisplays.api.line.HologramLine;
import me.filoghost.holographicdisplays.api.line.ItemLine;
import me.filoghost.holographicdisplays.common.DebugLogger;
import me.filoghost.holographicdisplays.nms.interfaces.CustomNameHelper;
import me.filoghost.holographicdisplays.nms.interfaces.ItemPickupManager;
import me.filoghost.holographicdisplays.nms.interfaces.NMSManager;
import me.filoghost.holographicdisplays.nms.interfaces.entity.NMSArmorStand;
import me.filoghost.holographicdisplays.nms.interfaces.entity.NMSEntityBase;
import me.filoghost.holographicdisplays.nms.interfaces.entity.NMSItem;
import net.minecraft.server.v1_11_R1.Entity;
import net.minecraft.server.v1_11_R1.EntityTypes;
import net.minecraft.server.v1_11_R1.MathHelper;
import net.minecraft.server.v1_11_R1.RegistryID;
import net.minecraft.server.v1_11_R1.RegistryMaterials;
import net.minecraft.server.v1_11_R1.World;
import net.minecraft.server.v1_11_R1.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_11_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftEntity;
import org.bukkit.inventory.ItemStack;

public class NmsManagerImpl implements NMSManager {
    
    private static final ReflectField<RegistryID<Class<? extends Entity>>> REGISTRY_ID_FIELD = ReflectField.lookup(new ClassToken<RegistryID<Class<? extends Entity>>>(){}, RegistryMaterials.class, "a");
    private static final ReflectField<Object[]> ID_TO_CLASS_MAP_FIELD = ReflectField.lookup(Object[].class, RegistryID.class, "d");

    private static final ReflectMethod<?> VALIDATE_ENTITY_METHOD = ReflectMethod.lookup(Object.class, World.class, "b", Entity.class);
    
    @Override
    public void setup() throws Exception {
        registerCustomEntity(EntityNMSSlime.class, 55);
    }
    
    public void registerCustomEntity(Class<? extends Entity> entityClass, int id) throws Exception {
        // Use reflection to get the RegistryID of entities.
        RegistryID<Class<? extends Entity>> registryID = REGISTRY_ID_FIELD.get(EntityTypes.b);
        Object[] idToClassMap = ID_TO_CLASS_MAP_FIELD.get(registryID);
        
        // Save the the ID -> entity class mapping before the registration.
        Object oldValue = idToClassMap[id];

        // Register the entity class.
        registryID.a(entityClass, id);

        // Restore the ID -> entity class mapping.
        idToClassMap[id] = oldValue;
    }
    
    @Override
    public NMSItem spawnNMSItem(org.bukkit.World bukkitWorld, double x, double y, double z, ItemLine parentPiece, ItemStack stack, ItemPickupManager itemPickupManager) {
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
    public NMSArmorStand spawnNMSArmorStand(org.bukkit.World world, double x, double y, double z, HologramLine parentPiece, boolean broadcastLocationPacket) {
        WorldServer nmsWorld = ((CraftWorld) world).getHandle();
        EntityNMSArmorStand invisibleArmorStand = new EntityNMSArmorStand(nmsWorld, parentPiece);
        invisibleArmorStand.setLocationNMS(x, y, z, broadcastLocationPacket);
        if (!addEntityToWorld(nmsWorld, invisibleArmorStand)) {
            DebugLogger.handleSpawnFail(parentPiece);
        }
        return invisibleArmorStand;
    }
    
    private boolean addEntityToWorld(WorldServer nmsWorld, Entity nmsEntity) {
        Preconditions.checkState(Bukkit.isPrimaryThread(), "Async entity add");
        
        final int chunkX = MathHelper.floor(nmsEntity.locX / 16.0);
        final int chunkZ = MathHelper.floor(nmsEntity.locZ / 16.0);
        
        if (!nmsWorld.getChunkProviderServer().isLoaded(chunkX, chunkZ)) {
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
        Entity nmsEntity = nmsWorld.getEntity(entityID);
        
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
