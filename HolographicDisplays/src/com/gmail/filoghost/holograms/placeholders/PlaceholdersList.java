package com.gmail.filoghost.holograms.placeholders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.bukkit.Bukkit;

import com.gmail.filoghost.holograms.Configuration;

public class PlaceholdersList {

	// Each 2 tenths of second
	private static final Placeholder RAINBOW_TEXT = new AnimatedPlaceholder("&u", 2, new String[] {"§c", "§6", "§e", "§a", "§b", "§d"});
	
	// Each second
	private static final Placeholder ONLINE_PLAYERS = new Placeholder("{online}", "{o}", 10) {
		
		@Override
		public void update() {
			currentReplacement = Integer.toString(Bukkit.getOnlinePlayers().length);
		}
		
	};
	
	// Each 10 seconds
	private static final Placeholder MAX_PLAYERS = new Placeholder("{max_players}", "{m}", 100) {
		
		@Override
		public void update() {
			currentReplacement = Integer.toString(Bukkit.getMaxPlayers());
		}
		
	};
	
	// Each second
	private static final Placeholder TIME = new Placeholder("{time}", "{t}", 10) {
		
		@Override
		public void update() {
			currentReplacement = Configuration.timeFormat.format(new Date());
		}
		
	};
	
	// Each 5 seconds, maybe has changed
	private static final Placeholder DISPLAYNAME = new AnimatedPlaceholder("{displayname}", 30, new String[]{"§f{displayname}", "§r{displayname}"});
	
	private static List<Placeholder> defaultList = Arrays.asList(RAINBOW_TEXT, ONLINE_PLAYERS, MAX_PLAYERS, TIME, DISPLAYNAME);
	private static List<AnimatedPlaceholder> animatedList = new ArrayList<AnimatedPlaceholder>();

	public static List<Placeholder> getDefaults() {
		return defaultList;
	}
	
	public static List<AnimatedPlaceholder> getAnimated() {
		return animatedList;
	}
	
	public static void clearAnimated() {
		animatedList.clear();
	}
	
	public static void addAnimatedPlaceholder(AnimatedPlaceholder animated) {
		if (!animatedList.contains(animated)) {
			animatedList.add(animated);
		}
	}
}
