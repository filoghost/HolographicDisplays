package com.gmail.filoghost.holographicdisplays.object;

import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

import com.gmail.filoghost.holograms.api.TouchHandler;
import com.gmail.filoghost.holograms.api.replacements.OldTouchHandlerWrapper;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.line.TouchableLine;
import com.gmail.filoghost.holographicdisplays.disk.Configuration;
import com.gmail.filoghost.holographicdisplays.object.line.CraftHologramLine;
import com.gmail.filoghost.holographicdisplays.object.line.CraftItemLine;
import com.gmail.filoghost.holographicdisplays.object.line.CraftTextLine;
import com.gmail.filoghost.holographicdisplays.placeholder.PlaceholdersManager;
import com.gmail.filoghost.holographicdisplays.util.Utils;
import com.gmail.filoghost.holographicdisplays.util.Validator;

/**
 * This class is only used by the plugin itself. Do not attempt to use it.
 * It still implements the old API, but it's temporary.
 */
@SuppressWarnings("deprecation")
public class CraftHologram implements Hologram, com.gmail.filoghost.holograms.api.Hologram {
	
	// Position variables.
	private World world;
	private double x, y, z;
	private int chunkX, chunkZ;
	
	// The entities that represent lines.
	private final List<CraftHologramLine> lines;
	
	private CraftVisibilityManager visibilityManager;
	private boolean allowPlaceholders;
	private long creationTimestamp;
	private boolean deleted;
	
	public CraftHologram(Location location) {
		Validator.notNull(location, "location");
		updateLocation(location.getWorld(), location.getX(), location.getY(), location.getZ());
		
		lines = Utils.newList();
		allowPlaceholders = false;
		creationTimestamp = System.currentTimeMillis();
		visibilityManager = new CraftVisibilityManager(this);
	}
	
	public boolean isInChunk(Chunk chunk) {
		return chunk.getX() == chunkX && chunk.getZ() == chunkZ;
	}
	
	@Override
	public World getWorld() {
		return world;
	}
	
	@Override
	public double getX() {
		return x;
	}
	
	@Override
	public double getY() {
		return y;
	}
	
	@Override
	public double getZ() {
		return z;
	}
	
	@Override
	public Location getLocation() {
		return new Location(world, x, y, z);
	}
	
	private void updateLocation(World world, double x, double y, double z) {
		Validator.notNull(world, "world");
		
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
		chunkX = Utils.floor(x) >> 4;
		chunkZ = Utils.floor(z) >> 4;
	}
	
	@Override
	public boolean isDeleted() {
		return deleted;
	}
	
	@Override
	public void delete() {
		if (!deleted) {
			deleted = true;
			clearLines();
		}
	}
	
	public List<CraftHologramLine> getLinesUnsafe() {
		return lines;
	}
	
	@Override
	public CraftHologramLine getLine(int index) {
		return lines.get(index);
	}
	
	@Override
	public CraftTextLine appendTextLine(String text) {
		Validator.isTrue(!deleted, "hologram already deleted");
		
		CraftTextLine line = new CraftTextLine(this, text);
		lines.add(line);
		refreshSingleLines();
		return line;
	}
	
	@Override
	public CraftItemLine appendItemLine(ItemStack itemStack) {
		Validator.isTrue(!deleted, "hologram already deleted");
		Validator.notNull(itemStack, "itemStack");
		
		CraftItemLine line = new CraftItemLine(this, itemStack);
		lines.add(line);
		refreshSingleLines();
		return line;
	}
	
	@Override
	public CraftTextLine insertTextLine(int index, String text) {
		Validator.isTrue(!deleted, "hologram already deleted");
		
		CraftTextLine line = new CraftTextLine(this, text);
		lines.add(index, line);
		refreshSingleLines();
		return line;
	}
	
	@Override
	public CraftItemLine insertItemLine(int index, ItemStack itemStack) {
		Validator.isTrue(!deleted, "hologram already deleted");
		Validator.notNull(itemStack, "itemStack");
		
		CraftItemLine line = new CraftItemLine(this, itemStack);
		lines.add(index, line);
		refreshSingleLines();
		return line;
	}
	
	@Override
	public void removeLine(int index) {
		Validator.isTrue(!deleted, "hologram already deleted");
		
		lines.remove(index).despawn();
		refreshSingleLines();
	}
	
	public void removeLine(CraftHologramLine line) {
		Validator.isTrue(!deleted, "hologram already deleted");
		
		lines.remove(line);
		line.despawn();
		refreshSingleLines();
	}
	
	@Override
	public void clearLines() {
		for (CraftHologramLine line : lines) {
			line.despawn();
		}
		
		lines.clear();
	}
	
	@Override
	public int size() {
		return lines.size();
	}
	
	@Override
	public double getHeight() {
		if (lines.isEmpty()) {
			return 0;
		}
		
		double height = 0.0;
		
		for (CraftHologramLine line : lines) {
			height += line.getHeight();
		}
		
		height += Configuration.spaceBetweenLines * (lines.size() - 1);
		return height;
	}
	
	@Override
	public CraftVisibilityManager getVisibilityManager() {
		return visibilityManager;
	}
	
	
	@Override
	public long getCreationTimestamp() {
		return creationTimestamp;
	}

