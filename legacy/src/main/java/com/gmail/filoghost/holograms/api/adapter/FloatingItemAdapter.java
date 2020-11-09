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
package com.gmail.filoghost.holograms.api.adapter;

import com.gmail.filoghost.holograms.api.FloatingItem;
import com.gmail.filoghost.holograms.api.ItemTouchHandler;
import com.gmail.filoghost.holograms.api.PickupHandler;
import me.filoghost.holographicdisplays.api.Hologram;
import me.filoghost.holographicdisplays.api.line.ItemLine;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("deprecation")
public class FloatingItemAdapter implements FloatingItem {
	
	public static Map<Plugin, Collection<FloatingItemAdapter>> activeFloatingItems = new HashMap<>();
	
	private Plugin plugin;
	public Hologram hologram;
	private ItemLine itemLine;
	private ItemTouchHandler touchHandler;
	private PickupHandler pickupHandler;
	
	public FloatingItemAdapter(Plugin plugin, Hologram delegateHologram, ItemLine delegateItemLine) {
		this.plugin = plugin;
		this.hologram = delegateHologram;		
		this.itemLine = delegateItemLine;
		
		activeFloatingItems.computeIfAbsent(plugin, __ -> new ArrayList<>()).add(this);
	}

	@Override
	public boolean update() {
		return true;
	}

	@Override
	public void hide() {
		
	}

	@Override
	public void setItemStack(ItemStack itemstack) {
		itemLine.setItemStack(itemstack);
	}

	@Override
	public ItemStack getItemStack() {
		return itemLine.getItemStack();
	}

	@Override
	public Location getLocation() {
		return hologram.getLocation();
	}

	@Override
	public double getX() {
		return hologram.getX();
	}

	@Override
	public double getY() {
		return hologram.getY();
	}

	@Override
	public double getZ() {
		return hologram.getZ();
	}

	@Override
	public World getWorld() {
		return hologram.getWorld();
	}

	@Override
	public void teleport(Location location) {
		hologram.teleport(location);
	}

	@Override
	public void setTouchHandler(ItemTouchHandler handler) {
		this.touchHandler = handler;
		
		if (handler != null) {
			itemLine.setTouchHandler(new ItemTouchHandlerAdapter(this, handler));
		} else {
			itemLine.setTouchHandler(null);
		}
	}

	@Override
	public ItemTouchHandler getTouchHandler() {
		return touchHandler;
	}

	@Override
	public boolean hasTouchHandler() {
		return touchHandler != null;
	}

	@Override
	public void setPickupHandler(PickupHandler handler) {
		this.pickupHandler = handler;
		
		if (handler != null) {
			itemLine.setPickupHandler(new PickupHandlerAdapter(this, handler));
		} else {
			itemLine.setPickupHandler(null);
		}
	}

	@Override
	public PickupHandler getPickupHandler() {
		return pickupHandler;
	}

	@Override
	public boolean hasPickupHandler() {
		return pickupHandler != null;
	}

	@Override
	public long getCreationTimestamp() {
		return hologram.getCreationTimestamp();
	}

	@Override
	public void delete() {
		hologram.delete();
		
		activeFloatingItems.get(plugin).remove(this);
	}

	@Override
	public boolean isDeleted() {
		return hologram.isDeleted();
	}

}
