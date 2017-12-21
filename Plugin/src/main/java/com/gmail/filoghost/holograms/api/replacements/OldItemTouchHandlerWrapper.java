package com.gmail.filoghost.holograms.api.replacements;

import org.bukkit.entity.Player;

import com.gmail.filoghost.holograms.api.FloatingItem;
import com.gmail.filoghost.holograms.api.ItemTouchHandler;

@SuppressWarnings("deprecation")
public class OldItemTouchHandlerWrapper implements com.gmail.filoghost.holographicdisplays.api.handler.TouchHandler {

	public ItemTouchHandler oldHandler;
	private FloatingItem item;
	
	public OldItemTouchHandlerWrapper(FloatingItem item, ItemTouchHandler oldHandler) {
		this.item = item;
		this.oldHandler = oldHandler;
	}

	@Override
	public void onTouch(Player player) {
		oldHandler.onTouch(item, player);
	}

}
