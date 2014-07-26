package com.gmail.filoghost.holograms.object;

import org.bukkit.Location;

import com.gmail.filoghost.holograms.Configuration;
import com.gmail.filoghost.holograms.exception.SpawnFailedException;
import com.gmail.filoghost.holograms.object.pieces.HologramLine;
import com.gmail.filoghost.holograms.utils.Validator;

/**
 * Exactly like CraftHologram, but without variable replacement (more clean).
 */
public class APICraftHologram extends CraftHologram {

	public APICraftHologram(Location source) {
		super("{API-Hologram}", source);
	}
	
	// Just changes this method.
	@Override
	public boolean forceUpdate() {
		
		Validator.checkState(!isDeleted(), "Hologram already deleted");
	
		// Remove previous entities.
		hide();
		
		try {
			
			double lineSpacing = Configuration.verticalLineSpacing;
			
			// While iterating we change this var.
			double currentY = this.y;
			
			for (String text : textLines) {

				HologramLine lineEntity = new HologramLine(text);
				lineEntity.spawn(this, bukkitWorld, x, currentY, z);
				linesEntities.add(lineEntity);				
				
				currentY -= lineSpacing;
			}
			
			if (touchHandler != null) {
				touchSlimeEntity.spawn(this, bukkitWorld, x, y, z);
			}
			
		} catch (SpawnFailedException ex) {
			// Kill the entities and return false.
			hide();
			return false;
		}
		
		return true;
	}

}
