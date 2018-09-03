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
package com.gmail.filoghost.holographicdisplays.nms.v1_8_R1;

import org.bukkit.craftbukkit.v1_8_R1.entity.CraftEntity;

import com.gmail.filoghost.holographicdisplays.api.line.HologramLine;
import com.gmail.filoghost.holographicdisplays.disk.Configuration;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.entity.NMSArmorStand;
import com.gmail.filoghost.holographicdisplays.util.Utils;
import com.gmail.filoghost.holographicdisplays.util.reflection.ReflectField;
import com.gmail.filoghost.holographicdisplays.util.reflection.ReflectionUtils;

import net.minecraft.server.v1_8_R1.AxisAlignedBB;
import net.minecraft.server.v1_8_R1.DamageSource;
import net.minecraft.server.v1_8_R1.EntityArmorStand;
import net.minecraft.server.v1_8_R1.EntityHuman;
import net.minecraft.server.v1_8_R1.EntityPlayer;
import net.minecraft.server.v1_8_R1.ItemStack;
import net.minecraft.server.v1_8_R1.MathHelper;
import net.minecraft.server.v1_8_R1.NBTTagCompound;
import net.minecraft.server.v1_8_R1.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_8_R1.Vec3D;
import net.minecraft.server.v1_8_R1.World;

public class EntityNMSArmorStand extends EntityArmorStand implements NMSArmorStand {

	private static final ReflectField<Integer> DISABLED_SLOTS_FIELD = new ReflectField<Integer>(EntityArmorStand.class, "bg");
	
	private boolean lockTick;
	private HologramLine parentPiece;
	
	public EntityNMSArmorStand(World world, HologramLine parentPiece) {
		super(world);
		setInvisible(true);
		setSmall(true);
		setArms(false);
		setGravity(true);
		setBasePlate(true);
		// There is no "Marker" tag in v1_8_R1
		this.parentPiece = parentPiece;
		try {
			DISABLED_SLOTS_FIELD.set(this, Integer.MAX_VALUE);
		} catch (Exception e) {
			// There's still the overridden method.
		}
		forceSetBoundingBox(new NullBoundingBox());
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
	public int getId() {
		if (Configuration.preciseHologramMovement) {
			StackTraceElement element = ReflectionUtils.getStackTraceElement(2);
			if (element != null && element.getFileName() != null && element.getFileName().equals("EntityTrackerEntry.java") && element.getLineNumber() > 137 && element.getLineNumber() < 147) {
				// Then this method is being called when creating a new packet, we return a fake ID!
				return -1;
			}
		}
		
		return super.getId();
	}

	@Override
	public void s_() {
		if (!lockTick) {
			super.s_();
		}
	}
	
	@Override
	public void makeSound(String sound, float f1, float f2) {
	    // Remove sounds.
	}
	
	@Override
	public void setCustomNameNMS(String name) {
		if (name != null && name.length() > 300) {
			name = name.substring(0, 300);
		}
		super.setCustomName(name);
		super.setCustomNameVisible(name != null && !name.isEmpty());
	}
	
	@Override
	public String getCustomNameNMS() {
		return super.getCustomName();
	}

	@Override
	public void setLockTick(boolean lock) {
		lockTick = lock;
	}
	
	@Override
	public void die() {
		setLockTick(false);
		super.die();
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
		die();
	}
	
	@Override
	public void setLocationNMS(double x, double y, double z) {
		super.setPosition(x, y, z);
		
		if (Configuration.preciseHologramMovement) {
			// Send a packet near to update the position.
			PacketPlayOutEntityTeleport teleportPacket = new PacketPlayOutEntityTeleport(
				getIdNMS(),
				MathHelper.floor(this.locX * 32.0D),
				MathHelper.floor(this.locY * 32.0D),
				MathHelper.floor(this.locZ * 32.0D),
				(byte) (int) (this.yaw * 256.0F / 360.0F),
				(byte) (int) (this.pitch * 256.0F / 360.0F),
				this.onGround
			);
	
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
	}

	@Override
	public boolean isDeadNMS() {
		return this.dead;
	}
	
	@Override
	public int getIdNMS() {
		return super.getId(); // Return the real ID without checking the stack trace.
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
