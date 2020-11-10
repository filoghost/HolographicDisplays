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
package me.filoghost.holographicdisplays.nms.interfaces.entity;

public interface NMSNameable extends NMSEntityBase {
    
    // Sets a custom name as a String.
    public void setCustomNameNMS(String name);
    
    // Returns the custom name as a String.
    public String getCustomNameStringNMS();

    // Returns the custom name as version-dependent NMS object (String for MC 1.12 and below, ChatComponent for MC 1.13+ a ChatComponent).
    public Object getCustomNameObjectNMS();
    
}