	@Override
	public boolean isAllowPlaceholders() {
		return allowPlaceholders;
	}

	@Override
	public void setAllowPlaceholders(boolean allowPlaceholders) {
		if (this.allowPlaceholders != allowPlaceholders) {
			
			if (allowPlaceholders) {
				// Now allowed, previously weren't
				for (CraftHologramLine line : lines) {
					if (line instanceof CraftTextLine) {
						PlaceholdersManager.trackIfNecessary((CraftTextLine) line);
					}
				}
				
			} else {
				
				// Now not allowed
				for (CraftHologramLine line : lines) {
					if (line instanceof CraftTextLine) {
						PlaceholdersManager.untrack((CraftTextLine) line);
					}
				}
			}
			
			this.allowPlaceholders = allowPlaceholders;
		}
	}
	

	public void refreshAll() {
		if (world.isChunkLoaded(chunkX, chunkZ)) {
			spawnEntities();
		}
	}
	
	public void refreshSingleLines() {
		if (world.isChunkLoaded(chunkX, chunkZ)) {
			
			double currentY = this.y;
			boolean first = true;
			
			for (CraftHologramLine line : lines) {
				
				currentY -= line.getHeight();
				
				if (first) {
					first = false;
				} else {
					currentY -= Configuration.spaceBetweenLines;
				}
				
				if (line.isSpawned()) {
					line.teleport(x, currentY, z);
				} else {
					line.spawn(world, x, currentY, z);
					if (allowPlaceholders && line instanceof CraftTextLine) {
						PlaceholdersManager.trackIfNecessary((CraftTextLine) line);
					}
				}
			}
		}
	}
	
	/**
	 * Forces the entities to spawn, without checking if the chunk is loaded.
	 */
	public void spawnEntities() {
		Validator.isTrue(!deleted, "hologram already deleted");
		
		despawnEntities();

		double currentY = this.y;
		boolean first = true;
		
		for (CraftHologramLine line : lines) {
			
			currentY -= line.getHeight();
			
			if (first) {
				first = false;
			} else {
				currentY -= Configuration.spaceBetweenLines;
			}
			
			line.spawn(world, x, currentY, z);
			if (allowPlaceholders && line instanceof CraftTextLine) {
				PlaceholdersManager.trackIfNecessary((CraftTextLine) line);
			}
		}
	}
	
	/**
	 * Called by the PluginHologramManager when the chunk is unloaded.
	 */
	public void despawnEntities() {
		for (CraftHologramLine piece : lines) {
			piece.despawn();
		}
	}
	
	@Override
	public void teleport(Location location) {
		Validator.notNull(location, "location");
		teleport(location.getWorld(), location.getX(), location.getY(), location.getZ());
	}
	
	@Override
	public void teleport(World world, double x, double y, double z) {
		Validator.isTrue(!deleted, "hologram already deleted");
		Validator.notNull(world, "world");
		
		updateLocation(world, x, y, z);
		
		if (this.world != world) {
			despawnEntities();
			refreshAll();
			return;
		}
		
		double currentY = y;
		boolean first = true;
		
		for (CraftHologramLine line : lines) {
			
			if (!line.isSpawned()) {
				continue;
			}
			
			currentY -= line.getHeight();
			
			if (first) {
				first = false;
			} else {
				currentY -= Configuration.spaceBetweenLines;
			}
			
			line.teleport(x, currentY, z);
		}
	}

	@Override
	public String toString() {
		return "CraftHologram [world=" + world + ", x=" + x + ", y=" + y + ", z=" + z + ", lines=" + lines + ", deleted=" + deleted + "]";
	}
	
	/**
	 * Old API methods, will be removed soon
	 */

	@Override
	@Deprecated
	public boolean update() {
		return true;
	}

	@Override
	@Deprecated
	public void hide() {

	}

	@Override
	@Deprecated
	public void addLine(String text) {
		appendTextLine(text);
	}

	@Override
	@Deprecated
	public void setLine(int index, String text) {
		lines.get(index).despawn();
		lines.set(index, new CraftTextLine(this, text));
	}

	@Override
	@Deprecated
	public void insertLine(int index, String text) {
		insertLine(index, text);
	}

	@Override
	@Deprecated
	public String[] getLines() {
		return null;
	}

	@Override
	@Deprecated
	public int getLinesLength() {
		return size();
	}

	@Override
	@Deprecated
	public void setLocation(Location location) {
		teleport(location);
	}

	@Override
	@Deprecated
	public void setTouchHandler(TouchHandler handler) {
		if (size() > 0) {
			TouchableLine line0 = ((TouchableLine) getLine(0));
			
			if (handler != null) {
				line0.setTouchHandler(new OldTouchHandlerWrapper(this, handler));
			} else {
				line0.setTouchHandler(null);
			}
		}
	}

	@Override
	@Deprecated
	public TouchHandler getTouchHandler() {
		return null;
	}

	@Override
	@Deprecated
	public boolean hasTouchHandler() {
		return false;
	}

	/**
	 * About: equals() and hashcode()
	 * Two holograms can never be equal. Even if they have the same position and the same elements, they are still two different objects.
	 * The equals and hashcode methods are not overriden, two holograms are equal only if they have the same memory address.
	 */
	
}
