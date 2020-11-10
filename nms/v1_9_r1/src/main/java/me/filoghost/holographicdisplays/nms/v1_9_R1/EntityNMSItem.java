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
package me.filoghost.holographicdisplays.nms.v1_9_R1;

import me.filoghost.holographicdisplays.api.line.ItemLine;
import me.filoghost.holographicdisplays.nms.interfaces.ItemPickupManager;
import me.filoghost.holographicdisplays.nms.interfaces.entity.NMSEntityBase;
import me.filoghost.holographicdisplays.nms.interfaces.entity.NMSItem;
import me.filoghost.holographicdisplays.util.ConsoleLogger;
import me.filoghost.holographicdisplays.util.ItemUtils;
import me.filoghost.holographicdisplays.util.Utils;
import me.filoghost.holographicdisplays.util.reflection.ReflectField;
import net.minecraft.server.v1_9_R1.Blocks;
import net.minecraft.server.v1_9_R1.DamageSource;
import net.minecraft.server.v1_9_R1.Entity;
import net.minecraft.server.v1_9_R1.EntityHuman;
import net.minecraft.server.v1_9_R1.EntityItem;
import net.minecraft.server.v1_9_R1.EntityPlayer;
import net.minecraft.server.v1_9_R1.ItemStack;
import net.minecraft.server.v1_9_R1.NBTTagCompound;
import net.minecraft.server.v1_9_R1.NBTTagList;
import net.minecraft.server.v1_9_R1.NBTTagString;
import net.minecraft.server.v1_9_R1.PacketPlayOutMount;
import net.minecraft.server.v1_9_R1.World;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_9_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public class EntityNMSItem extends EntityItem implements NMSItem {
    
    private static final ReflectField<Entity> VEHICLE_FIELD = new ReflectField<>(Entity.class, "as");
    
    private ItemLine parentPiece;
    private ItemPickupManager itemPickupManager;
    
    private int resendMountPacketTicks;
    
    public EntityNMSItem(World world, ItemLine piece, ItemPickupManager itemPickupManager) {
        super(world);
        super.pickupDelay = Integer.MAX_VALUE;
        this.parentPiece = piece;
        this.itemPickupManager = itemPickupManager;
    }
    
    @Override
    public void m() {
        // Disable normal ticking for this entity.
        
        // So it won't get removed.
        ticksLived = 0;
        
        if (resendMountPacketTicks++ > 20) {
            resendMountPacketTicks = 0;
            
            if (by() != null) {
                // Send a packet near to "remind" players that the item is riding the armor stand (Spigot bug or client bug)
                PacketPlayOutMount mountPacket = new PacketPlayOutMount(by());
    
                for (Object obj : super.world.players) {
                    if (obj instanceof EntityPlayer) {
                        EntityPlayer nmsPlayer = (EntityPlayer) obj;
    
                        double distanceSquared = Utils.square(nmsPlayer.locX - super.locX) + Utils.square(nmsPlayer.locZ - super.locZ);
                        if (distanceSquared < 1024 && nmsPlayer.playerConnection != null) {
                            nmsPlayer.playerConnection.sendPacket(mountPacket);
                        }
                    }
                }
            }
        }
    }
    
    @Override
    public void inactiveTick() {
        // Disable normal ticking for this entity.
        
        // So it won't get removed.
        ticksLived = 0;
    }
    
    // Method called when a player is near.
    @Override
    public void d(EntityHuman human) {
        
        if (human.locY < super.locY - 1.5 || human.locY > super.locY + 1.0) {
            // Too low or too high, it's a bit weird.
            return;
        }
        
        if (parentPiece.getPickupHandler() != null && human instanceof EntityPlayer) {
            itemPickupManager.handleItemLinePickup((Player) human.getBukkitEntity(), parentPiece.getPickupHandler(), parentPiece.getParent());
            // It is never added to the inventory.
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
    public void die() {
        // Prevent being killed.
    }
    
    @Override
    public boolean isAlive() {
        // This override prevents items from being picked up by hoppers.
        // Should have no side effects.
        return false;
    }

    @Override
    public CraftEntity getBukkitEntity() {
        if (super.bukkitEntity == null) {
            super.bukkitEntity = new CraftNMSItem(super.world.getServer(), this);
        }
        return super.bukkitEntity;
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
    public void setItemStackNMS(org.bukkit.inventory.ItemStack stack) {
        ItemStack newItem = CraftItemStack.asNMSCopy(stack);
        
        if (newItem == null) {
            newItem = new ItemStack(Blocks.BEDROCK);
        }
        
        if (newItem.getTag() == null) {
            newItem.setTag(new NBTTagCompound());
        }
        NBTTagCompound display = newItem.getTag().getCompound("display");
        
        if (!newItem.getTag().hasKey("display")) {
            newItem.getTag().set("display", display);
        }
        
        NBTTagList tagList = new NBTTagList();
        tagList.add(new NBTTagString(ItemUtils.ANTISTACK_LORE)); // Antistack lore
        display.set("Lore", tagList);
        
        setItemStack(newItem);
    }
    
    @Override
    public int getIdNMS() {
        return super.getId();
    }
    
    @Override
    public ItemLine getHologramLine() {
        return parentPiece;
    }
    
    @Override
    public void allowPickup(boolean pickup) {
        if (pickup) {
            super.pickupDelay = 0;
        } else {
            super.pickupDelay = Integer.MAX_VALUE;
        }
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
            if (super.by() != null) {
                Entity oldVehicle = super.by();
                VEHICLE_FIELD.set(this, null);
                oldVehicle.passengers.remove(this);
            }

            VEHICLE_FIELD.set(this, entity);
            entity.passengers.clear();
            entity.passengers.add(this);

        } catch (Throwable t) {
            ConsoleLogger.logDebug(Level.SEVERE, "Couldn't set passenger", t);
        }
    }

    @Override
    public Object getRawItemStack() {
        return super.getItemStack();
    }
}
