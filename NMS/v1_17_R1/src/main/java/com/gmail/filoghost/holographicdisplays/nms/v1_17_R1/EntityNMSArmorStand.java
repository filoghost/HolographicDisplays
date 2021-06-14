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
package com.gmail.filoghost.holographicdisplays.nms.v1_17_R1;

import com.gmail.filoghost.holographicdisplays.api.line.HologramLine;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.entity.NMSArmorStand;
import com.gmail.filoghost.holographicdisplays.util.Utils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.game.PacketPlayOutEntityTeleport;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.sounds.SoundEffect;
import net.minecraft.world.EnumHand;
import net.minecraft.world.EnumInteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EnumItemSlot;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.World;
import net.minecraft.world.phys.AxisAlignedBB;
import net.minecraft.world.phys.Vec3D;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_17_R1.util.CraftChatMessage;

public class EntityNMSArmorStand extends EntityArmorStand implements NMSArmorStand {

    private HologramLine parentPiece;
    private CraftEntity customBukkitEntity;
    private String customName;

    public EntityNMSArmorStand(World world, HologramLine parentPiece) {
        super(EntityTypes.c, world);
        super.setInvisible(true);
        super.setSmall(true);
        super.setArms(false);
        super.setNoGravity(true);
        super.setBasePlate(true);
        super.setMarker(true);
        super.collides = false;
        this.parentPiece = parentPiece;
        forceSetBoundingBox(new NullBoundingBox());
        
        this.z /* onGround */ = true; // Workaround to force EntityTrackerEntry to send a teleport packet.
    }
    
	@Override
	public void tick() {
		// Disable normal ticking for this entity.
		
		// Workaround to force EntityTrackerEntry to send a teleport packet immediately after spawning this entity.
		if (this.z /* onGround */) {
			this.z = false;
		}
	}
	
	@Override
	public void inactiveTick() {
		// Disable normal ticking for this entity.
		
		// Workaround to force EntityTrackerEntry to send a teleport packet immediately after spawning this entity.
		if (this.z /* onGround */) {
			this.z = false;
		}
	}

    @Override
    public void setPosition(double d0, double d1, double d2) {
        // Do not change it!
    }

    @Override
    public void saveData(NBTTagCompound nbttagcompound) {
        // Do not save NBT.
    }

    @Override
    public boolean d(NBTTagCompound nbttagcompound) {
        // Do not save NBT.
        return false;
    }

    @Override
    public boolean e(NBTTagCompound nbttagcompound) {
        // Do not save NBT.
        return false;
    }

    @Override
    public NBTTagCompound save(NBTTagCompound nbttagcompound) {
        // Do not save NBT.
        return nbttagcompound;
    }

    @Override
    public void load(NBTTagCompound nbttagcompound) {
        // Do not load NBT.
    }

    @Override
    public void loadData(NBTTagCompound nbttagcompound) {
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
        return EnumInteractionResult.d /* PASS */;
    }

    @Override
    public void setSlot(EnumItemSlot enumitemslot, ItemStack itemstack) {
        // Prevent stand being equipped
    }

    @Override
    public void setSlot(EnumItemSlot enumitemslot, ItemStack itemstack, boolean silent) {
        // Prevent stand being equipped
    }

    public void forceSetBoundingBox(AxisAlignedBB boundingBox) {
        super.a(boundingBox);
    }
    
    @Override
    public void playSound(SoundEffect soundeffect, float f, float f1) {
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
    public void killEntity() {
        // Prevent being killed.
    }

    @Override
    public CraftEntity getBukkitEntity() {
        if (customBukkitEntity == null) {
            customBukkitEntity = new CraftNMSArmorStand(super.getWorld().getCraftServer(), this);
        }
        return customBukkitEntity;
    }

    @Override
    public boolean isDeadNMS() {
        return super.isRemoved();
    }

    @Override
    public void killEntityNMS() {
        super.setRemoved(RemovalReason.b /* DISCARDED */);
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

        for (Object obj : super.getWorld().getPlayers()) {
            if (obj instanceof EntityPlayer) {
                EntityPlayer nmsPlayer = (EntityPlayer) obj;

                double distanceSquared = Utils.square(nmsPlayer.locX() - super.locX()) + Utils.square(nmsPlayer.locZ() - super.locZ());
                if (distanceSquared < 8192 && nmsPlayer.b /* playerConnection */ != null) {
                    nmsPlayer.b.sendPacket(teleportPacket);
                }
            }
        }
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
