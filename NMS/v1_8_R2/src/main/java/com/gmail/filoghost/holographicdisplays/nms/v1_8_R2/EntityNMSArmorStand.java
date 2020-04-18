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
package com.gmail.filoghost.holographicdisplays.nms.v1_8_R2;

import java.util.logging.Level;

import org.bukkit.craftbukkit.v1_8_R2.entity.CraftEntity;

import com.gmail.filoghost.holographicdisplays.api.line.HologramLine;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.entity.NMSArmorStand;
import com.gmail.filoghost.holographicdisplays.util.ConsoleLogger;
import com.gmail.filoghost.holographicdisplays.util.Utils;
import com.gmail.filoghost.holographicdisplays.util.reflection.ReflectField;
import com.gmail.filoghost.holographicdisplays.util.reflection.ReflectMethod;
import net.minecraft.server.v1_8_R2.AxisAlignedBB;
import net.minecraft.server.v1_8_R2.DamageSource;
import net.minecraft.server.v1_8_R2.EntityArmorStand;
import net.minecraft.server.v1_8_R2.EntityHuman;
import net.minecraft.server.v1_8_R2.EntityPlayer;
import net.minecraft.server.v1_8_R2.ItemStack;
import net.minecraft.server.v1_8_R2.NBTTagCompound;
import net.minecraft.server.v1_8_R2.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_8_R2.Vec3D;
import net.minecraft.server.v1_8_R2.World;

public class EntityNMSArmorStand extends EntityArmorStand implements NMSArmorStand {
	
	private static final ReflectField<Integer> DISABLED_SLOTS_FIELD = new ReflectField<>(EntityArmorStand.class, "bi");
	private static final ReflectMethod<Void> SET_MARKER_METHOD = new ReflectMethod<>(EntityArmorStand.class, "n", boolean.class);

	private HologramLine parentPiece;
	private String customName;
	
	public EntityNMSArmorStand(World world, HologramLine parentPiece) {
		super(world);
		setInvisible(true);
		setSmall(true);
		setArms(false);
		setGravity(true);
		setBasePlate(true);
		try {
			SET_MARKER_METHOD.invoke(this, true);
		} catch (Throwable t) {
			ConsoleLogger.logDebug(Level.SEVERE, "Couldn't set armor stand as marker", t);
			// It will still work, but the offset will be wrong.
		}
		this.parentPiece = parentPiece;
		try {
			DISABLED_SLOTS_FIELD.set(this, Integer.MAX_VALUE);
		} catch (Exception e) {
			// There's still the overridden method.
		}
		forceSetBoundingBox(new NullBoundingBox());
		
		this.onGround = true; // Workaround to force EntityTrackerEntry to send a teleport packet.
	}
	
	@Override
	public void t_() {
		// Disable normal ticking for this entity.
		
		// Workaround to force EntityTrackerEntry to send a teleport packet immediately after spawning this entity.
		if (this.onGround) {
			this.onGround = false;
		}
	}
	
	@Override
	public void inactiveTick() {
		// Disable normal ticking for this entity.
		
		// Workaround to force EntityTrackerEntry to send a teleport packet immediately after spawning this entity.
		if (this.onGround) {
			this.onGround = false;
		}
	}
	
	@Override
	public void b(NBTTagCompound nbttagcompound) {
		// Do not save NBT.
	}
	
	@Override
	public boolean c(NBTTagCompound nbttagcompound) {
		// Do not save NBT.
		return false;
	}

	@Override
	public boolean d(NBTTagCompound nbttagcompound) {
		// Do not save NBT.
		return false;
	}
	
	@Override
	public void e(NBTTagCompound nbttagcompound) {
		// Do not save NBT.
	}
	
	
	@Override
	public boolean isInvulnerable(DamageSource source) {
		/*
		 * The field Entity.invulnerable is private.
		 * It's only used while saving NBTTags, but since the entity would be killed
		 * on chunk unload, we prefer to override isInvulnerable().
		 */
	    return true;
	}
	
	@Override
	public void setCustomName(String customName) {
		// Locks the custom name.
	}
	
	@Override
	public void setCustomNameVisible(boolean visible) {
		// Locks the custom name.
	}

	@Override
	public boolean a(EntityHuman human, Vec3D vec3d) {
		// Prevent stand being equipped
		return true;
	}

	@Override
	public boolean d(int i, ItemStack item) {
		// Prevent stand being equipped
		return false;
	}

	@Override
	public void setEquipment(int i, ItemStack item) {
		// Prevent stand being equipped
	}
	
	@Override
	public void a(AxisAlignedBB boundingBox) {
		// Do not change it!
	}
	
	public void forceSetBoundingBox(AxisAlignedBB boundingBox) {
		super.a(boundingBox);
	}
	
	@Override
	public void makeSound(String sound, float f1, float f2) {
	    // Remove sounds.
	}
	
	@Override
	public void setCustomNameNMS(String name) {
		this.customName = Utils.limitLength(name, 300);
		super.setCustomName(customName);
		super.setCustomNameVisible(customName != null && !customName.isEmpty());
	}
	
	@Override
	public String getCustomNameStringNMS() {
		return this.customName;
	}
	
	@Override
	public Object getCustomNameObjectNMS() {
		return super.getCustomName();
	}
	
	@Override
	public void die() {
		// Prevent being killed.
	}
	
	@Override
	public CraftEntity getBukkitEntity() {
		if (super.bukkitEntity == null) {
			this.bukkitEntity = new CraftNMSArmorStand(this.world.getServer(), this);
	    }
		return this.bukkitEntity;
	}
	
	@Override
	public void killEntityNMS() {
		super.dead = true;
	}
	
	@Override
	public void setLocationNMS(double x, double y, double z, boolean broadcastLocationPacket) {
		super.setPosition(x, y, z);
		if (broadcastLocationPacket) {
			broadcastLocationPacketNMS();
		}
	}
	
	private void broadcastLocationPacketNMS() {
		PacketPlayOutEntityTeleport teleportPacket = new PacketPlayOutEntityTeleport(this);
	
		for (Object obj : this.world.players) {
			if (obj instanceof EntityPlayer) {
				EntityPlayer nmsPlayer = (EntityPlayer) obj;

				double distanceSquared = Utils.square(nmsPlayer.locX - this.locX) + Utils.square(nmsPlayer.locZ - this.locZ);
				if (distanceSquared < 8192 && nmsPlayer.playerConnection != null) {
					nmsPlayer.playerConnection.sendPacket(teleportPacket);
				}
			}
		}
	}

	@Override
	public boolean isDeadNMS() {
		return this.dead;
	}
	
	@Override
	public int getIdNMS() {
		return super.getId();
	}

	@Override
	public HologramLine getHologramLine() {
		return parentPiece;
	}
	
	@Override
	public org.bukkit.entity.Entity getBukkitEntityNMS() {
		return getBukkitEntity();
	}
}
