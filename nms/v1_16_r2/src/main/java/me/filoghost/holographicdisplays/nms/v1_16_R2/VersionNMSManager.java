/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.v1_16_R2;

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
import net.minecraft.server.v1_16_R2.Entity;
import net.minecraft.server.v1_16_R2.EntityTypes;
import net.minecraft.server.v1_16_R2.EnumCreatureType;
import net.minecraft.server.v1_16_R2.IRegistry;
import net.minecraft.server.v1_16_R2.MathHelper;
import net.minecraft.server.v1_16_R2.RegistryMaterials;
import net.minecraft.server.v1_16_R2.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_16_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R2.entity.CraftEntity;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class VersionNMSManager implements NMSManager {

    private static final ReflectField<Map<EntityTypes<?>, Integer>> REGISTRY_TO_ID_FIELD
            = ReflectField.lookup(new ClassToken<Map<EntityTypes<?>, Integer>>() {}, RegistryMaterials.class, "bg");
    private static final ReflectMethod<Void> REGISTER_ENTITY_METHOD
            = ReflectMethod.lookup(void.class, WorldServer.class, "registerEntity", Entity.class);

    private final ProtocolPacketSettings protocolPacketSettings;

    public VersionNMSManager(ProtocolPacketSettings protocolPacketSettings) {
        this.protocolPacketSettings = protocolPacketSettings;
    }

    @Override
    public void setup() throws Exception {
        registerCustomEntity(EntityNMSSlime.class, 55, 2.04f, 2.04f);
    }

    public void registerCustomEntity(Class<? extends Entity> entityClass, int id, float sizeWidth, float sizeHeight) throws Exception {
        // Use reflection to map the custom entity to the correct ID
        Map<EntityTypes<?>, Integer> entityTypesToId = REGISTRY_TO_ID_FIELD.get(IRegistry.ENTITY_TYPE);
        EntityTypes<?> customEntityTypes = EntityTypes.Builder.a(EnumCreatureType.MONSTER).a(sizeWidth, sizeHeight).b().a((String) null);
        entityTypesToId.put(customEntityTypes, id);
    }

    @Override
    public NMSItem spawnNMSItem(
            World bukkitWorld, double x, double y, double z,
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
            World bukkitWorld, double x, double y, double z,
            StandardHologramLine parentHologramLine) throws SpawnFailedException {
        WorldServer nmsWorld = ((CraftWorld) bukkitWorld).getHandle();
        EntityNMSSlime slime = new EntityNMSSlime(nmsWorld, parentHologramLine);
        slime.setLocationNMS(x, y, z);
        addEntityToWorld(nmsWorld, slime);
        return slime;
    }

    @Override
    public NMSArmorStand spawnNMSArmorStand(
            World world, double x, double y, double z,
            StandardHologramLine parentHologramLine) throws SpawnFailedException {
        WorldServer nmsWorld = ((CraftWorld) world).getHandle();
        EntityNMSArmorStand armorStand = new EntityNMSArmorStand(nmsWorld, parentHologramLine, protocolPacketSettings);
        armorStand.setLocationNMS(x, y, z);
        addEntityToWorld(nmsWorld, armorStand);
        return armorStand;
    }

    private void addEntityToWorld(WorldServer nmsWorld, Entity nmsEntity) throws SpawnFailedException {
        Preconditions.checkState(Bukkit.isPrimaryThread(), "Async entity add");

        final int chunkX = MathHelper.floor(nmsEntity.locX() / 16.0);
        final int chunkZ = MathHelper.floor(nmsEntity.locZ() / 16.0);

        if (!nmsWorld.isChunkLoaded(chunkX, chunkZ)) {
            // This should never happen
            nmsEntity.dead = true;
            throw new SpawnFailedException(SpawnFailedException.CHUNK_NOT_LOADED);
        }

        nmsWorld.getChunkAt(chunkX, chunkZ).a(nmsEntity);
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
