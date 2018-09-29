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
package com.gmail.filoghost.holographicdisplays.object.line;

import java.util.Collection;

import org.bukkit.World;

import com.gmail.filoghost.holographicdisplays.api.line.HologramLine;
import com.gmail.filoghost.holographicdisplays.object.CraftHologram;
import com.gmail.filoghost.holographicdisplays.placeholder.RelativePlaceholder;
import com.gmail.filoghost.holographicdisplays.util.Validator;

public abstract class CraftHologramLine implements HologramLine {
	
	private final double height;
	private final CraftHologram parent;
	
	// This field is necessary for teleport.
	private boolean isSpawned;
	
	protected CraftHologramLine(double height, CraftHologram parent) {
		Validator.notNull(parent, "parent hologram");
		this.height = height;
		this.parent = parent;
	}
	
	public final double getHeight() {
		return height;
	}

	@Override
	public final CraftHologram getParent() {
		return parent;
	}
	
	public void removeLine() {
		parent.removeLine(this);
	}

	public void spawn(World world, double x, double y, double z) {
		Validator.notNull(world, "world");
		
		// Remove the old entities when spawning the new ones.
		despawn();
		isSpawned = true;
		
		// Do nothing, there are no entities in this class.
	}
	
	public void despawn() {
		isSpawned = false;
	}
	
	public final boolean isSpawned() {
		return isSpawned;
	}

	public Collection<RelativePlaceholder> getRelativePlaceholders() {
		return null;
	}
	
	public abstract int[] getEntitiesIDs();
	
	public abstract void teleport(double x, double y, double z);

}
