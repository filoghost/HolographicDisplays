package com.gmail.filoghost.holograms.nms.v1_7_R2;

import net.minecraft.server.v1_7_R2.Entity;
import net.minecraft.server.v1_7_R2.EntityTypes;
import net.minecraft.server.v1_7_R2.WorldServer;

import org.apache.commons.lang.NotImplementedException;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_7_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R2.entity.CraftEntity;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.inventory.ItemStack;

import com.gmail.filoghost.holograms.exception.SpawnFailedException;
import com.gmail.filoghost.holograms.nms.interfaces.BasicEntityNMS;
import com.gmail.filoghost.holograms.nms.interfaces.CustomItem;
import com.gmail.filoghost.holograms.nms.interfaces.FancyMessage;
import com.gmail.filoghost.holograms.nms.interfaces.HologramArmorStand;
import com.gmail.filoghost.holograms.nms.interfaces.HologramComponent;
import com.gmail.filoghost.holograms.nms.interfaces.HologramHorse;
import com.gmail.filoghost.holograms.nms.interfaces.HologramWitherSkull;
import com.gmail.filoghost.holograms.nms.interfaces.NmsManager;
import com.gmail.filoghost.holograms.object.HologramBase;
import com.gmail.filoghost.holograms.utils.ReflectionUtils;
import com.gmail.filoghost.holograms.utils.VersionUtils;

public class NmsManagerImpl implements NmsManager {

	@Override
	public void registerCustomEntities() throws Exception {
		registerCustomEntity(EntityHologramHorse.class, "EntityHorse", 100);
		registerCustomEntity(EntityHologramWitherSkull.class, "WitherSkull", 19);
		registerCustomEntity(EntityCustomItem.class, "Item", 1);
		registerCustomEntity(EntityTouchSlime.class, "Slime", 55);
	}
	
	@SuppressWarnings("rawtypes")
	public void registerCustomEntity(Class entityClass, String name, int id) throws Exception {
		if (VersionUtils.isMCPCOrCauldron()) {
			// MCPC+ / Cauldron entity registration.
			Class<?> entityTypesClass = Class.forName("net.minecraft.server.v1_7_R2.EntityTypes");
			ReflectionUtils.putInPrivateStaticMap(entityTypesClass, "field_75626_c", entityClass, name);
			ReflectionUtils.putInPrivateStaticMap(entityTypesClass, "field_75624_e", entityClass, Integer.valueOf(id));
		} else {
			// Normal entity registration.
			ReflectionUtils.putInPrivateStaticMap(EntityTypes.class, "d", entityClass, name);
			ReflectionUtils.putInPrivateStaticMap(EntityTypes.class, "f", entityClass, Integer.valueOf(id));
		}
	}
	
	@Override
	public HologramHorse spawnHologramHorse(org.bukkit.World world, double x, double y, double z, HologramBase parent) throws SpawnFailedException {
		WorldServer nmsWorld = ((CraftWorld) world).getHandle();
		EntityHologramHorse invisibleHorse = new EntityHologramHorse(nmsWorld);
		invisibleHorse.setParentHologram(parent);
		invisibleHorse.setLocationNMS(x, y, z);
		if (!nmsWorld.addEntity(invisibleHorse, SpawnReason.CUSTOM)) {
			throw new SpawnFailedException();
		}
		return invisibleHorse;
	}
	
	@Override
	public HologramWitherSkull spawnHologramWitherSkull(org.bukkit.World bukkitWorld, double x, double y, double z, HologramBase parent) throws SpawnFailedException {
		WorldServer nmsWorld = ((CraftWorld) bukkitWorld).getHandle();
		EntityHologramWitherSkull staticWitherSkull = new EntityHologramWitherSkull(nmsWorld);
		staticWitherSkull.setParentHologram(parent);
		staticWitherSkull.setLocationNMS(x, y, z);
		if (!nmsWorld.addEntity(staticWitherSkull, SpawnReason.CUSTOM)) {
			throw new SpawnFailedException();
		}
		return staticWitherSkull;
	}

	
	@Override
	public CustomItem spawnCustomItem(org.bukkit.World bukkitWorld, double x, double y, double z, HologramBase parent, ItemStack stack) throws SpawnFailedException {
		WorldServer nmsWorld = ((CraftWorld) bukkitWorld).getHandle();
		EntityCustomItem customItem = new EntityCustomItem(nmsWorld);
		customItem.setParentHologram(parent);
		customItem.setLocationNMS(x, y, z);
		customItem.setItemStackNMS(stack);
		if (!nmsWorld.addEntity(customItem, SpawnReason.CUSTOM)) {
			throw new SpawnFailedException();
		}
		return customItem;
	}
	
	
	@Override
	public EntityTouchSlime spawnTouchSlime(org.bukkit.World bukkitWorld, double x, double y, double z, HologramBase parent) throws SpawnFailedException {
		WorldServer nmsWorld = ((CraftWorld) bukkitWorld).getHandle();
		EntityTouchSlime touchSlime = new EntityTouchSlime(nmsWorld);
		touchSlime.setParentHologram(parent);
		touchSlime.setLocationNMS(x, y, z);
		if (!nmsWorld.addEntity(touchSlime, SpawnReason.CUSTOM)) {
			throw new SpawnFailedException();
		}
		return touchSlime;
	}
	
	

	@Override
	public boolean isHologramComponent(org.bukkit.entity.Entity bukkitEntity) {
		return ((CraftEntity) bukkitEntity).getHandle() instanceof HologramComponent;
	}
	
	@Override
	public boolean isBasicEntityNMS(org.bukkit.entity.Entity bukkitEntity) {
		return ((CraftEntity) bukkitEntity).getHandle() instanceof BasicEntityNMS;
	}

	@Override
	public HologramBase getParentHologram(org.bukkit.entity.Entity bukkitEntity) {
		
		Entity nmsEntity = ((CraftEntity) bukkitEntity).getHandle();
		if (nmsEntity instanceof HologramComponent) {
			return ((HologramComponent) nmsEntity).getParentHologram();
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
	public HologramArmorStand spawnHologramArmorStand(World world, double x, double y, double z, HologramBase parent) throws SpawnFailedException {
		throw new NotImplementedException("Method can only be used on 1.8 or greater");
	}
}
