package com.gmail.filoghost.holograms.listener;

import org.bukkit.Chunk;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

import com.gmail.filoghost.holograms.Configuration;
import com.gmail.filoghost.holograms.api.FloatingItem;
import com.gmail.filoghost.holograms.api.Hologram;
import com.gmail.filoghost.holograms.commands.Messages;
import com.gmail.filoghost.holograms.nms.interfaces.NmsManager;
import com.gmail.filoghost.holograms.object.APIFloatingItemManager;
import com.gmail.filoghost.holograms.object.APIHologramManager;
import com.gmail.filoghost.holograms.object.HologramBase;
import com.gmail.filoghost.holograms.object.HologramManager;

public class MainListener implements Listener {
	
	private NmsManager nmsManager;
	
	public MainListener(NmsManager nmsManager) {
		this.nmsManager = nmsManager;
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onChunkUnload(ChunkUnloadEvent event) {
		for (Entity entity : event.getChunk().getEntities()) {
			if (!entity.isDead()) {
				HologramBase multiEntity = nmsManager.getParentHologram(entity);
				
				if (multiEntity != null) {
					multiEntity.hide();
				}
			}
		}
	}
	
	@EventHandler (priority = EventPriority.MONITOR)
	public void onChunkLoad(ChunkLoadEvent event) {
		Chunk chunk = event.getChunk();
		HologramManager.onChunkLoad(chunk);
		APIHologramManager.onChunkLoad(chunk);
		APIFloatingItemManager.onChunkLoad(chunk);
	}
	
	@EventHandler (priority = EventPriority.MONITOR, ignoreCancelled = false)
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		if (nmsManager.isBasicEntityNMS(event.getEntity())) {
			if (event.isCancelled()) {
				event.setCancelled(false);
			}
		}
	}
	
	@EventHandler (priority = EventPriority.MONITOR, ignoreCancelled = false)
	public void onProjectileLaunch(ProjectileLaunchEvent event) {
		if (nmsManager.isBasicEntityNMS(event.getEntity())) {
			if (event.isCancelled()) {
				event.setCancelled(false);
			}
		}
	}
	
	@EventHandler (priority = EventPriority.MONITOR, ignoreCancelled = false)
	public void onItemSpawn(ItemSpawnEvent event) {
		if (nmsManager.isBasicEntityNMS(event.getEntity())) {
			if (event.isCancelled()) {
				event.setCancelled(false);
			}
		}
	}
	
	@EventHandler (priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onSlimeInteract(PlayerInteractEntityEvent event) {
		if (event.getRightClicked().getType() == EntityType.SLIME) {
			HologramBase base = nmsManager.getParentHologram(event.getRightClicked());
			if (base == null) return;
			
			if (base instanceof Hologram) {
				Hologram textHologram = (Hologram) base;
				if (textHologram.hasTouchHandler()) {
					textHologram.getTouchHandler().onTouch(textHologram, event.getPlayer());
				}
			} else if (base instanceof FloatingItem) {
				FloatingItem floatingItem = (FloatingItem) base;
				if (floatingItem.hasTouchHandler()) {
					floatingItem.getTouchHandler().onTouch(floatingItem, event.getPlayer());
				}
			}
		}
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		if (Configuration.updateNotification && Configuration.newVersion != null) {
			if (event.getPlayer().hasPermission(Messages.MAIN_PERMISSION)) {
				event.getPlayer().sendMessage("§3[HolographicDisplays] §bFound an update: " + Configuration.newVersion + ". Download:");
				event.getPlayer().sendMessage("§3>> §bhttp://dev.bukkit.org/bukkit-plugins/holographic-displays");
			}
		}
	}
}
