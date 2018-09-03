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
package com.gmail.filoghost.holograms.api.replacements;

import org.bukkit.entity.Player;

import com.gmail.filoghost.holograms.api.TouchHandler;
import com.gmail.filoghost.holographicdisplays.object.CraftHologram;

@SuppressWarnings("deprecation")
public class OldTouchHandlerWrapper implements com.gmail.filoghost.holographicdisplays.api.handler.TouchHandler {

	public TouchHandler oldHandler;
	private CraftHologram hologram;
	
	public OldTouchHandlerWrapper(CraftHologram hologram, TouchHandler oldHandler) {
		this.hologram = hologram;
		this.oldHandler = oldHandler;
	}
	
	@Override
	public void onTouch(Player player) {
		oldHandler.onTouch(hologram, player);
	}

}
