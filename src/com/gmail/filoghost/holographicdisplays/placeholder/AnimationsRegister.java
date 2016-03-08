package com.gmail.filoghost.holographicdisplays.placeholder;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.bukkit.plugin.Plugin;

import com.gmail.filoghost.holographicdisplays.HolographicDisplays;
import com.gmail.filoghost.holographicdisplays.disk.StringConverter;
import com.gmail.filoghost.holographicdisplays.util.DebugHandler;
import com.gmail.filoghost.holographicdisplays.util.FileUtils;
import com.gmail.filoghost.holographicdisplays.util.Utils;

public class AnimationsRegister {
	
	// <fileName, lines>
	private final static Map<String, Placeholder> animations = Utils.newMap();
	
	public static void loadAnimations(Plugin plugin) {
		animations.clear();
		
		File animationFolder = new File(plugin.getDataFolder(), "animations");
		if (!animationFolder.isDirectory()) {
			animationFolder.mkdirs();
			plugin.saveResource("animations/example.txt", false);
			return;
		}
		
		for (File file : animationFolder.listFiles()) {
			
			try {
				List<String> lines = FileUtils.readLines(file);
				if (lines.size() == 0) {
					continue;
				}
				
				double speed = 0.5;
				boolean validSpeedFound = false;
				
				String firstLine = lines.get(0).trim();
				if (firstLine.toLowerCase().startsWith("speed:")) {
					
					// Do not consider it.
					lines.remove(0);
					
					firstLine = firstLine.substring("speed:".length()).trim();
					
					try {
						speed = Double.parseDouble(firstLine);
						validSpeedFound = true;
					} catch (NumberFormatException e) {	}
				}
				
				if (!validSpeedFound) {
					plugin.getLogger().warning("Could not find a valid 'speed: <number>' in the first line of the file '" + file.getName() + "'. Default speed of 0.5 seconds will be used.");
				}
				
				if (lines.isEmpty()) {
					lines.add("[No lines: " + file.getName() + "]");
					plugin.getLogger().warning("Could not find any line in '" + file.getName() + "' (excluding the speed). You should add at least one more line.");
				}
				
				// Replace placeholders.
				for (int i = 0; i < lines.size(); i++) {
					lines.set(i, StringConverter.toReadableFormat(lines.get(i)));
				}
				
				animations.put(file.getName(), new Placeholder(HolographicDisplays.getInstance(), file.getName(), speed, new CyclicPlaceholderReplacer(lines.toArray(new String[lines.size()]))));
				DebugHandler.handleAnimationLoadSuccess(file.getName(), speed);
				
			} catch (Exception e) {
				e.printStackTrace();
				plugin.getLogger().severe("Couldn't load the file '" + file.getName() + "'!");
			}
		}
	}

	
	public static Map<String, Placeholder> getAnimations() {
		return animations;
	}

	public static Placeholder getAnimation(String name) {
		return animations.get(name);
	}
	
}
