package com.gmail.filoghost.holographicdisplays.object.line;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Location;
import org.bukkit.World;

import com.gmail.filoghost.holographicdisplays.HolographicDisplays;
import com.gmail.filoghost.holographicdisplays.api.handler.TouchHandler;
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.entity.NMSNameable;
import com.gmail.filoghost.holographicdisplays.object.CraftHologram;
import com.gmail.filoghost.holographicdisplays.placeholder.PlaceholdersManager;
import com.gmail.filoghost.holographicdisplays.util.NMSVersion;
import com.gmail.filoghost.holographicdisplays.util.Offsets;

public class CraftTextLine extends CraftTouchableLine implements TextLine {

	private String text;
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
	}
	
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
		if (NMSVersion.isGreaterEqualThan(NMSVersion.v1_9_R1)) {
			return Offsets.ARMOR_STAND_ALONE_1_9;
		} else {
			return Offsets.ARMOR_STAND_ALONE;
		}
	}

	@Override
	public String toString() {
		return "CraftTextLine [text=" + text + "]";
	}
	
}
