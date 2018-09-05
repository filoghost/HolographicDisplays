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

import com.google.common.primitives.Ints;

/**
 * A very simple and lightweight updater, without download features.
 * @author filoghost
 */
public final class SimpleUpdater {

	private static Pattern VERSION_PATTERN = Pattern.compile("v?([0-9\\.]+)");
	
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
					final PluginVersion remoteVersion = new PluginVersion(updateName);
					PluginVersion localVersion = new PluginVersion(plugin.getDescription().getVersion());
					
					if (remoteVersion.isNewerThan(localVersion)) {
						Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {

							@Override
							public void run() {
								responseHandler.onUpdateFound(remoteVersion.toString());
							}
						});
					}

				} catch (IOException e) {
					plugin.getLogger().warning("Could not contact BukkitDev to check for updates.");
				} catch (InvalidVersionException e) {
					plugin.getLogger().warning("Could not check for updates because of a version format error: " + e.getMessage() + ".");
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
	
	
	private static class PluginVersion {
		
		// The version extracted from a string, e.g. "Holographic Displays v1.3" becomes [1, 3]
		private int[] versionNumbers;
		private boolean isDevBuild;
		
		
		public PluginVersion(String input) throws InvalidVersionException {
			if (input == null) {
				throw new InvalidVersionException("input was null");
			}
			
			Matcher matcher = VERSION_PATTERN.matcher(input);
			
			if (!matcher.find()) {
				throw new InvalidVersionException("version pattern not found in \"" + input + "\"");
			}
			
			// Get the first group of the matcher (without the "v")
			String version = matcher.group(1);
			
			// Replace multiple full stops (probably typos) with a single full stop, and split the version with them.
			String[] versionParts = version.replaceAll("[\\.]{2,}", ".").split("\\.");
			
			// Convert the strings to integers in order to compare them
			this.versionNumbers = new int[versionParts.length];
			for (int i = 0; i < versionParts.length; i++) {
				try {
					this.versionNumbers[i] = Integer.parseInt(versionParts[i]);
				} catch (NumberFormatException e) {
					throw new InvalidVersionException("invalid number in \"" + input + "\"");
				}
			}
			
			this.isDevBuild = input.contains("SNAPSHOT");
		}
		
		
		/**
		 * Compares this version with another version, using the array "versionNumbers".
		 * Examples:
		 * v1.12 is newer than v1.2 ([1, 12] is newer than [1, 2])
		 * v2.01 is equal to v2.1 ([2, 1] is equal to [2, 1])
		 * 
		 * @return true if this version is newer than the other, false if equal or older
		 */
		public boolean isNewerThan(PluginVersion other) {
			int longest = Math.max(this.versionNumbers.length, other.versionNumbers.length);
			
			for (int i = 0; i < longest; i++) {
				int thisVersionPart = i < this.versionNumbers.length ? this.versionNumbers[i] : 0;
				int otherVersionPart = i < other.versionNumbers.length ? other.versionNumbers[i] : 0;
				int diff = thisVersionPart - otherVersionPart;
				
				if (diff > 0) {
					return true;
				} else if (diff < 0) {
					return false;
				}
				
				// Continue the loop until diff = 0.
			}
			
			// If we get here, they're the same version, check dev builds.
			// This version is newer only if it's not a dev build and the other is.
			if (other.isDevBuild && !this.isDevBuild) {
				return true;
			}
			
			return false;
		}
		
		
		@Override
		public String toString() {
			return "v" + Ints.join(".", versionNumbers);
		}
		
	}
	
	
	private static class InvalidVersionException extends Exception {

		private static final long serialVersionUID = 1L;

		public InvalidVersionException(String message) {
			super(message);
		}
		
	}
	
	
	public interface ResponseHandler {
		
		/**
		 * Called when the updater finds a new version.
		 * @param newVersion - the new version
		 */
		public void onUpdateFound(final String newVersion);
		
	}
	
}
