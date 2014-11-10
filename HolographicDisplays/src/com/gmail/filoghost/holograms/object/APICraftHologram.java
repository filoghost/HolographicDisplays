package com.gmail.filoghost.holograms.object;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.gmail.filoghost.holograms.Configuration;
import com.gmail.filoghost.holograms.commands.CommandValidator;
import com.gmail.filoghost.holograms.exception.CommandException;
import com.gmail.filoghost.holograms.exception.SpawnFailedException;
import com.gmail.filoghost.holograms.object.pieces.FloatingItemDoubleEntity;
import com.gmail.filoghost.holograms.object.pieces.HologramLine;
import com.gmail.filoghost.holograms.utils.ItemUtils;
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
				
				if (text.length() >= 5 && text.substring(0, 5).toLowerCase().equals("icon:")) {

					// It's a floating icon!
					ItemStack icon;
					try {
						icon = CommandValidator.matchItemStack(text.substring(5));
					} catch (CommandException e) {
						icon = new ItemStack(Material.BEDROCK);
					}
					
					// If the current Y has been changed, the item is NOT on top of the hologram.
					if (currentY != this.y) {
						// Extra space for the floating item, blocks are smaller
						if (ItemUtils.appearsAsBlock(icon.getType())) {
							currentY -= 0.27;
						} else {
							currentY -= 0.52;
						}
					}
					
					FloatingItemDoubleEntity lineEntity = new FloatingItemDoubleEntity(icon);
					lineEntity.spawn(this, bukkitWorld, x, currentY, z);
					linesEntities.add(lineEntity);
					
					// And some more space below.
					currentY -= 0.05;
					
				} else {
				
					HologramLine lineEntity = new HologramLine(text);
					lineEntity.spawn(this, bukkitWorld, x, currentY, z);
					linesEntities.add(lineEntity);
				
					// Don't track placeholders for API holograms!
					// HolographicDisplays.getPlaceholderManager().trackIfNecessary(lineEntity.getHorse());
				}
				
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
