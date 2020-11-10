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

import me.filoghost.holographicdisplays.api.Hologram;

/**
 * Interface to represent a line in a Hologram.
 */
public interface HologramLine {
    
    /**
     * Returns the parent Hologram of this line.
     * 
     * @return the parent Hologram.
     */
    public Hologram getParent();
    
    /**
     * Removes this line from the parent Hologram. Since: v2.0.1
     * Do not call if the Hologram has been deleted, an exception will be thrown.
     */
    public void removeLine();

}
