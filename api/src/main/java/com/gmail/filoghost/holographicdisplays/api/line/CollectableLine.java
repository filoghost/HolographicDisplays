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
package com.gmail.filoghost.holographicdisplays.api.line;

import com.gmail.filoghost.holographicdisplays.api.handler.PickupHandler;

/**
 * A line of a Hologram that can be picked up.
 */
public interface CollectableLine extends HologramLine {
	
	/**
	 * Sets the PickupHandler for this line.
	 * 
	 * @param pickupHandler the new PickupHandler, can be null.
	 */
	public void setPickupHandler(PickupHandler pickupHandler);
	
	/**
	 * Returns the current PickupHandler of this line.
	 * 
	 * @return the current PickupHandler, can be null.
	 */
	public PickupHandler getPickupHandler();
	
}
