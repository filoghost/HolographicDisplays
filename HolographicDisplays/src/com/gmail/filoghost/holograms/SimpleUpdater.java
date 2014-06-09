package com.gmail.filoghost.holograms;

import java.io.BufferedReader;
import java.io.File;
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

public final class SimpleUpdater {

	private Plugin plugin;
	@SuppressWarnings("unused")
	private File pluginFile;
	private int projectId;

	public SimpleUpdater(Plugin plugin, int projectId, File pluginFile) {
		notNull(plugin, "Plugin cannot be null");
		notNull(pluginFile, "Plugin file cannot be null");
		
		this.plugin = plugin;
		this.pluginFile = pluginFile;
		this.projectId = projectId;
	}

	/**
	 * Checks for updated with the default response handler.
	 * You have just to provide the link to the bukkit dev's page of the plugin.
	 * 
	 * @param bukkitDevPageLink The link to the bukkit dev's page of the plugin.
	 */
	public void checkForUpdates(final String bukkitDevPageLink) {
		checkForUpdates(new ResponseHandler() {
			
			@Override
			public void onUpdateFound(final String newVersion) {
				Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {

					@Override
					public void run() {
						plugin.getLogger().info("Found a new version available: " + newVersion);
						plugin.getLogger().info("Download it on Bukkit Dev:");
						plugin.getLogger().info(bukkitDevPageLink);
					}
					
				});
				
			}
			
			@Override
			public void onFail(FailCause result) {
				// Handle BAD_VERSION and INVALID_PROJECT_ID only.
				if (result == FailCause.BAD_VERSION) {
					plugin.getLogger().warning("The author of this plugin has misconfigured the Updater system.");
					plugin.getLogger().warning("File versions should follow the format 'PluginName vVERSION'");
		            plugin.getLogger().warning("Please notify the author of this error.");
				} else if (result == FailCause.INVALID_PROJECT_ID) {
					plugin.getLogger().warning("The author of this plugin has misconfigured the Updater system.");
					plugin.getLogger().warning("The project ID (" + projectId + ") provided for updating is invalid.");
					plugin.getLogger().warning("Please notify the author of this error.");
				} else if (result == FailCause.BUKKIT_OFFLINE) {
					plugin.getLogger().warning("Could not contact BukkitDev to check for updates.");
				}
			}
		});
	}
	
	/**
	 * This method creates a new async thread to check for updates.
	 */
	public void checkForUpdates(ResponseHandler responseHandler) {
		Thread updaterThread = new Thread(new UpdaterRunnable(responseHandler));
		updaterThread.start();
	}

	private class UpdaterRunnable implements Runnable {

		ResponseHandler responseHandler;

		private UpdaterRunnable(ResponseHandler responseHandler) {
			this.responseHandler = responseHandler;
		}

		@Override
		public void run() {

			try {

				JSONArray filesArray = (JSONArray) readJson("https://api.curseforge.com/servermods/files?projectIds=" + projectId);

				if (filesArray.size() == 0) {
					// The array cannot be empty, there must be at least one file. The project ID is not valid.
					responseHandler.onFail(FailCause.INVALID_PROJECT_ID);
					return;
				}
				
				String updateName = (String) ((JSONObject) filesArray.get(filesArray.size() - 1)).get("name");
				//String downloadUrl = (String) ((JSONObject) filesArray.get(filesArray.size() - 1)).get("downloadUrl");
				//String releaseType = (String) ((JSONObject) filesArray.get(filesArray.size() - 1)).get("releaseType");
				
				String newVersion = extractVersion(updateName);
				
				if (newVersion == null) {
					responseHandler.onFail(FailCause.BAD_VERSION);
					return;
				}
				
				if (isNewerVersion(newVersion)) {
					responseHandler.onUpdateFound(newVersion);
				} else {
					responseHandler.onFail(FailCause.NO_UPDATES);
				}

			} catch (MalformedURLException e) {
				responseHandler.onFail(FailCause.INVALID_PROJECT_ID);
			} catch (IOException e) {
				responseHandler.onFail(FailCause.BUKKIT_OFFLINE);
			} catch (NumberFormatException e) {
				responseHandler.onFail(FailCause.BAD_VERSION);
			} catch (Exception e) {
				e.printStackTrace();
				plugin.getLogger().warning("Unable to check for updates: unhandled exception.");
			}

		}

	}

	public static interface ResponseHandler {

		/**
		 * Called if the updater finds an update on Bukkit.
		 * @param newVersion the newer version of the plugin.
		 */
		public void onUpdateFound(String newVersion);

		public void onFail(FailCause result);

	}
	
	public static enum FailCause {
		
		/** 
		 * The plugin is already updated to the latest version.
		 */
		NO_UPDATES,
		
		/** 
		 * Remote version format is not correct. Should be in the format "v{VERSION}".
		 */
		BAD_VERSION,
		
		/**
		 * Bukkit is offline or the connection is very slow.
		 */
		BUKKIT_OFFLINE,
		
		/**
		 * The provided project ID is not valid.
		 */
		INVALID_PROJECT_ID
		
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
			
			// Continue the loop
		}
		
		return false;
	}
	
	private String extractVersion(String input) {
		Matcher matcher = Pattern.compile("v[0-9\\.]+").matcher(input);
		
		String result = null;
		while (matcher.find()) {
			result = matcher.group();
		}
		
		return result;
	}
	
	private void notNull(Object o, String msg) {
		if (o == null) {
			throw new IllegalArgumentException(msg);
		}
	}
}
