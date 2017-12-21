package com.gmail.filoghost.holographicdisplays;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 * A very simple and lightweight updater, without download features.
 * @autor filoghost
 */
public final class SimpleUpdater {
	
	public interface ResponseHandler {
		
		/**
		 * Called when the updater finds a new version.
		 * @param newVersion - the new version
		 */
		public void onUpdateFound(final String newVersion);
		
	}

	private Plugin plugin;
	private int projectId;

	public SimpleUpdater(Plugin plugin, int projectId) {
		if (plugin == null) {
			throw new NullPointerException("Plugin cannot be null");
		}
		
		this.plugin = plugin;
		this.projectId = projectId;
	}
	
	/**
	 * This method creates a new async thread to check for updates.
	 * @param responseHandler the response handler
	 */
	public void checkForUpdates(final ResponseHandler responseHandler) {
		Thread updaterThread = new Thread(new Runnable() {
			@Override
			public void run() {

				try {

					JSONArray filesArray = (JSONArray) readJson("https://api.curseforge.com/servermods/files?projectIds=" + projectId);

					if (filesArray.size() == 0) {
						// The array cannot be empty, there must be at least one file. The project ID is not valid or curse returned a wrong response.
						return;
					}
					
					String updateName = (String) ((JSONObject) filesArray.get(filesArray.size() - 1)).get("name");
					final String newVersion = extractVersion(updateName);
					
					if (newVersion == null) {
						throw new NumberFormatException();
					}
					
					if (isNewerVersion(newVersion)) {
						Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {

							@Override
							public void run() {
								responseHandler.onUpdateFound(newVersion);
							}
						});
					}

				} catch (IOException e) {
					plugin.getLogger().warning("Could not contact BukkitDev to check for updates.");
				} catch (NumberFormatException e) {
					plugin.getLogger().warning("The author of this plugin has misconfigured the Updater system.");
					plugin.getLogger().warning("File versions should follow the format 'PluginName vVERSION'");
		            plugin.getLogger().warning("Please notify the author of this error.");
				} catch (Exception e) {
					e.printStackTrace();
					plugin.getLogger().warning("Unable to check for updates: unhandled exception.");
				}

			}
		});
		updaterThread.start();
	}
	
	private Object readJson(String url) throws MalformedURLException, IOException {
		
		URLConnection conn = new URL(url).openConnection();
		conn.setConnectTimeout(5000);
		conn.setReadTimeout(8000);
		conn.addRequestProperty("User-Agent", "Updater (by filoghost)");
		conn.setDoOutput(true);

		return JSONValue.parse(new BufferedReader(new InputStreamReader(conn.getInputStream())));
	}
	
	/**
	 * Compare the version found with the plugin's version, from an array of integer separated by full stops.
	 * Examples:
	 *    v1.2 > v1.12
	 *    v2.1 = v2.01
	 * @param remoteVersion the remote version of the plugin
	 * @return true if the remove version is newer
	 */
	private boolean isNewerVersion(String remoteVersion) {
		String pluginVersion = plugin.getDescription().getVersion();
		
		if (pluginVersion == null || !pluginVersion.matches("v?[0-9\\.]+")) {
			// Do not throw exceptions, just consider it as v0.
			pluginVersion = "0";
		}
		
		if (!remoteVersion.matches("v?[0-9\\.]+")) {
			// Should always be checked before by this class.
			throw new IllegalArgumentException("fetched version's format is incorrect");
		}
		
		// Remove all the "v" from the versions, replace multiple full stops with a single full stop, and split them.
		String[] pluginVersionSplit = pluginVersion.replace("v", "").replaceAll("[\\.]{2,}", ".").split("\\.");
		String[] remoteVersionSplit = remoteVersion.replace("v", "").replaceAll("[\\.]{2,}", ".").split("\\.");
		
		int longest = Math.max(pluginVersionSplit.length, remoteVersionSplit.length);
		
		int[] pluginVersionArray = new int[longest];
		int[] remoteVersionArray = new int[longest];
		
		for (int i = 0; i < pluginVersionSplit.length; i++) {
			pluginVersionArray[i] = Integer.parseInt(pluginVersionSplit[i]);
		}
		
		for (int i = 0; i < remoteVersionSplit.length; i++) {
			remoteVersionArray[i] = Integer.parseInt(remoteVersionSplit[i]);
		}
		
		for (int i = 0; i < longest; i++) {
			int diff = remoteVersionArray[i] - pluginVersionArray[i];
			if (diff > 0) {
				return true;
			} else if (diff < 0) {
				return false;
			}
			
			// Continue the loop until diff = 0.
		}
		
		return false;
	}
	
	private String extractVersion(String input) {
		Matcher matcher = Pattern.compile("v[0-9\\.]+").matcher(input);
		
		String result = null;
		if (matcher.find()) {
			result = matcher.group();
		}
		
		return result;
	}
}
