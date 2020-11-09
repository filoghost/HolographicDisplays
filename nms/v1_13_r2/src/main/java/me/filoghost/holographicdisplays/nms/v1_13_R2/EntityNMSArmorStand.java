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
package me.filoghost.holographicdisplays.nms.v1_13_R2;

import me.filoghost.holographicdisplays.api.line.HologramLine;
import me.filoghost.holographicdisplays.nms.interfaces.entity.NMSArmorStand;
import me.filoghost.holographicdisplays.util.Utils;
import net.minecraft.server.v1_13_R2.AxisAlignedBB;
import net.minecraft.server.v1_13_R2.DamageSource;
import net.minecraft.server.v1_13_R2.EntityArmorStand;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.EntityPlayer;
import net.minecraft.server.v1_13_R2.EnumHand;
import net.minecraft.server.v1_13_R2.EnumInteractionResult;
import net.minecraft.server.v1_13_R2.EnumItemSlot;
import net.minecraft.server.v1_13_R2.IChatBaseComponent;
import net.minecraft.server.v1_13_R2.ItemStack;
import net.minecraft.server.v1_13_R2.NBTTagCompound;
import net.minecraft.server.v1_13_R2.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_13_R2.SoundEffect;
import net.minecraft.server.v1_13_R2.Vec3D;
import net.minecraft.server.v1_13_R2.World;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_13_R2.util.CraftChatMessage;

public class EntityNMSArmorStand extends EntityArmorStand implements NMSArmorStand {

	private HologramLine parentPiece;
	private String customName;
	
	public EntityNMSArmorStand(World world, HologramLine parentPiece) {
		super(world);
		super.setInvisible(true);
		super.setSmall(true);
		super.setArms(false);
		super.setNoGravity(true);
		super.setBasePlate(true);
		super.setMarker(true);
		super.collides = false;
		this.parentPiece = parentPiece;
		forceSetBoundingBox(new NullBoundingBox());
		
		this.onGround = true; // Workaround to force EntityTrackerEntry to send a teleport packet.
	}
	
	@Override
	public void tick() {
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
	public NBTTagCompound save(NBTTagCompound nbttagcompound) {
		// Do not save NBT.
		return nbttagcompound;
	}
	
	@Override
	public void f(NBTTagCompound nbttagcompound) {
		// Do not load NBT.
	}
	
	@Override
	public void a(NBTTagCompound nbttagcompound) {
		// Do not load NBT.
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
	public boolean isCollidable() {
		return false;
	}
	
	@Override
	public void setCustomName(IChatBaseComponent ichatbasecomponent) {
		// Locks the custom name.
	}
	
	@Override
	public void setCustomNameVisible(boolean visible) {
		// Locks the custom name.
	}

	@Override
	public EnumInteractionResult a(EntityHuman human, Vec3D vec3d, EnumHand enumhand) {
		// Prevent stand being equipped
		return EnumInteractionResult.PASS;
	}

	@Override
	public boolean c(int i, ItemStack item) {
		// Prevent stand being equipped
		return false;
	}

	@Override
	public void setSlot(EnumItemSlot enumitemslot, ItemStack itemstack) {
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
	public void a(SoundEffect soundeffect, float f, float f1) {
	    // Remove sounds.
	}
	
	@Override
	public void setCustomNameNMS(String name) {
		this.customName = Utils.limitLength(name, 300);
		super.setCustomName(CraftChatMessage.fromStringOrNull(customName));
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
			super.bukkitEntity = new CraftNMSArmorStand(super.world.getServer(), this);
	    }
		return super.bukkitEntity;
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
		
		for (Object obj : super.world.players) {
			if (obj instanceof EntityPlayer) {
				EntityPlayer nmsPlayer = (EntityPlayer) obj;

				double distanceSquared = Utils.square(nmsPlayer.locX - super.locX) + Utils.square(nmsPlayer.locZ - super.locZ);
				if (distanceSquared < 8192 && nmsPlayer.playerConnection != null) {
					nmsPlayer.playerConnection.sendPacket(teleportPacket);
				}
			}
		}
	}

	@Override
	public boolean isDeadNMS() {
		return super.dead;
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
