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
package com.gmail.filoghost.holographicdisplays.object;

import org.bukkit.Location;

public class NamedHologram extends CraftHologram {

	private final String name;
	
	public NamedHologram(Location source, String name) {
		super(source);
		this.name = name;
		setAllowPlaceholders(true);
	}

	public String getName() {
		return name;
	}
	
	@Override
	public void delete() {
		super.delete();
		NamedHologramManager.removeHologram(this);
	}

	@Override
	public String toString() {
		return "NamedHologram [name=" + name + ", super=" + super.toString() + "]";
	}	
	
}
