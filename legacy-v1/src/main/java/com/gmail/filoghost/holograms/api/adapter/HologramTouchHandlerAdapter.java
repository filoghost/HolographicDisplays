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

import com.gmail.filoghost.holograms.api.Hologram;
import com.gmail.filoghost.holograms.api.TouchHandler;
import org.bukkit.entity.Player;

@SuppressWarnings("deprecation")
public class HologramTouchHandlerAdapter implements me.filoghost.holographicdisplays.api.handler.TouchHandler {

	protected TouchHandler oldHandler;
	private Hologram hologram;
	
	public HologramTouchHandlerAdapter(Hologram hologram, TouchHandler oldHandler) {
		this.hologram = hologram;
		this.oldHandler = oldHandler;
	}
	
	@Override
	public void onTouch(Player player) {
		oldHandler.onTouch(hologram, player);
	}

}
