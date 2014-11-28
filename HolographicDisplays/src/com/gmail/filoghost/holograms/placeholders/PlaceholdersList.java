package com.gmail.filoghost.holograms.placeholders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import com.gmail.filoghost.holograms.Configuration;

public class PlaceholdersList {

	// Each 2 tenths of second
	private static final Placeholder RAINBOW_TEXT = new AnimatedPlaceholder("&u", 2, new String[] {
			ChatColor.RED + "",
			ChatColor.GOLD + "",
			ChatColor.YELLOW + "",
			ChatColor.GREEN + "",
			ChatColor.AQUA + "",
			ChatColor.LIGHT_PURPLE + ""
	});
	
	// Each second
	private static final Placeholder ONLINE_PLAYERS = new Placeholder("{online}", "{o}", 10) {
		
		@SuppressWarnings("deprecation")
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
	
	// Each 60 seconds
	private static final Placeholder MOTD = new Placeholder("{motd}", "{motd}", 600) {
		
		@Override
		public void update() {
			currentReplacement = Bukkit.getServer().getMotd();
		}
	};
	
	// Each second
	private static final Placeholder TIME = new Placeholder("{time}", "{t}", 10) {
		
		@Override
		public void update() {
			currentReplacement = Configuration.timeFormat.format(new Date());
		}
		
	};
	
	// Each 10 seconds, maybe has changed
	private static final Placeholder DISPLAYNAME = new AnimatedPlaceholder("{displayname}", 100, new String[] {ChatColor.WHITE + "{displayname}", ChatColor.WHITE + "{displayname}"});
	
	private static List<Placeholder> defaultList = Arrays.asList(RAINBOW_TEXT, ONLINE_PLAYERS, MAX_PLAYERS, TIME, DISPLAYNAME, MOTD);
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
