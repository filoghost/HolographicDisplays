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
package com.gmail.filoghost.holographicdisplays.object.line;

import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Location;
import org.bukkit.World;

import com.gmail.filoghost.holographicdisplays.HolographicDisplays;
import com.gmail.filoghost.holographicdisplays.api.handler.TouchHandler;
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.entity.NMSNameable;
import com.gmail.filoghost.holographicdisplays.object.CraftHologram;
import com.gmail.filoghost.holographicdisplays.placeholder.PlaceholdersManager;
import com.gmail.filoghost.holographicdisplays.placeholder.RelativePlaceholder;
import com.gmail.filoghost.holographicdisplays.util.Offsets;
import com.gmail.filoghost.holographicdisplays.util.Utils;

public class CraftTextLine extends CraftTouchableLine implements TextLine {

	private String text;
	private List<RelativePlaceholder> relativePlaceholders;
	private NMSNameable nmsNameble;
	
	
	public CraftTextLine(CraftHologram parent, String text) {
		super(0.23, parent);
		setText(text);
	}
	
	
	@Override
	public String getText() {
		return text;
	}
	
	@Override
	public void setText(String text) {
		this.text = text;
		
		if (nmsNameble != null) {
			if (text != null && !text.isEmpty()) {
				nmsNameble.setCustomNameNMS(text);
				if (getParent().isAllowPlaceholders()) {
					PlaceholdersManager.trackIfNecessary(this);
				}
			} else {
				nmsNameble.setCustomNameNMS(""); // It will not appear
				if (getParent().isAllowPlaceholders()) {
					PlaceholdersManager.untrack(this);
				}
			}
		}
		
		if (text != null) {
			for (RelativePlaceholder relativePlaceholder : RelativePlaceholder.getRegistry()) {
				if (text.contains(relativePlaceholder.getTextPlaceholder())) {
					if (relativePlaceholders == null) {
						relativePlaceholders = Utils.newList();
					}
					relativePlaceholders.add(relativePlaceholder);
				}
			}
		}
		
		// Deallocate the list if unused
		if (relativePlaceholders != null && relativePlaceholders.isEmpty()) {
			relativePlaceholders = null;
		}
	}
	
	@Override
	public void setTouchHandler(TouchHandler touchHandler) {
		if (nmsNameble != null) {
			Location loc = nmsNameble.getBukkitEntityNMS().getLocation();
			super.setTouchHandler(touchHandler, loc.getWorld(), loc.getX(), loc.getY() - getTextOffset(), loc.getZ());
		} else {
			super.setTouchHandler(touchHandler, null, 0, 0, 0);
		}
	}

	@Override
	public void spawn(World world, double x, double y, double z) {
		super.spawn(world, x, y, z);
			
		nmsNameble = HolographicDisplays.getNMSManager().spawnNMSArmorStand(world, x, y + getTextOffset(), z, this);

		if (text != null && !text.isEmpty()) {
			nmsNameble.setCustomNameNMS(text);
		}
		
		nmsNameble.setLockTick(true);
	}

	
	@Override
	public void despawn() {
		super.despawn();
		
		if (nmsNameble != null) {
			nmsNameble.killEntityNMS();
			nmsNameble = null;
		}
	}
	
	
	@Override
	public Collection<RelativePlaceholder> getRelativePlaceholders() {
		return relativePlaceholders;
	}

	
	@Override
	public void teleport(double x, double y, double z) {
		super.teleport(x, y, z);
		
		if (nmsNameble != null) {
			nmsNameble.setLocationNMS(x, y + getTextOffset(), z);
		}
	}
	
	@Override
	public int[] getEntitiesIDs() {
		if (isSpawned()) {
			if (touchSlime != null) {
				return ArrayUtils.add(touchSlime.getEntitiesIDs(), nmsNameble.getIdNMS());
			} else {
				return new int[] {nmsNameble.getIdNMS()};
			}
		} else {
			return new int[0];
		}
	}

	public NMSNameable getNmsNameble() {
		return nmsNameble;
	}

	private double getTextOffset() {
		return Offsets.ARMOR_STAND_ALONE;
	}

	@Override
	public String toString() {
		return "CraftTextLine [text=" + text + "]";
	}
	
}
