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
package me.filoghost.holographicdisplays.listener;

import me.filoghost.holographicdisplays.HolographicDisplays;
import me.filoghost.holographicdisplays.api.Hologram;
import me.filoghost.holographicdisplays.api.handler.PickupHandler;
import me.filoghost.holographicdisplays.commands.Colors;
import me.filoghost.holographicdisplays.commands.Strings;
import me.filoghost.holographicdisplays.disk.Configuration;
import me.filoghost.holographicdisplays.nms.interfaces.ItemPickupManager;
import me.filoghost.holographicdisplays.nms.interfaces.NMSManager;
import me.filoghost.holographicdisplays.nms.interfaces.entity.NMSEntityBase;
import me.filoghost.holographicdisplays.object.CraftHologram;
import me.filoghost.holographicdisplays.object.NamedHologramManager;
import me.filoghost.holographicdisplays.object.PluginHologram;
import me.filoghost.holographicdisplays.object.PluginHologramManager;
import me.filoghost.holographicdisplays.object.line.CraftTouchSlimeLine;
import me.filoghost.holographicdisplays.util.ConsoleLogger;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.GameMode;
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

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class MainListener implements Listener, ItemPickupManager {
	
	private NMSManager nmsManager;
	
	private Map<Player, Long> anticlickSpam = new HashMap<>();
	
	public MainListener(NMSManager nmsManager) {
		this.nmsManager = nmsManager;
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onChunkUnload(ChunkUnloadEvent event) {
		for (Entity entity : event.getChunk().getEntities()) {
			if (!entity.isDead()) {
				NMSEntityBase entityBase = nmsManager.getNMSEntityBase(entity);
				
				if (entityBase != null) {
					((CraftHologram) entityBase.getHologramLine().getParent()).despawnEntities();
				}
			}
		}
	}
	
	@EventHandler (priority = EventPriority.MONITOR)
	public void onChunkLoad(ChunkLoadEvent event) {
		Chunk chunk = event.getChunk();
		
		// Other plugins could call this event wrongly, check if the chunk is actually loaded.
		if (chunk.isLoaded()) {
			
			// In case another plugin loads the chunk asynchronously always make sure to load the holograms on the main thread.
			if (Bukkit.isPrimaryThread()) {
				processChunkLoad(chunk);
			} else {
				Bukkit.getScheduler().runTask(HolographicDisplays.getInstance(), () -> processChunkLoad(chunk));
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
		if (event.getRightClicked().getType() != EntityType.SLIME) {
			return;
		}
			
		Player clicker = event.getPlayer();
		if (clicker.getGameMode() == GameMode.SPECTATOR) {
			return;
		}
		
		NMSEntityBase entityBase = nmsManager.getNMSEntityBase(event.getRightClicked());
		if (entityBase == null || !(entityBase.getHologramLine() instanceof CraftTouchSlimeLine)) {
			return;
		}
		
		CraftTouchSlimeLine touchSlime = (CraftTouchSlimeLine) entityBase.getHologramLine();
		if (touchSlime.getTouchablePiece().getTouchHandler() == null || !touchSlime.getParent().getVisibilityManager().isVisibleTo(clicker)) {
			return;
		}
		
		Long lastClick = anticlickSpam.get(clicker);
		if (lastClick != null && System.currentTimeMillis() - lastClick.longValue() < 100) {
			return;
		}
		
		anticlickSpam.put(event.getPlayer(), System.currentTimeMillis());
		
		try {
			touchSlime.getTouchablePiece().getTouchHandler().onTouch(event.getPlayer());
		} catch (Throwable t) {
			Plugin plugin = touchSlime.getParent() instanceof PluginHologram ? ((PluginHologram) touchSlime.getParent()).getOwner() : HolographicDisplays.getInstance();
			ConsoleLogger.log(Level.WARNING, "The plugin " + plugin.getName() + " generated an exception when the player " + event.getPlayer().getName() + " touched a hologram.", t);
		}
	}
	
	@Override
	public void handleItemLinePickup(Player player, PickupHandler pickupHandler, Hologram hologram) {
		try {
			if (hologram.getVisibilityManager().isVisibleTo(player)) {
				pickupHandler.onPickup(player);
			}
		} catch (Throwable t) {
			Plugin plugin = hologram instanceof PluginHologram ? ((PluginHologram) hologram).getOwner() : HolographicDisplays.getInstance();
			ConsoleLogger.log(Level.WARNING, "The plugin " + plugin.getName() + " generated an exception when the player " + player.getName() + " picked up an item from a hologram.", t);
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
	public void onQuit(PlayerQuitEvent event) {
		anticlickSpam.remove(event.getPlayer());
	}
}
