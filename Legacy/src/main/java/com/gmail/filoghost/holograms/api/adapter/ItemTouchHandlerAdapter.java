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

import org.bukkit.entity.Player;

import com.gmail.filoghost.holograms.api.FloatingItem;
import com.gmail.filoghost.holograms.api.ItemTouchHandler;

@SuppressWarnings("deprecation")
public class ItemTouchHandlerAdapter implements com.gmail.filoghost.holographicdisplays.api.handler.TouchHandler {

	protected ItemTouchHandler oldHandler;
	private FloatingItem item;
	
	public ItemTouchHandlerAdapter(FloatingItem item, ItemTouchHandler oldHandler) {
		this.item = item;
		this.oldHandler = oldHandler;
	}

	@Override
	public void onTouch(Player player) {
		oldHandler.onTouch(item, player);
	}

}
