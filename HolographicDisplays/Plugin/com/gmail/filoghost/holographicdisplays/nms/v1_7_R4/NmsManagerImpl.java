package com.gmail.filoghost.holographicdisplays.nms.v1_7_R4;

import net.minecraft.server.v1_7_R4.Entity;
import net.minecraft.server.v1_7_R4.EntityTypes;
import net.minecraft.server.v1_7_R4.WorldServer;

import org.apache.commons.lang.NotImplementedException;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftEntity;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.inventory.ItemStack;

import com.gmail.filoghost.holographicdisplays.nms.interfaces.FancyMessage;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.NMSManager;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.entity.NMSArmorStand;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.entity.NMSEntityBase;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.entity.NMSHorse;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.entity.NMSItem;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.entity.NMSWitherSkull;
import com.gmail.filoghost.holographicdisplays.object.line.CraftHologramLine;
import com.gmail.filoghost.holographicdisplays.object.line.CraftItemLine;
import com.gmail.filoghost.holographicdisplays.object.line.CraftTouchSlimeLine;
import com.gmail.filoghost.holographicdisplays.util.DebugHandler;
import com.gmail.filoghost.holographicdisplays.util.ReflectionUtils;
import com.gmail.filoghost.holographicdisplays.util.VersionUtils;

public class NmsManagerImpl implements NMSManager {

	@Override
	public void registerCustomEntities() throws Exception {
		registerCustomEntity(EntityNMSHorse.class, "EntityHorse", 100);
		registerCustomEntity(EntityNMSWitherSkull.class, "WitherSkull", 19);
		registerCustomEntity(EntityNMSItem.class, "Item", 1);
		registerCustomEntity(EntityNMSSlime.class, "Slime", 55);
	}
	
	@SuppressWarnings("rawtypes")
	public void registerCustomEntity(Class entityClass, String name, int id) throws Exception {
		if (VersionUtils.isMCPCOrCauldron()) {
			// MCPC+ / Cauldron entity registration.
			Class<?> entityTypesClass = Class.forName("net.minecraft.server.v1_7_R4.EntityTypes");
			ReflectionUtils.putInPrivateStaticMap(entityTypesClass, "field_75626_c", entityClass, name);
			ReflectionUtils.putInPrivateStaticMap(entityTypesClass, "field_75624_e", entityClass, Integer.valueOf(id));
		} else {
			// Normal entity registration.
			ReflectionUtils.putInPrivateStaticMap(EntityTypes.class, "d", entityClass, name);
			ReflectionUtils.putInPrivateStaticMap(EntityTypes.class, "f", entityClass, Integer.valueOf(id));
		}
	}
	
	@Override
	public NMSHorse spawnNMSHorse(org.bukkit.World world, double x, double y, double z, CraftHologramLine parentPiece) {
		WorldServer nmsWorld = ((CraftWorld) world).getHandle();
		EntityNMSHorse invisibleHorse = new EntityNMSHorse(nmsWorld, parentPiece);
		invisibleHorse.setLocationNMS(x, y, z);
		if (!nmsWorld.addEntity(invisibleHorse, SpawnReason.CUSTOM)) {
			DebugHandler.handleSpawnFail(parentPiece);
		}
		return invisibleHorse;
	}
	
	@Override
	public NMSWitherSkull spawnNMSWitherSkull(org.bukkit.World bukkitWorld, double x, double y, double z, CraftHologramLine parentPiece) {
		WorldServer nmsWorld = ((CraftWorld) bukkitWorld).getHandle();
		EntityNMSWitherSkull staticWitherSkull = new EntityNMSWitherSkull(nmsWorld, parentPiece);
		staticWitherSkull.setLocationNMS(x, y, z);
		if (!nmsWorld.addEntity(staticWitherSkull, SpawnReason.CUSTOM)) {
			DebugHandler.handleSpawnFail(parentPiece);
		}
		return staticWitherSkull;
	}
	
	@Override
	public NMSItem spawnNMSItem(org.bukkit.World bukkitWorld, double x, double y, double z, CraftItemLine parentPiece, ItemStack stack) {
		WorldServer nmsWorld = ((CraftWorld) bukkitWorld).getHandle();
		EntityNMSItem customItem = new EntityNMSItem(nmsWorld, parentPiece);
		customItem.setLocationNMS(x, y, z);
		customItem.setItemStackNMS(stack);
		if (!nmsWorld.addEntity(customItem, SpawnReason.CUSTOM)) {
			DebugHandler.handleSpawnFail(parentPiece);
		}
		return customItem;
	}
	
	@Override
	public EntityNMSSlime spawnNMSSlime(org.bukkit.World bukkitWorld, double x, double y, double z, CraftTouchSlimeLine parentPiece) {
		WorldServer nmsWorld = ((CraftWorld) bukkitWorld).getHandle();
		EntityNMSSlime touchSlime = new EntityNMSSlime(nmsWorld, parentPiece);
		touchSlime.setLocationNMS(x, y, z);
		if (!nmsWorld.addEntity(touchSlime, SpawnReason.CUSTOM)) {
			DebugHandler.handleSpawnFail(parentPiece);
		}
		return touchSlime;
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

	@Override
	public boolean hasChatHoverFeature() {
		return true;
	}

	@Override
	public NMSArmorStand spawnNMSArmorStand(World world, double x, double y, double z, CraftHologramLine parentPiece) {
		throw new NotImplementedException("Method can only be used on 1.8 or higher");
	}
}
