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
 * @deprecated Please use the new API!
 */
@Deprecated
public interface CollectableLine extends HologramLine {

	@Deprecated
	public void setPickupHandler(PickupHandler pickupHandler);
	
	@Deprecated
	public PickupHandler getPickupHandler();
	
}
