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
package me.filoghost.holographicdisplays.api.line;

import me.filoghost.holographicdisplays.api.handler.TouchHandler;

/**
 * A line of a Hologram that can be touched (right click).
 */
public interface TouchableLine extends HologramLine {

	/**
	 * Sets the TouchHandler for this line.
	 * 
	 * @param touchHandler the new TouchHandler, can be null.
	 */
	public void setTouchHandler(TouchHandler touchHandler);
	
	/**
	 * Returns the current TouchHandler of this line.
	 * 
	 * @return the current TouchHandler, can be null.
	 */
	public TouchHandler getTouchHandler();
	
}
