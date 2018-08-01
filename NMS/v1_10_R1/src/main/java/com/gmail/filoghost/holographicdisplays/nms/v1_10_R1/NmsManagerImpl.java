package com.gmail.filoghost.holographicdisplays.nms.v1_10_R1;

import com.gmail.filoghost.holographicdisplays.api.line.HologramLine;
import com.gmail.filoghost.holographicdisplays.api.line.ItemLine;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.ItemPickupManager;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.NMSManager;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.entity.NMSArmorStand;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.entity.NMSEntityBase;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.entity.NMSItem;
import com.gmail.filoghost.holographicdisplays.util.ReflectionUtils;
import com.gmail.filoghost.holographicdisplays.util.bukkit.BukkitUtils;
import com.gmail.filoghost.holographicdisplays.util.bukkit.BukkitValidator;
import com.gmail.filoghost.holographicdisplays.util.message.FancyMessage;
import net.minecraft.server.v1_10_R1.*;
import org.bukkit.craftbukkit.v1_10_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Method;

public class NmsManagerImpl implements NMSManager {

	private Method validateEntityMethod;

	@Override
	public void setup() throws Exception {
		registerCustomEntity(EntityNMSArmorStand.class, "ArmorStand", 30);
		registerCustomEntity(EntityNMSItem.class, "Item", 1);
		registerCustomEntity(EntityNMSSlime.class, "Slime", 55);

		validateEntityMethod = World.class.getDeclaredMethod("b", Entity.class);
		validateEntityMethod.setAccessible(true);
	}

	@SuppressWarnings("rawtypes")
	public void registerCustomEntity(Class entityClass, String name, int id) throws Exception {
		if (BukkitUtils.isForgeServer()) {
			throw new UnsupportedOperationException("Forge based servers are not supported");
		} else {
			// Normal entity registration.
			ReflectionUtils.putInPrivateStaticMap(EntityTypes.class, "d", entityClass, name);
			ReflectionUtils.putInPrivateStaticMap(EntityTypes.class, "f", entityClass, Integer.valueOf(id));
		}
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
	public void sendFancyMessage(FancyMessage message, Player player) {
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a(message.toJson().toString())));
	}
}
