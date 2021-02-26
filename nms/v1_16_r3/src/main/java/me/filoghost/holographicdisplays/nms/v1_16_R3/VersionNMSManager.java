/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.v1_16_R3;

import me.filoghost.fcommons.Preconditions;
import me.filoghost.fcommons.reflection.ClassToken;
import me.filoghost.fcommons.reflection.ReflectField;
import me.filoghost.fcommons.reflection.ReflectMethod;
import me.filoghost.holographicdisplays.api.line.HologramLine;
import me.filoghost.holographicdisplays.api.line.ItemLine;
import me.filoghost.holographicdisplays.common.DebugLogger;
import me.filoghost.holographicdisplays.nms.interfaces.ChatComponentAdapter;
import me.filoghost.holographicdisplays.nms.interfaces.CustomNameHelper;
import me.filoghost.holographicdisplays.nms.interfaces.ItemPickupManager;
import me.filoghost.holographicdisplays.nms.interfaces.NMSManager;
import me.filoghost.holographicdisplays.nms.interfaces.PacketController;
import me.filoghost.holographicdisplays.nms.interfaces.entity.NMSArmorStand;
import me.filoghost.holographicdisplays.nms.interfaces.entity.NMSEntityBase;
import me.filoghost.holographicdisplays.nms.interfaces.entity.NMSItem;
import net.minecraft.server.v1_16_R3.ChatComponentText;
import net.minecraft.server.v1_16_R3.Entity;
import net.minecraft.server.v1_16_R3.EntityTypes;
import net.minecraft.server.v1_16_R3.EnumCreatureType;
import net.minecraft.server.v1_16_R3.IChatBaseComponent;
import net.minecraft.server.v1_16_R3.IRegistry;
import net.minecraft.server.v1_16_R3.MathHelper;
import net.minecraft.server.v1_16_R3.RegistryMaterials;
import net.minecraft.server.v1_16_R3.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

public class VersionNMSManager implements NMSManager {
    
    private static final ReflectField<Map<EntityTypes<?>, Integer>> REGISTRY_TO_ID_FIELD = ReflectField.lookup(new ClassToken<Map<EntityTypes<?>, Integer>>(){}, RegistryMaterials.class, "bg");
    private static final ReflectMethod<Void> REGISTER_ENTITY_METHOD = ReflectMethod.lookup(void.class, WorldServer.class, "registerEntity", Entity.class);

    private final ItemPickupManager itemPickupManager;
    private final PacketController packetController;

    public VersionNMSManager(ItemPickupManager itemPickupManager, PacketController packetController) {
        this.itemPickupManager = itemPickupManager;
        this.packetController = packetController;
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
    public NMSItem spawnNMSItem(World bukkitWorld, double x, double y, double z, ItemLine parentPiece, ItemStack stack) {
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
    public NMSArmorStand spawnNMSArmorStand(World world, double x, double y, double z, HologramLine parentPiece) {
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
        
        final int chunkX = MathHelper.floor(nmsEntity.locX() / 16.0);
        final int chunkZ = MathHelper.floor(nmsEntity.locZ() / 16.0);
        
        if (!nmsWorld.isChunkLoaded(chunkX, chunkZ)) {
            // This should never happen
            nmsEntity.dead = true;
            return false;
        }
        
        nmsWorld.getChunkAt(chunkX, chunkZ).a(nmsEntity);
        try {
            REGISTER_ENTITY_METHOD.invoke(nmsWorld, nmsEntity);
            return true;
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
            return false;
        }
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
                return chatComponent.getSiblings();
            }
    
            @Override
            public void addSibling(IChatBaseComponent chatComponent, IChatBaseComponent newSibling) {
                newSibling.getChatModifier().setChatModifier(chatComponent.getChatModifier());
                chatComponent.getSiblings().add(newSibling);
            }
    
            @Override
            public ChatComponentText cloneComponent(IChatBaseComponent chatComponent, String newText) {
                ChatComponentText clonedChatComponent = new ChatComponentText(newText);
                clonedChatComponent.setChatModifier(chatComponent.getChatModifier().a());
                return clonedChatComponent;
            }
            
        }
        
    }
    
}
