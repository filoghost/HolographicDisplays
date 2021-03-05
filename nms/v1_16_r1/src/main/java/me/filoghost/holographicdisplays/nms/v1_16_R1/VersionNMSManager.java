/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.v1_16_R1;

import me.filoghost.fcommons.Preconditions;
import me.filoghost.fcommons.reflection.ClassToken;
import me.filoghost.fcommons.reflection.ReflectField;
import me.filoghost.fcommons.reflection.ReflectMethod;
import me.filoghost.holographicdisplays.core.hologram.StandardHologramLine;
import me.filoghost.holographicdisplays.core.hologram.StandardItemLine;
import me.filoghost.holographicdisplays.core.nms.ChatComponentCustomNameEditor;
import me.filoghost.holographicdisplays.core.nms.CustomNameEditor;
import me.filoghost.holographicdisplays.core.nms.NMSManager;
import me.filoghost.holographicdisplays.core.nms.PacketController;
import me.filoghost.holographicdisplays.core.nms.SpawnFailedException;
import me.filoghost.holographicdisplays.core.nms.entity.NMSArmorStand;
import me.filoghost.holographicdisplays.core.nms.entity.NMSEntity;
import me.filoghost.holographicdisplays.core.nms.entity.NMSItem;
import net.minecraft.server.v1_16_R1.ChatComponentText;
import net.minecraft.server.v1_16_R1.Entity;
import net.minecraft.server.v1_16_R1.EntityTypes;
import net.minecraft.server.v1_16_R1.EnumCreatureType;
import net.minecraft.server.v1_16_R1.IChatBaseComponent;
import net.minecraft.server.v1_16_R1.IRegistry;
import net.minecraft.server.v1_16_R1.MathHelper;
import net.minecraft.server.v1_16_R1.RegistryID;
import net.minecraft.server.v1_16_R1.RegistryMaterials;
import net.minecraft.server.v1_16_R1.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_16_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R1.entity.CraftEntity;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class VersionNMSManager implements NMSManager {
    
    private static final ReflectField<RegistryID<EntityTypes<?>>> REGISTRY_ID_FIELD = ReflectField.lookup(new ClassToken<RegistryID<EntityTypes<?>>>(){}, RegistryMaterials.class, "b");
    private static final ReflectField<Object[]> ID_TO_CLASS_MAP_FIELD = ReflectField.lookup(Object[].class, RegistryID.class, "d");
    private static final ReflectMethod<Void> REGISTER_ENTITY_METHOD = ReflectMethod.lookup(void.class, WorldServer.class, "registerEntity", Entity.class);

    private final PacketController packetController;

    public VersionNMSManager(PacketController packetController) {
        this.packetController = packetController;
    }
    
    @Override
    public void setup() throws Exception {        
        registerCustomEntity(EntityNMSSlime.class, 55, 2.04f, 2.04f);
    }
    
    public void registerCustomEntity(Class<? extends Entity> entityClass, int id, float sizeWidth, float sizeHeight) throws Exception {
        // Use reflection to get the RegistryID of entities.
        RegistryID<EntityTypes<?>> registryID = REGISTRY_ID_FIELD.get(IRegistry.ENTITY_TYPE);
        Object[] idToClassMap = ID_TO_CLASS_MAP_FIELD.get(registryID);
        
        // Save the the ID -> EntityTypes mapping before the registration.
        Object oldValue = idToClassMap[id];

        // Register the EntityTypes object.
        registryID.a(EntityTypes.Builder.a(EnumCreatureType.MONSTER).a(sizeWidth, sizeHeight).b().a((String) null), id);

        // Restore the ID -> EntityTypes mapping.
        idToClassMap[id] = oldValue;
    }
    
    @Override
    public NMSItem spawnNMSItem(World bukkitWorld, double x, double y, double z, StandardItemLine parentPiece, ItemStack stack) throws SpawnFailedException {
        WorldServer nmsWorld = ((CraftWorld) bukkitWorld).getHandle();
        EntityNMSItem item = new EntityNMSItem(nmsWorld, parentPiece);
        item.setLocationNMS(x, y, z);
        item.setItemStackNMS(stack);
        addEntityToWorld(nmsWorld, item);
        return item;
    }
    
    @Override
    public EntityNMSSlime spawnNMSSlime(World bukkitWorld, double x, double y, double z, StandardHologramLine parentPiece) throws SpawnFailedException {
        WorldServer nmsWorld = ((CraftWorld) bukkitWorld).getHandle();
        EntityNMSSlime slime = new EntityNMSSlime(nmsWorld, parentPiece);
        slime.setLocationNMS(x, y, z);
        addEntityToWorld(nmsWorld, slime);
        return slime;
    }
    
    @Override
    public NMSArmorStand spawnNMSArmorStand(World world, double x, double y, double z, StandardHologramLine parentPiece) throws SpawnFailedException {
        WorldServer nmsWorld = ((CraftWorld) world).getHandle();
        EntityNMSArmorStand armorStand = new EntityNMSArmorStand(nmsWorld, parentPiece, packetController);
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
            return ((NMSEntity) nmsEntity);
        } else {
            return null;
        }
    }
    
    @Override
    public NMSEntity getNMSEntityBaseFromID(org.bukkit.World bukkitWorld, int entityID) {
        WorldServer nmsWorld = ((CraftWorld) bukkitWorld).getHandle();
        Entity nmsEntity = nmsWorld.getEntity(entityID);
        
        if (nmsEntity instanceof NMSEntity) {
            return ((NMSEntity) nmsEntity);
        } else {
            return null;
        }
    }
    
    @Override
    public CustomNameEditor getCustomNameEditor() {
        return VersionChatComponentCustomNameEditor.INSTANCE;
    }
    
    private enum VersionChatComponentCustomNameEditor implements ChatComponentCustomNameEditor<IChatBaseComponent> {

        INSTANCE;

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
