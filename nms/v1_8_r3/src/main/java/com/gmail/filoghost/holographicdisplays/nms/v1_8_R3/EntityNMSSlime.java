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
package com.gmail.filoghost.holographicdisplays.nms.v1_8_R3;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import com.gmail.filoghost.holographicdisplays.api.line.HologramLine;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.entity.NMSEntityBase;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.entity.NMSSlime;
import com.gmail.filoghost.holographicdisplays.util.ConsoleLogger;
import com.gmail.filoghost.holographicdisplays.util.reflection.ReflectField;

import net.minecraft.server.v1_8_R3.AxisAlignedBB;
import net.minecraft.server.v1_8_R3.DamageSource;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityDamageSource;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.EntitySlime;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.World;

public class EntityNMSSlime extends EntitySlime implements NMSSlime {
	
	private static final ReflectField<Double> RIDER_PITCH_DELTA = new ReflectField<>(Entity.class, "ar");
	private static final ReflectField<Double> RIDER_YAW_DELTA = new ReflectField<>(Entity.class, "as");

	private HologramLine parentPiece;
	
	public EntityNMSSlime(World world, HologramLine parentPiece) {
		super(world);
		super.persistent = true;
		a(0.0F, 0.0F);
		setSize(1);
		setInvisible(true);
		this.parentPiece = parentPiece;
		forceSetBoundingBox(new NullBoundingBox());
	}
	
	@Override
	public void t_() {
		// Disable normal ticking for this entity.
		
		// So it won't get removed.
		ticksLived = 0;

		// The slime dies without a vehicle.
		if (this.vehicle == null) {
			killEntityNMS();
		}
	}
	
	@Override
	public void inactiveTick() {
		// Disable normal ticking for this entity.
		
		// So it won't get removed.
		ticksLived = 0;
	}
	
	@Override
	public void a(AxisAlignedBB boundingBox) {
		// Do not change it!
	}
	
	public void forceSetBoundingBox(AxisAlignedBB boundingBox) {
		super.a(boundingBox);
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
	public boolean damageEntity(DamageSource damageSource, float amount) {
		if (damageSource instanceof EntityDamageSource) {
			EntityDamageSource entityDamageSource = (EntityDamageSource) damageSource;
			if (entityDamageSource.getEntity() instanceof EntityPlayer) {
				Bukkit.getPluginManager().callEvent(new PlayerInteractEntityEvent(((EntityPlayer) entityDamageSource.getEntity()).getBukkitEntity(), getBukkitEntity()));
			}
		}
		return false;
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
	public void makeSound(String sound, float volume, float pitch) {
	    // Remove sounds.
	}
	
	@Override
	public void die() {
		// Prevent being killed.
	}
	
	@Override
	public CraftEntity getBukkitEntity() {
		if (super.bukkitEntity == null) {
			this.bukkitEntity = new CraftNMSSlime(this.world.getServer(), this);
	    }
		return this.bukkitEntity;
	}

	@Override
	public boolean isDeadNMS() {
		return super.dead;
	}
	
	@Override
	public void killEntityNMS() {
		super.dead = true;
	}
	
	@Override
	public void setLocationNMS(double x, double y, double z) {
		super.setPosition(x, y, z);
	}
	
	@Override
	public int getIdNMS() {
		return this.getId();
	}
	
	@Override
	public HologramLine getHologramLine() {
		return parentPiece;
	}

	@Override
	public org.bukkit.entity.Entity getBukkitEntityNMS() {
		return getBukkitEntity();
	}
	
	@Override
	public void setPassengerOfNMS(NMSEntityBase vehicleBase) {
		if (vehicleBase == null || !(vehicleBase instanceof Entity)) {
			// It should never dismount
			return;
		}
		
		Entity entity = (Entity) vehicleBase;
		
		try {
			RIDER_PITCH_DELTA.set(this, 0.0);
			RIDER_YAW_DELTA.set(this, 0.0);
		} catch (Throwable t) {
			ConsoleLogger.logDebug(Level.SEVERE, "Couldn't set rider pitch and yaw", t);
		}

        if (this.vehicle != null) {
        	this.vehicle.passenger = null;
        }
        
        this.vehicle = entity;
        entity.passenger = this;
	}
}
