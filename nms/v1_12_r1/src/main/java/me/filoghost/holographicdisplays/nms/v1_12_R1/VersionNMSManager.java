/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.v1_12_R1;

import me.filoghost.fcommons.Preconditions;
import me.filoghost.fcommons.reflection.ClassToken;
import me.filoghost.fcommons.reflection.ReflectField;
import me.filoghost.fcommons.reflection.ReflectMethod;
import me.filoghost.holographicdisplays.common.hologram.StandardHologramLine;
import me.filoghost.holographicdisplays.common.hologram.StandardItemLine;
import me.filoghost.holographicdisplays.common.nms.NMSManager;
import me.filoghost.holographicdisplays.common.nms.ProtocolPacketSettings;
import me.filoghost.holographicdisplays.common.nms.SpawnFailedException;
import me.filoghost.holographicdisplays.common.nms.entity.NMSArmorStand;
import me.filoghost.holographicdisplays.common.nms.entity.NMSEntity;
import me.filoghost.holographicdisplays.common.nms.entity.NMSItem;
import net.minecraft.server.v1_12_R1.Entity;
import net.minecraft.server.v1_12_R1.EntityTypes;
import net.minecraft.server.v1_12_R1.MathHelper;
import net.minecraft.server.v1_12_R1.RegistryID;
import net.minecraft.server.v1_12_R1.RegistryMaterials;
import net.minecraft.server.v1_12_R1.World;
import net.minecraft.server.v1_12_R1.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.inventory.ItemStack;

public class VersionNMSManager implements NMSManager {

    private static final ReflectField<RegistryID<Class<? extends Entity>>> REGISTRY_ID_FIELD
            = ReflectField.lookup(new ClassToken<RegistryID<Class<? extends Entity>>>() {}, RegistryMaterials.class, "a");
    private static final ReflectField<Object[]> ID_TO_CLASS_MAP_FIELD
            = ReflectField.lookup(Object[].class, RegistryID.class, "d");

    private static final ReflectMethod<?> REGISTER_ENTITY_METHOD = ReflectMethod.lookup(Object.class, World.class, "b", Entity.class);

    private final ProtocolPacketSettings protocolPacketSettings;

    public VersionNMSManager(ProtocolPacketSettings protocolPacketSettings) {
        this.protocolPacketSettings = protocolPacketSettings;
    }

    @Override
    public void setup() throws Exception {
        registerCustomEntity(EntityNMSSlime.class, 55);
    }

    public void registerCustomEntity(Class<? extends Entity> entityClass, int id) throws Exception {
        // Use reflection to get the RegistryID of entities
        RegistryID<Class<? extends Entity>> registryID = REGISTRY_ID_FIELD.get(EntityTypes.b);
        Object[] idToClassMap = ID_TO_CLASS_MAP_FIELD.get(registryID);

        // Save the the ID -> entity class mapping before the registration
        Object oldValue = idToClassMap[id];

        // Register the entity class
        registryID.a(entityClass, id);

        // Restore the ID -> entity class mapping
        idToClassMap[id] = oldValue;
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
