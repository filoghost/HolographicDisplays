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
package com.gmail.filoghost.holograms.api;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

/**
 * @deprecated Please use the new API!
 */
@Deprecated
public interface FloatingItem {

    @Deprecated
    public boolean update();

    @Deprecated
    public void hide();

    @Deprecated
    public void setItemStack(ItemStack itemstack);

    @Deprecated
    public ItemStack getItemStack();

    @Deprecated
    public Location getLocation();

    @Deprecated
    public double getX();

    @Deprecated
    public double getY();

    @Deprecated
    public double getZ();

    @Deprecated
    public World getWorld();

    @Deprecated
    public void teleport(Location location);

    @Deprecated
    public void setTouchHandler(ItemTouchHandler handler);

    @Deprecated
    public ItemTouchHandler getTouchHandler();

    @Deprecated
    public boolean hasTouchHandler();
    
    @Deprecated
    public void setPickupHandler(PickupHandler handler);

    @Deprecated
    public PickupHandler getPickupHandler();

    @Deprecated
    public boolean hasPickupHandler();

    @Deprecated
    public long getCreationTimestamp();

    @Deprecated
    public void delete();

    @Deprecated
    public boolean isDeleted();
}
