/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.v1_13_R2;

import me.filoghost.fcommons.Preconditions;
import me.filoghost.fcommons.reflection.ClassToken;
import me.filoghost.fcommons.reflection.ReflectField;
import me.filoghost.fcommons.reflection.ReflectMethod;
import me.filoghost.holographicdisplays.core.DebugLogger;
import me.filoghost.holographicdisplays.core.nms.ChatComponentAdapter;
import me.filoghost.holographicdisplays.core.nms.CustomNameHelper;
import me.filoghost.holographicdisplays.core.nms.NMSCommons;
import me.filoghost.holographicdisplays.core.nms.NMSManager;
import me.filoghost.holographicdisplays.core.nms.PacketController;
import me.filoghost.holographicdisplays.core.nms.entity.NMSArmorStand;
import me.filoghost.holographicdisplays.core.nms.entity.NMSEntityBase;
import me.filoghost.holographicdisplays.core.nms.entity.NMSItem;
import me.filoghost.holographicdisplays.core.hologram.StandardItemLine;
import me.filoghost.holographicdisplays.core.hologram.StandardHologramLine;
import net.minecraft.server.v1_13_R2.ChatComponentText;
import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityTypes;
import net.minecraft.server.v1_13_R2.IChatBaseComponent;
import net.minecraft.server.v1_13_R2.IRegistry;
import net.minecraft.server.v1_13_R2.MathHelper;
import net.minecraft.server.v1_13_R2.RegistryID;
import net.minecraft.server.v1_13_R2.RegistryMaterials;
import net.minecraft.server.v1_13_R2.World;
import net.minecraft.server.v1_13_R2.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_13_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftEntity;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class VersionNMSManager implements NMSManager {
    
    private static final ReflectField<RegistryID<EntityTypes<?>>> REGISTRY_ID_FIELD = ReflectField.lookup(new ClassToken<RegistryID<EntityTypes<?>>>(){}, RegistryMaterials.class, "b");
    private static final ReflectField<Object[]> ID_TO_CLASS_MAP_FIELD = ReflectField.lookup(Object[].class, RegistryID.class, "d");
    private static final ReflectField<List<Entity>> ENTITY_LIST_FIELD = ReflectField.lookup(new ClassToken<List<Entity>>(){}, World.class, "entityList");

    private static final ReflectMethod<?> VALIDATE_ENTITY_METHOD = ReflectMethod.lookup(Object.class, World.class, "b", Entity.class);

    private final PacketController packetController;

    public VersionNMSManager(PacketController packetController) {
        this.packetController = packetController;
    }
    
    @Override
    public void setup() throws Exception {
        registerCustomEntity(EntityNMSSlime.class, 55);
    }
    
    public void registerCustomEntity(Class<? extends Entity> entityClass, int id) throws Exception {
        // Use reflection to get the RegistryID of entities.
        RegistryID<EntityTypes<?>> registryID = REGISTRY_ID_FIELD.get(IRegistry.ENTITY_TYPE);
        Object[] idToClassMap = ID_TO_CLASS_MAP_FIELD.get(registryID);
        
        // Save the the ID -> EntityTypes mapping before the registration.
        Object oldValue = idToClassMap[id];

        // Register the EntityTypes object.
        registryID.a(new EntityTypes<>(entityClass, world -> null, true, true, null), id);

        // Restore the ID -> EntityTypes mapping.
        idToClassMap[id] = oldValue;
    }
    
    @Override
    public NMSItem spawnNMSItem(org.bukkit.World bukkitWorld, double x, double y, double z, StandardItemLine parentPiece, ItemStack stack) {
        WorldServer nmsWorld = ((CraftWorld) bukkitWorld).getHandle();
        EntityNMSItem customItem = new EntityNMSItem(nmsWorld, parentPiece);
        customItem.setLocationNMS(x, y, z);
        customItem.setItemStackNMS(stack);
        if (!addEntityToWorld(nmsWorld, customItem)) {
            DebugLogger.handleSpawnFail(parentPiece);
        }
        return customItem;
    }
    
    @Override
    public EntityNMSSlime spawnNMSSlime(org.bukkit.World bukkitWorld, double x, double y, double z, StandardHologramLine parentPiece) {
        WorldServer nmsWorld = ((CraftWorld) bukkitWorld).getHandle();
        EntityNMSSlime touchSlime = new EntityNMSSlime(nmsWorld, parentPiece);
        touchSlime.setLocationNMS(x, y, z);
        if (!addEntityToWorld(nmsWorld, touchSlime)) {
            DebugLogger.handleSpawnFail(parentPiece);
        }
        return touchSlime;
    }
    
    @Override
    public NMSArmorStand spawnNMSArmorStand(org.bukkit.World world, double x, double y, double z, StandardHologramLine parentPiece) {
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
        
        final int chunkX = MathHelper.floor(nmsEntity.locX / 16.0);
        final int chunkZ = MathHelper.floor(nmsEntity.locZ / 16.0);
        
        if (!nmsWorld.isChunkLoaded(chunkX, chunkZ, true)) { // The boolean "true" is currently unused
            // This should never happen
            nmsEntity.dead = true;
            return false;
        }
        
        nmsWorld.getChunkAt(chunkX, chunkZ).a(nmsEntity);
        if (NMSCommons.isPaperServer()) {
            try {
                // Workaround because nmsWorld.entityList is a different class in Paper, if used without reflection it throws NoSuchFieldError.
                ENTITY_LIST_FIELD.get(nmsWorld).add(nmsEntity);
            } catch (ReflectiveOperationException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            nmsWorld.entityList.add(nmsEntity);
        }
        
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
        return CustomNameHelper.replaceCustomNameChatComponent(NMSChatComponentAdapter.INSTANCE, customNameObject, target, replacement);
    }
    
    private enum NMSChatComponentAdapter implements ChatComponentAdapter<IChatBaseComponent> {

        INSTANCE {
            
            @Override
            public ChatComponentText cast(Object chatComponentObject) {
                return (ChatComponentText) chatComponentObject;
            }
            
            @Override
            public String getText(IChatBaseComponent chatComponent) {
                return chatComponent.getText();
            }
    
            @Override
            public List<IChatBaseComponent> getSiblings(IChatBaseComponent chatComponent) {
                return chatComponent.a();
            }
    
            @Override
            public void addSibling(IChatBaseComponent chatComponent, IChatBaseComponent newSibling) {
                chatComponent.addSibling(newSibling);
            }
    
            @Override
            public ChatComponentText cloneComponent(IChatBaseComponent chatComponent, String newText) {
                ChatComponentText clonedChatComponent = new ChatComponentText(newText);
                clonedChatComponent.setChatModifier(chatComponent.getChatModifier().clone());
                return clonedChatComponent;
            }
            
        }
        
    }
    
}
