package com.gmail.filoghost.holograms.api.replacements;

import org.bukkit.entity.Player;

import com.gmail.filoghost.holograms.api.FloatingItem;
import com.gmail.filoghost.holograms.api.PickupHandler;

@SuppressWarnings("deprecation")
public class OldPickupHandlerWrapper implements com.gmail.filoghost.holographicdisplays.api.handler.PickupHandler {

	public PickupHandler oldHandler;
	private FloatingItem item;
	
	public OldPickupHandlerWrapper(FloatingItem item, PickupHandler oldPickupHandler) {
		this.item = item;
		this.oldHandler = oldPickupHandler;
	}
	
	@Override
	public void onPickup(Player player) {
		oldHandler.onPickup(item, player);
	}

}
