package com.gmail.filoghost.holographicdisplays.nms.v1_13_R1;

import com.gmail.filoghost.holographicdisplays.api.line.ItemLine;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.ItemPickupManager;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.entity.NMSEntityBase;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.entity.NMSItem;
import com.gmail.filoghost.holographicdisplays.util.DebugHandler;
import com.gmail.filoghost.holographicdisplays.util.ItemUtils;
import com.gmail.filoghost.holographicdisplays.util.ReflectionUtils;
import net.minecraft.server.v1_13_R1.*;
import org.bukkit.craftbukkit.v1_13_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_13_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;

public class EntityNMSItem extends EntityItem implements NMSItem {

    private boolean lockTick;
    private ItemLine parentPiece;
    private ItemPickupManager itemPickupManager;

    public EntityNMSItem(World world, ItemLine piece, ItemPickupManager itemPickupManager) {
        super(world);
        super.pickupDelay = 32767; // Lock the item pickup delay, also prevents entities from picking up the item.
        this.parentPiece = piece;
        this.itemPickupManager = itemPickupManager;
    }

    /*
     * Implement NMSEntityBase methods.
     */

    @Override
    public ItemLine getHologramLine() {
        return parentPiece;
    }

    @Override
    public void setLockTick(boolean lock) {
        lockTick = lock;
    }

    @Override
    public void setLocationNMS(double x, double y, double z) {
        super.setPosition(x, y, z);
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
    public int getIdNMS() {
        return super.getId();
    }

    @Override
    public org.bukkit.entity.Entity getBukkitEntityNMS() {
        return getBukkitEntity();
    }

    /*
     * Implement NMSCanMount methods.
     */

    @Override
    public void setPassengerOfNMS(NMSEntityBase vehicleBase) {
        if (!(vehicleBase instanceof Entity)) {
            // It should never dismount.
            return;
        }

        Entity entity = (Entity) vehicleBase;
        try {
            if (super.getVehicle() != null) {
                super.getVehicle().passengers.remove(this);
            }
            ReflectionUtils.setPrivateField(Entity.class, this, "ax", entity);
            entity.passengers.clear();
            entity.passengers.add(this);
        } catch (Exception ex) {
            DebugHandler.handleDebugException(ex);
        }
    }

    /*
     * Implement NMSItem methods.
     */

    @Override
    public void setItemStackNMS(org.bukkit.inventory.ItemStack stack) {
        ItemStack newItem = CraftItemStack.asNMSCopy(stack);

        if (newItem == null) {
            newItem = new ItemStack(Blocks.BEDROCK);
        }

        if (newItem.getTag() == null) {
            newItem.setTag(new NBTTagCompound());
        }
        NBTTagCompound display = newItem.getTag().getCompound("display"); // Returns a new NBTTagCompound if not existing
        if (!newItem.getTag().hasKey("display")) {
            newItem.getTag().set("display", display);
        }

        NBTTagList tagList = new NBTTagList();
        tagList.add(new NBTTagString(ItemUtils.ANTISTACK_LORE)); // Antistack lore
        display.set("Lore", tagList);

        newItem.setCount(1);
        setItemStack(newItem);
    }

    @Override
    public void allowPickup(boolean pickup) {
        if (pickup) {
            super.pickupDelay = 0;
        } else {
            super.pickupDelay = 32767;
        }
    }

    @Override
    public Object getRawItemStack() {
        return super.getItemStack();
    }

    /*
     * Handle vanilla Entity methods.
     */

    @Override
    public CraftEntity getBukkitEntity() {
        if (super.bukkitEntity == null) {
            super.bukkitEntity = new CraftNMSItem(super.world.getServer(), this);
        }
        return super.bukkitEntity;
    }

    // Do not save EntityItem NBT data.
    @Override
    public void b(NBTTagCompound nbttagcompound) {
    }

    // Do not save Entity ID data.
    @Override
    public boolean c(NBTTagCompound nbttagcompound) {
        return false;
    }

    // Do not save Entity NBT data.
    @Override
    public NBTTagCompound save(NBTTagCompound nbttagcompound) {
        return nbttagcompound;
    }

    // Do not load Entity NBT data.
    @Override
    public void f(NBTTagCompound nbttagcompound) {
    }

    // Do not load EntityItem NBT data.
    @Override
    public void a(NBTTagCompound nbttagcompound) {
    }

    /*
     * The field Entity.invulnerable is private.
     * It's only used while saving NBTTags, but since the entity would be killed
     * on chunk unload, we prefer to override isInvulnerable().
     */
    @Override
    public boolean isInvulnerable(DamageSource source) {
        return true;
    }

    @Override
    public boolean isCollidable() {
        return false;
    }


    // Filter inactive ticks.
    @Override
    public void inactiveTick() {
        if (!lockTick) {
            super.inactiveTick();
        }
    }

    // Filter ticks.
    @Override
    public void tick() {
        // So it won't get removed.
        ticksLived = 0;

        if (!lockTick) {
            super.tick();
        }
    }

    // Prevent being killed.
    @Override
    public void die() {
    }

    /*
     * Handle vanilla EntityItem methods.
     */

    // Handle item pickup request. Called when a player is near the item entity.
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
}
