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
package com.gmail.filoghost.holographicdisplays.api;

import com.gmail.filoghost.holographicdisplays.api.line.HologramLine;
import com.gmail.filoghost.holographicdisplays.api.line.ItemLine;
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

/**
 * @deprecated Please use the new API!
 */
@Deprecated
public interface Hologram {

    @Deprecated
    public TextLine appendTextLine(String text);

    @Deprecated
    public ItemLine appendItemLine(ItemStack itemStack);
    
    @Deprecated
    public TextLine insertTextLine(int index, String text);

    @Deprecated
    public ItemLine insertItemLine(int index, ItemStack itemStack);

    @Deprecated
    public HologramLine getLine(int index);
    
    @Deprecated
    public void removeLine(int index);

    @Deprecated
    public void clearLines();

    @Deprecated
    public int size();

    @Deprecated
    public double getHeight();

    @Deprecated
    public void teleport(Location location);
    
    @Deprecated
    public void teleport(World world, double x, double y, double z);
    
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
    public VisibilityManager getVisibilityManager();

    @Deprecated
    public long getCreationTimestamp();

    @Deprecated
    public boolean isAllowPlaceholders();

    @Deprecated
    public void setAllowPlaceholders(boolean allowPlaceholders);
    
    @Deprecated
    public void delete();
    
    @Deprecated
    public boolean isDeleted();
    
}
