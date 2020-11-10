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
package me.filoghost.holographicdisplays.bridge.protocollib;

import me.filoghost.holographicdisplays.nms.interfaces.NMSManager;
import me.filoghost.holographicdisplays.object.CraftHologram;
import me.filoghost.holographicdisplays.object.line.CraftHologramLine;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public interface ProtocolLibHook {
    
    public boolean hook(Plugin plugin, NMSManager nmsManager);
    
    public void sendDestroyEntitiesPacket(Player player, CraftHologram hologram);
    
    public void sendDestroyEntitiesPacket(Player player, CraftHologramLine line);
    
    public void sendCreateEntitiesPacket(Player player, CraftHologram hologram);
    
    public void sendCreateEntitiesPacket(Player player, CraftHologramLine line);

}
