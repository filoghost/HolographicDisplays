package com.gmail.filoghost.holographicdisplays.object.line;

import org.bukkit.World;

import com.gmail.filoghost.holographicdisplays.api.handler.TouchHandler;
import com.gmail.filoghost.holographicdisplays.object.CraftHologram;

/**
 * Useful class that implements TouchablePiece. The downside is that subclasses must extend this, and cannot extend other classes.
 * But all the current items are touchable.
 */
public abstract class CraftTouchableLine extends CraftHologramLine {

	protected CraftTouchSlimeLine touchSlime;
	private TouchHandler touchHandler;

	
	protected CraftTouchableLine(double height, CraftHologram parent) {
		super(height, parent);
	}
	
	
	protected void setTouchHandler(TouchHandler touchHandler, World world, double x, double y, double z) {
		this.touchHandler = touchHandler;
		
		if (touchHandler != null && touchSlime == null && world != null) {
			// If the touch handler was null before and no entity has been spawned, spawn it now.
			touchSlime = new CraftTouchSlimeLine(getParent(), this);
			touchSlime.spawn(world, x, y + (getHeight() / 2.0 - touchSlime.getHeight() / 2.0), z);
			
		} else if (touchHandler == null && touchSlime != null) {
			// Opposite case, the touch handler was not null and an entity was spawned, but now it's useless.
			touchSlime.despawn();
			touchSlime = null;
		}
	}

	
	public TouchHandler getTouchHandler() {
		return this.touchHandler;
	}


	@Override
	public void spawn(World world, double x, double y, double z) {
		super.spawn(world, x, y, z);
		
		if (touchHandler != null) {
			touchSlime = new CraftTouchSlimeLine(getParent(), this);
			touchSlime.spawn(world, x, y + (getHeight() / 2.0 - touchSlime.getHeight() / 2.0), z);
		}
	}


	@Override
	public void despawn() {
		super.despawn();
		
		if (touchSlime != null) {
			touchSlime.despawn();
			touchSlime = null;
		}
	}
	
	
	@Override
	public void teleport(double x, double y, double z) {
		if (touchSlime != null) {
			touchSlime.teleport(x, y + (getHeight() / 2.0 - touchSlime.getHeight() / 2.0), z);
		}
	}


	public CraftTouchSlimeLine getTouchSlime() {
		return touchSlime;
	}
	
}
