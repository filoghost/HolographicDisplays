package com.gmail.filoghost.holographicdisplays.nms.v1_8_R2;

import com.gmail.filoghost.holographicdisplays.api.line.HologramLine;
import com.gmail.filoghost.holographicdisplays.api.line.ItemLine;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.ItemPickupManager;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.NMSManager;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.entity.*;
import com.gmail.filoghost.holographicdisplays.util.ReflectionUtils;
import com.gmail.filoghost.holographicdisplays.util.bukkit.BukkitUtils;
import com.gmail.filoghost.holographicdisplays.util.bukkit.BukkitValidator;
import com.gmail.filoghost.holographicdisplays.util.message.FancyMessage;
import net.minecraft.server.v1_8_R2.*;
import org.apache.commons.lang.NotImplementedException;
import org.bukkit.Chunk;
import org.bukkit.craftbukkit.v1_8_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Method;

public class NmsManagerImpl implements NMSManager {

	private Method validateEntityMethod;

	@Override
	public void setup() throws Exception {
		registerCustomEntity(EntityNMSArmorStand.class, "ArmorStand", 30);
		registerCustomEntity(EntityNMSItem.class, "Item", 1);
		registerCustomEntity(EntityNMSSlime.class, "Slime", 55);

		if (!BukkitUtils.isForgeServer()) {
			validateEntityMethod = World.class.getDeclaredMethod("a", Entity.class);
			validateEntityMethod.setAccessible(true);
		}
	}

	@SuppressWarnings("rawtypes")
	public void registerCustomEntity(Class entityClass, String name, int id) throws Exception {
		if (BukkitUtils.isForgeServer()) {
			// Forge entity registration.
			Class<?> entityTypesClass = Class.forName("net.minecraft.server.v1_8_R2.EntityTypes");
			ReflectionUtils.putInPrivateStaticMap(entityTypesClass, "field_75626_c", entityClass, name);
			ReflectionUtils.putInPrivateStaticMap(entityTypesClass, "field_75624_e", entityClass, Integer.valueOf(id));
		} else {
			// Normal entity registration.
			ReflectionUtils.putInPrivateStaticMap(EntityTypes.class, "d", entityClass, name);
			ReflectionUtils.putInPrivateStaticMap(EntityTypes.class, "f", entityClass, Integer.valueOf(id));
		}
	}

	@Override
	public NMSHorse spawnNMSHorse(org.bukkit.World world, double x, double y, double z, HologramLine parentPiece) {
		throw new NotImplementedException("Method can only be used on 1.7 or lower");
	}

	@Override
	public NMSWitherSkull spawnNMSWitherSkull(org.bukkit.World bukkitWorld, double x, double y, double z, HologramLine parentPiece) {
		throw new NotImplementedException("Method can only be used on 1.7 or lower");
	}

	@Override
	public NMSItem spawnNMSItem(org.bukkit.World bukkitWorld, double x, double y, double z, ItemLine parentPiece, ItemStack stack, ItemPickupManager itemPickupManager) {
		WorldServer nmsWorld = ((CraftWorld) bukkitWorld).getHandle();
		EntityNMSItem customItem = new EntityNMSItem(nmsWorld, parentPiece, itemPickupManager);
		customItem.setLocationNMS(x, y, z);
		customItem.setItemStackNMS(stack);
		if (!addEntityToWorld(nmsWorld, customItem)) {
			handleSpawnFail(parentPiece);
		}
		return customItem;
	}

	@Override
	public EntityNMSSlime spawnNMSSlime(org.bukkit.World bukkitWorld, double x, double y, double z, HologramLine parentPiece) {
		WorldServer nmsWorld = ((CraftWorld) bukkitWorld).getHandle();
		EntityNMSSlime touchSlime = new EntityNMSSlime(nmsWorld, parentPiece);
		touchSlime.setLocationNMS(x, y, z);
		if (!addEntityToWorld(nmsWorld, touchSlime)) {
			handleSpawnFail(parentPiece);
		}
		return touchSlime;
	}

	@Override
	public NMSArmorStand spawnNMSArmorStand(org.bukkit.World world, double x, double y, double z, HologramLine parentPiece) {
		WorldServer nmsWorld = ((CraftWorld) world).getHandle();
		EntityNMSArmorStand invisibleArmorStand = new EntityNMSArmorStand(nmsWorld, parentPiece);
		invisibleArmorStand.setLocationNMS(x, y, z);
		if (!addEntityToWorld(nmsWorld, invisibleArmorStand)) {
			handleSpawnFail(parentPiece);
		}
		return invisibleArmorStand;
	}

	private boolean addEntityToWorld(WorldServer nmsWorld, Entity nmsEntity) {
		BukkitValidator.isSync("Async entity add");

		if (validateEntityMethod == null) {
			return nmsWorld.addEntity(nmsEntity, SpawnReason.CUSTOM);
		}

		final int chunkX = MathHelper.floor(nmsEntity.locX / 16.0);
		final int chunkZ = MathHelper.floor(nmsEntity.locZ / 16.0);

		if (!nmsWorld.chunkProviderServer.isChunkLoaded(chunkX, chunkZ)) {
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
	public void sendFancyMessage(FancyMessage message, Player player) {
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a(message.toJson().toString())));
	}

	@Override
	public boolean isUnloadUnsure(Chunk bukkitChunk) {
		return bukkitChunk.getWorld().isChunkInUse(bukkitChunk.getX(), bukkitChunk.getZ());
	}

}
