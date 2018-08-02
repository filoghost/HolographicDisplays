package com.gmail.filoghost.holographicdisplays.util.bukkit;

import com.gmail.filoghost.holographicdisplays.util.ConsoleLogger;
import com.gmail.filoghost.holographicdisplays.util.ReflectionUtils;
import com.google.common.collect.ImmutableList;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A collection of utility methods related to Bukkit server versions.
 */
public final class BukkitUtils {

	private static Method getOnlinePlayersMethod;
	private static boolean getOnlinePlayersUseReflection;

	static {
		try {
			getOnlinePlayersMethod = Bukkit.class.getDeclaredMethod("getOnlinePlayers");
			if (getOnlinePlayersMethod.getReturnType() == Player[].class) {
				getOnlinePlayersUseReflection = true;
			}
		} catch (NoSuchMethodException e) {
			ConsoleLogger.error(e);
		}
	}

	private BukkitUtils() {
	}

	/**
	 * Legacy compatible version of the Bukkit.getOnlinePlayers() method.
	 *
	 * @return the collection of online players, empty list if reflection fails.
	 */
	public static Collection<? extends Player> getOnlinePlayers() {
		try {
			if (getOnlinePlayersUseReflection) {
				return ImmutableList.copyOf((Player[]) getOnlinePlayersMethod.invoke(null));
			}
			return Bukkit.getOnlinePlayers();
		} catch (IllegalAccessException | InvocationTargetException e) {
			ConsoleLogger.error("Unable to obtain obtain online players via reflection!", e);
			return Collections.emptyList();
		}
	}

	/**
	 * This method returns the current NMS version code.
	 * Compatible with CraftBukkit/Spigot and MCPC+/Cauldron.
	 * Example: v1_8_R1
	 *
	 * @return the NMS package part or null if not found.
	 */
	public static String getNMSVersion() {
		String version = getBukkitVersion();
		if (version == null) {
			// Caused by MCPC+/Cauldron renaming packages, extract the version from Bukkit.getVersion().
			String serverVersion = getMinecraftVersion();
			if ("1.7.2".equals(serverVersion)) {
				version = "v1_7_R1";
			} else if ("1.7.5".equals(serverVersion)) {
				version = "v1_7_R2";
			} else if ("1.7.8".equals(serverVersion)) {
				version = "v1_7_R3";
			} else if ("1.7.10".equals(serverVersion)) {
				version = "v1_7_R4";
			} else if ("1.8".equals(serverVersion)) {
				version = "v1_8_R1";
			} else if ("1.8.3".equals(serverVersion)) {
				version = "v1_8_R2";
			}
		}
		return version;
	}

	/**
	 * This method uses a regex to getCurrent the NMS package part that changes with every update.
	 * Example: v1_8_R1
	 *
	 * @return the NMS package part or null if not found.
	 */
	public static String getBukkitVersion() {
		Matcher matcher = Pattern.compile("v\\d+_\\d+_R\\d+").matcher(Bukkit.getServer().getClass().getPackage().getName());
		return matcher.find() ? matcher.group() : null;
	}

	/**
	 * This method uses a regex to getCurrent the version of this Minecraft release.
	 * Example: 1.8.1
	 *
	 * @return the version of this release or null if not found.
	 */
	public static String getMinecraftVersion() {
		Matcher matcher = Pattern.compile("(\\(MC: )([\\d.]+)(\\))").matcher(Bukkit.getVersion());
		return matcher.find() ? matcher.group(2) : null;
	}

	/**
	 * Returns if the server is based on Forge.
	 *
	 * @return true if the server is based on Forge.
	 */
	public static boolean isForgeServer() {
		return ReflectionUtils.isClassLoaded("net.minecraftforge.common.MinecraftForge");
	}
}
