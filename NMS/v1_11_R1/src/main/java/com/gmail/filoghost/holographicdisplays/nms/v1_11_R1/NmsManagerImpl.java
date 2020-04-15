/*
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package com.gmail.filoghost.holographicdisplays.nms.v1_11_R1;

import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_11_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftEntity;
import org.bukkit.inventory.ItemStack;

import com.gmail.filoghost.holographicdisplays.api.line.HologramLine;
import com.gmail.filoghost.holographicdisplays.api.line.ItemLine;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.ItemPickupManager;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.CustomNameHelper;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.NMSManager;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.entity.NMSArmorStand;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.entity.NMSEntityBase;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.entity.NMSItem;
import com.gmail.filoghost.holographicdisplays.util.ConsoleLogger;
import com.gmail.filoghost.holographicdisplays.util.Validator;
import com.gmail.filoghost.holographicdisplays.util.reflection.ReflectField;

import net.minecraft.server.v1_11_R1.Entity;
import net.minecraft.server.v1_11_R1.EntityTypes;
import net.minecraft.server.v1_11_R1.MathHelper;
import net.minecraft.server.v1_11_R1.RegistryID;
import net.minecraft.server.v1_11_R1.RegistryMaterials;
import net.minecraft.server.v1_11_R1.World;
import net.minecraft.server.v1_11_R1.WorldServer;

public class NmsManagerImpl implements NMSManager {
	
	private static final ReflectField<RegistryID<Class<? extends Entity>>> REGISTRY_ID_FIELD = new ReflectField<>(RegistryMaterials.class, "a");
	private static final ReflectField<Object[]> ID_TO_CLASS_MAP_FIELD = new ReflectField<>(RegistryID.class, "d");

	private Method validateEntityMethod;
	
	@Override
	public void setup() throws Exception {
		validateEntityMethod = World.class.getDeclaredMethod("b", Entity.class);
		validateEntityMethod.setAccessible(true);
		
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
			ConsoleLogger.handleSpawnFail(parentPiece);
		}
		return customItem;
	}
	
	@Override
	public EntityNMSSlime spawnNMSSlime(org.bukkit.World bukkitWorld, double x, double y, double z, HologramLine parentPiece) {
		WorldServer nmsWorld = ((CraftWorld) bukkitWorld).getHandle();
		EntityNMSSlime touchSlime = new EntityNMSSlime(nmsWorld, parentPiece);
		touchSlime.setLocationNMS(x, y, z);
		if (!addEntityToWorld(nmsWorld, touchSlime)) {
			ConsoleLogger.handleSpawnFail(parentPiece);
		}
		return touchSlime;
	}
	
	@Override
	public NMSArmorStand spawnNMSArmorStand(org.bukkit.World world, double x, double y, double z, HologramLine parentPiece) {
		WorldServer nmsWorld = ((CraftWorld) world).getHandle();
		EntityNMSArmorStand invisibleArmorStand = new EntityNMSArmorStand(nmsWorld, parentPiece);
		invisibleArmorStand.setLocationNMS(x, y, z);
		if (!addEntityToWorld(nmsWorld, invisibleArmorStand)) {
			ConsoleLogger.handleSpawnFail(parentPiece);
		}
		return invisibleArmorStand;
	}
	
	private boolean addEntityToWorld(WorldServer nmsWorld, Entity nmsEntity) {
		Validator.isTrue(Bukkit.isPrimaryThread(), "Async entity add");
		
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
			validateEntityMethod.invoke(nmsWorld, nmsEntity);
		} catch (Exception e) {
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
		}

		return null;
	}
	
	@Override
	public org.bukkit.entity.Entity getEntityFromID(org.bukkit.World bukkitWorld, int entityID) {
		WorldServer nmsWorld = ((CraftWorld) bukkitWorld).getHandle();
		Entity nmsEntity = nmsWorld.getEntity(entityID);
		
		if (nmsEntity == null) {
			return null;
		}
		
		return nmsEntity.getBukkitEntity();
	}
	
	@Override
	public Object replaceCustomNameText(Object customNameObject, String target, String replacement) {
		return CustomNameHelper.replaceCustomNameString(customNameObject, target, replacement);
	}

}
