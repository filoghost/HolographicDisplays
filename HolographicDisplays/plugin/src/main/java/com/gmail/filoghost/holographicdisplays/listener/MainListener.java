package com.gmail.filoghost.holographicdisplays.listener;

import java.util.Map;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.plugin.Plugin;

import com.gmail.filoghost.holographicdisplays.HolographicDisplays;
import com.gmail.filoghost.holographicdisplays.api.handler.PickupHandler;
import com.gmail.filoghost.holographicdisplays.commands.Colors;
import com.gmail.filoghost.holographicdisplays.commands.Strings;
import com.gmail.filoghost.holographicdisplays.disk.Configuration;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.NMSManager;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.entity.NMSEntityBase;
import com.gmail.filoghost.holographicdisplays.object.CraftHologram;
import com.gmail.filoghost.holographicdisplays.object.NamedHologramManager;
import com.gmail.filoghost.holographicdisplays.object.PluginHologram;
import com.gmail.filoghost.holographicdisplays.object.PluginHologramManager;
import com.gmail.filoghost.holographicdisplays.object.line.CraftTouchSlimeLine;
import com.gmail.filoghost.holographicdisplays.util.Utils;

public class MainListener implements Listener {
	
	private NMSManager nmsManager;
	
	private Map<Player, Long> anticlickSpam = Utils.newMap();
	
	public MainListener(NMSManager nmsManager) {
		this.nmsManager = nmsManager;
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onChunkUnload(ChunkUnloadEvent event) {
		for (Entity entity : event.getChunk().getEntities()) {
			if (!entity.isDead()) {
				NMSEntityBase entityBase = nmsManager.getNMSEntityBase(entity);
				
				if (entityBase != null) {
					entityBase.getHologramLine().getParent().despawnEntities();
				}
			}
		}
	}
	
	@EventHandler (priority = EventPriority.MONITOR)
	public void onChunkLoad(ChunkLoadEvent event) {
		final Chunk chunk = event.getChunk();
		
		// Other plugins could call this event wrongly, check if the chunk is actually loaded.
		if (chunk.isLoaded()) {
			
			// In case another plugin loads the chunk asynchronously always make sure to load the holograms on the main thread.
			if (Bukkit.isPrimaryThread()) {
				processChunkLoad(chunk);
			} else {
				Bukkit.getScheduler().runTask(HolographicDisplays.getInstance(), new Runnable() {
					@Override
					public void run() {
						processChunkLoad(chunk);
					}
				});
			}
		}
	}
	
	// This method should be always called synchronously.
	public void processChunkLoad(Chunk chunk) {
		NamedHologramManager.onChunkLoad(chunk);
		PluginHologramManager.onChunkLoad(chunk);
	}
	
	@EventHandler (priority = EventPriority.MONITOR, ignoreCancelled = false)
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		if (nmsManager.isNMSEntityBase(event.getEntity())) {
			if (event.isCancelled()) {
				event.setCancelled(false);
			}
		}
	}
	
	@EventHandler (priority = EventPriority.MONITOR, ignoreCancelled = false)
	public void onProjectileLaunch(ProjectileLaunchEvent event) {
		if (nmsManager.isNMSEntityBase(event.getEntity())) {
			if (event.isCancelled()) {
				event.setCancelled(false);
			}
		}
	}
	
	@EventHandler (priority = EventPriority.MONITOR, ignoreCancelled = false)
	public void onItemSpawn(ItemSpawnEvent event) {
		if (nmsManager.isNMSEntityBase(event.getEntity())) {
			if (event.isCancelled()) {
				event.setCancelled(false);
			}
		}
	}
	
	@EventHandler (priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onSlimeInteract(PlayerInteractEntityEvent event) {
		if (event.getRightClicked().getType() == EntityType.SLIME) {
			
			Player clicker = event.getPlayer();
			NMSEntityBase entityBase = nmsManager.getNMSEntityBase(event.getRightClicked());
			
			if (entityBase != null && entityBase.getHologramLine() instanceof CraftTouchSlimeLine && !isSpectatorMode(clicker)) {
				
				CraftTouchSlimeLine touchSlime = (CraftTouchSlimeLine) entityBase.getHologramLine();
				
				if (touchSlime.getTouchablePiece().getTouchHandler() != null && touchSlime.getParent().getVisibilityManager().isVisibleTo(clicker)) {
					
					Long lastClick = anticlickSpam.get(clicker);
					if (lastClick != null && System.currentTimeMillis() - lastClick.longValue() < 100) {
						return;
					}
					
					anticlickSpam.put(event.getPlayer(), System.currentTimeMillis());
					
					try {
						touchSlime.getTouchablePiece().getTouchHandler().onTouch(event.getPlayer());
					} catch (Throwable t) {
						Plugin plugin = touchSlime.getParent() instanceof PluginHologram ? ((PluginHologram) touchSlime.getParent()).getOwner() : HolographicDisplays.getInstance();
						HolographicDisplays.getInstance().getLogger().log(Level.WARNING, "The plugin " + plugin.getName() + " generated an exception when the player " + event.getPlayer().getName() + " touched a hologram.", t);
					}
				}
			}
		}
	}
	
	public static boolean isSpectatorMode(Player player) {
		return player.getGameMode().toString().equals("SPECTATOR");
	}
	
	public static void handleItemLinePickup(Player player, PickupHandler pickupHandler, CraftHologram hologram) {
		try {
			if (hologram.getVisibilityManager().isVisibleTo(player)) {
				pickupHandler.onPickup(player);
			}
		} catch (Throwable t) {
			Plugin plugin = hologram instanceof PluginHologram ? ((PluginHologram) hologram).getOwner() : HolographicDisplays.getInstance();
			HolographicDisplays.getInstance().getLogger().log(Level.WARNING, "The plugin " + plugin.getName() + " generated an exception when the player " + player.getName() + " picked up an item from a hologram.", t);
		}
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		if (Configuration.updateNotification && HolographicDisplays.getNewVersion() != null) {
			if (event.getPlayer().hasPermission(Strings.BASE_PERM + "update")) {
				event.getPlayer().sendMessage(Colors.PRIMARY_SHADOW + "[HolographicDisplays] " + Colors.PRIMARY + "Found an update: " + HolographicDisplays.getNewVersion() + ". Download:");
				event.getPlayer().sendMessage(Colors.PRIMARY_SHADOW + ">> " + Colors.PRIMARY + "http://dev.bukkit.org/bukkit-plugins/holographic-displays");
			}
		}
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent event) {
		anticlickSpam.remove(event.getPlayer());
	}
}
