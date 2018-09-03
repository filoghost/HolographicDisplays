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
package com.gmail.filoghost.holograms.api.replacements;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

import com.gmail.filoghost.holograms.api.FloatingItem;
import com.gmail.filoghost.holograms.api.ItemTouchHandler;
import com.gmail.filoghost.holograms.api.PickupHandler;
import com.gmail.filoghost.holographicdisplays.object.CraftHologram;
import com.gmail.filoghost.holographicdisplays.object.line.CraftItemLine;

/**
 * Do not use this class!
 */
@SuppressWarnings("deprecation")
public class FakeFloatingItem implements FloatingItem {
	
	public CraftHologram hologram;
	private CraftItemLine mainLine;
	
	public FakeFloatingItem(CraftHologram hologram, ItemStack item) {
		this.hologram = hologram;		
		mainLine = hologram.appendItemLine(item);
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
		mainLine.setItemStack(itemstack);
	}

	@Override
	public ItemStack getItemStack() {
		return mainLine.getItemStack();
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
		if (handler != null) {
			mainLine.setTouchHandler(new OldItemTouchHandlerWrapper(this, handler));
		} else {
			mainLine.setTouchHandler(null);
		}
	}

	@Override
	public ItemTouchHandler getTouchHandler() {
		return ((OldItemTouchHandlerWrapper) mainLine.getTouchHandler()).oldHandler;
	}

	@Override
	public boolean hasTouchHandler() {
		return mainLine.getTouchHandler() != null;
	}

	@Override
	public void setPickupHandler(PickupHandler handler) {
		if (handler != null) {
			mainLine.setPickupHandler(new OldPickupHandlerWrapper(this, handler));
		} else {
			mainLine.setPickupHandler(null);
		}
	}

	@Override
	public PickupHandler getPickupHandler() {
		return ((OldPickupHandlerWrapper) mainLine.getPickupHandler()).oldHandler;
	}

	@Override
	public boolean hasPickupHandler() {
		return mainLine.getPickupHandler() != null;
	}

	@Override
	public long getCreationTimestamp() {
		return hologram.getCreationTimestamp();
	}

	@Override
	public void delete() {
		hologram.delete();
	}

	@Override
	public boolean isDeleted() {
		return hologram.isDeleted();
	}

}
