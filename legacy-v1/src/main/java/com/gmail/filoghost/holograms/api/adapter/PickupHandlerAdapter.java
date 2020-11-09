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
package com.gmail.filoghost.holograms.api.adapter;

import com.gmail.filoghost.holograms.api.FloatingItem;
import com.gmail.filoghost.holograms.api.PickupHandler;
import org.bukkit.entity.Player;

@SuppressWarnings("deprecation")
public class PickupHandlerAdapter implements me.filoghost.holographicdisplays.api.handler.PickupHandler {

	public PickupHandler oldHandler;
	private FloatingItem item;
	
	public PickupHandlerAdapter(FloatingItem item, PickupHandler oldPickupHandler) {
		this.item = item;
		this.oldHandler = oldPickupHandler;
	}
	
	@Override
	public void onPickup(Player player) {
		oldHandler.onPickup(item, player);
	}

}
