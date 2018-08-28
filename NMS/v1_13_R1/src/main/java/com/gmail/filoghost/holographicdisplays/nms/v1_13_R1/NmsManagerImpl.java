package com.gmail.filoghost.holographicdisplays.nms.v1_13_R1;

import java.lang.reflect.Method;
import java.util.List;
import java.util.function.Function;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_13_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_13_R1.entity.CraftEntity;
import org.bukkit.inventory.ItemStack;

import com.gmail.filoghost.holographicdisplays.api.line.HologramLine;
import com.gmail.filoghost.holographicdisplays.api.line.ItemLine;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.FancyMessage;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.ItemPickupManager;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.NMSManager;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.entity.NMSArmorStand;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.entity.NMSEntityBase;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.entity.NMSItem;
import com.gmail.filoghost.holographicdisplays.util.ConsoleLogger;
import com.gmail.filoghost.holographicdisplays.util.Validator;
import com.gmail.filoghost.holographicdisplays.util.VersionUtils;
import com.gmail.filoghost.holographicdisplays.util.reflection.ReflectField;

import net.minecraft.server.v1_13_R1.Entity;
import net.minecraft.server.v1_13_R1.EntityTypes;
import net.minecraft.server.v1_13_R1.MathHelper;
import net.minecraft.server.v1_13_R1.RegistryID;
import net.minecraft.server.v1_13_R1.RegistryMaterials;
import net.minecraft.server.v1_13_R1.World;
import net.minecraft.server.v1_13_R1.WorldServer;

public class NmsManagerImpl implements NMSManager {
	
	private static final ReflectField<RegistryID<EntityTypes<?>>> REGISTRY_ID_FIELD = new ReflectField<RegistryID<EntityTypes<?>>>(RegistryMaterials.class, "a");
	private static final ReflectField<Object[]> ID_TO_CLASS_MAP_FIELD = new ReflectField<Object[]>(RegistryID.class, "d");
	private static final ReflectField<List<Entity>> ENTITY_LIST_FIELD = new ReflectField<List<Entity>>(World.class, "entityList");

	private Method validateEntityMethod;
	
	@Override
	public void setup() throws Exception {
		validateEntityMethod = World.class.getDeclaredMethod("b", Entity.class);
		validateEntityMethod.setAccessible(true);
		
		registerCustomEntity(EntityNMSSlime.class, 55);
	}
	
	public void registerCustomEntity(Class<? extends Entity> entityClass, int id) throws Exception {
		// Use reflection to get the RegistryID of entities.
		RegistryID<EntityTypes<?>> registryID = REGISTRY_ID_FIELD.get(EntityTypes.REGISTRY);
		Object[] idToClassMap = ID_TO_CLASS_MAP_FIELD.get(registryID);
		
		// Save the the ID -> EntityTypes mapping before the registration.
		Object oldValue = idToClassMap[id];

		// Register the EntityTypes object.
		registryID.a(new EntityTypes<Entity>(entityClass, new Function<World, Entity>() {

			@Override
			public Entity apply(World world) {
				return null;
			}
			
		}, true, true, null), id);

		// Restore the ID -> EntityTypes mapping.
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
        if (VersionUtils.isPaperServer()) {
        	try {
        		// Workaround because nmsWorld.entityList is a different class in Paper, if used without reflection it throws NoSuchFieldError.
				ENTITY_LIST_FIELD.get(nmsWorld).add(nmsEntity);
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
        } else {
        	nmsWorld.entityList.add(nmsEntity);
        }
        
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
	public FancyMessage newFancyMessage(String text) {
		return new FancyMessageImpl(text);
	}
	
}
