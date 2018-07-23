package com.gmail.filoghost.holographicdisplays.object.line;

import com.gmail.filoghost.holographicdisplays.constant.Offsets;
import com.gmail.filoghost.holographicdisplays.util.bukkit.BukkitVersion;
import org.bukkit.World;

import com.gmail.filoghost.holographicdisplays.HolographicDisplays;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.entity.NMSEntityBase;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.entity.NMSSlime;
import com.gmail.filoghost.holographicdisplays.object.CraftHologram;

/**
 * A touch slime that can be applied to a line.
 */
public class CraftTouchSlimeLine extends CraftHologramLine {
	
	// The touchable piece associated with this piece
	private CraftTouchableLine touchablePiece;
	
	private NMSSlime nmsSlime;
	private NMSEntityBase nmsVehicle;

	
	protected CraftTouchSlimeLine(CraftHologram parent, CraftTouchableLine touchablePiece) {
		super(0.5, parent);
		this.touchablePiece = touchablePiece;
	}

	public CraftTouchableLine getTouchablePiece() {
		return touchablePiece;
	}


	@Override
	public void spawn(World world, double x, double y, double z) {
		super.spawn(world, x, y, z);
		
		double offset = getSlimeOffset();
		
		nmsSlime = HolographicDisplays.getNMSManager().spawnNMSSlime(world, x, y + offset, z, this);
		
		if (BukkitVersion.isAtLeast(BukkitVersion.V1_8_R1)) {
			nmsVehicle = HolographicDisplays.getNMSManager().spawnNMSArmorStand(world, x, y + offset, z, this);
		} else {
			nmsVehicle = HolographicDisplays.getNMSManager().spawnNMSWitherSkull(world, x, y + offset, z, this);
		}
		
		nmsSlime.setPassengerOfNMS(nmsVehicle);
			
		nmsSlime.setLockTick(true);
		nmsVehicle.setLockTick(true);
	}

	
	@Override
	public void despawn() {
		super.despawn();
		
		if (nmsSlime != null) {
			nmsSlime.killEntityNMS();
			nmsSlime = null;
		}
		
		if (nmsVehicle != null) {
			nmsVehicle.killEntityNMS();
			nmsVehicle = null;
		}
	}

	
	@Override
	public void teleport(double x, double y, double z) {
		
		double offset = getSlimeOffset();
		
		if (nmsVehicle != null) {
			nmsVehicle.setLocationNMS(x, y + offset, z);
		}
		
		if (nmsSlime != null) {
			nmsSlime.setLocationNMS(x, y + offset, z);
		}
	}

	@Override
	public int[] getEntitiesIDs() {
		if (isSpawned()) {
			return new int[] {nmsVehicle.getIdNMS(), nmsSlime.getIdNMS()};
		} else {
			return new int[0];
		}
	}

	public NMSSlime getNmsSlime() {
		return nmsSlime;
	}

	public NMSEntityBase getNmsVehicle() {
		return nmsVehicle;
	}
	
	private double getSlimeOffset() {
		if (BukkitVersion.isAtLeast(BukkitVersion.V1_9_R1)) {
			return Offsets.ARMOR_STAND_WITH_SLIME_1_9;
		} else if (BukkitVersion.isAtLeast(BukkitVersion.V1_8_R1)) {
			return Offsets.ARMOR_STAND_WITH_SLIME;
		} else {
			return Offsets.WITHER_SKULL_WITH_SLIME;
		}
	}

	@Override
	public String toString() {
		return "CraftTouchSlimeLine [touchablePiece=" + touchablePiece + "]";
	}
	
}
