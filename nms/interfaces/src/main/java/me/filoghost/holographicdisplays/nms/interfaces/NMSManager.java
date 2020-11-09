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
package me.filoghost.holographicdisplays.nms.interfaces;

import me.filoghost.holographicdisplays.api.line.HologramLine;
import me.filoghost.holographicdisplays.api.line.ItemLine;
import me.filoghost.holographicdisplays.nms.interfaces.entity.NMSArmorStand;
import me.filoghost.holographicdisplays.nms.interfaces.entity.NMSEntityBase;
import me.filoghost.holographicdisplays.nms.interfaces.entity.NMSItem;
import me.filoghost.holographicdisplays.nms.interfaces.entity.NMSSlime;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

public interface NMSManager {
	
	// A method to register all the custom entities of the plugin, it may fail.
	public void setup() throws Exception;
	
	public NMSArmorStand spawnNMSArmorStand(org.bukkit.World world, double x, double y, double z, HologramLine parentPiece, boolean broadcastLocationPacket);
	
	public NMSItem spawnNMSItem(org.bukkit.World bukkitWorld, double x, double y, double z, ItemLine parentPiece, ItemStack stack, ItemPickupManager itemPickupManager);
	
	public NMSSlime spawnNMSSlime(org.bukkit.World bukkitWorld, double x, double y, double z, HologramLine parentPiece);
	
	public boolean isNMSEntityBase(org.bukkit.entity.Entity bukkitEntity);

	public NMSEntityBase getNMSEntityBase(org.bukkit.entity.Entity bukkitEntity);

	public NMSEntityBase getNMSEntityBaseFromID(World bukkitWorld, int entityID);

	public Object replaceCustomNameText(Object customNameObject, String target, String replacement);

}
