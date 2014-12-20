package com.gmail.filoghost.holographicdisplays.api.line;

import com.gmail.filoghost.holographicdisplays.api.handler.TouchHandler;

/**
 * A piece of hologram that can be touched.
 */
public interface TouchableLine extends HologramLine {

	public void setTouchHandler(TouchHandler touchHandler);
	
	public TouchHandler getTouchHandler();
	
}
