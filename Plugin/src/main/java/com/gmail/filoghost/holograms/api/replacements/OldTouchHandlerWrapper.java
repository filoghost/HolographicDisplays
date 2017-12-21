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
