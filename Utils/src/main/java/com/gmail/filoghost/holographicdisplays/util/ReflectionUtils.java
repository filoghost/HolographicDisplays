package com.gmail.filoghost.holographicdisplays.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.logging.Level;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;

/**
 * A set of reflection related utilities.
 * TODO: javadoc
 */
@UtilityClass
public class ReflectionUtils {

	private static Method getStackTraceElementMethod;
	private static Method getStackTraceDepthMethod;
	private static boolean stackTraceErrorPrinted;

	/**
	 * Obtains one stack trace element faster than Throwable.getStackTrace()[element]
	 * as it doesn't generate the full stack trace.
	 *
	 * @param index the index of the stack trace element.
	 * @return the stack trace element.
	 */
	public static StackTraceElement getStackTraceElement(int index) {
		try {
			boolean noGetStackTraceElement = false;
			try {
				if (getStackTraceElementMethod == null) {
					getStackTraceElementMethod = Throwable.class.getDeclaredMethod("getStackTraceElement", int.class);
					getStackTraceElementMethod.setAccessible(true);
				}
				if (getStackTraceDepthMethod == null) {
					getStackTraceDepthMethod = Throwable.class.getDeclaredMethod("getStackTraceDepth");
					getStackTraceDepthMethod.setAccessible(true);
				}
			} catch (NoSuchMethodException e) {
				noGetStackTraceElement = true;
			}

			if (noGetStackTraceElement) {
				// Hotfix for https://github.com/filoghost/HolographicDisplays/issues/70
				// TODO: use StackWalker, but keep java 8 compatibility via reflection
				Throwable dummyThrowable = new Throwable();
				return dummyThrowable.getStackTrace()[index];
			} else {
				Throwable dummyThrowable = new Throwable();
				int depth = (Integer) getStackTraceDepthMethod.invoke(dummyThrowable);

				if (index < depth) {
					return (StackTraceElement) getStackTraceElementMethod.invoke(new Throwable(), index);
				} else {
					return null;
				}
			}

		} catch (Throwable t) {
			if (!stackTraceErrorPrinted) {
				Bukkit.getPluginManager().getPlugin("HolographicDisplays").getLogger().log(Level.WARNING, "Unable to getCurrent a stacktrace element, please inform the developer. You will only see this error once to avoid spam.", t);
				stackTraceErrorPrinted = true;
			}
			return null;
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void putInPrivateStaticMap(Class<?> clazz, String fieldName, Object key, Object value) throws Exception {
		Field field = clazz.getDeclaredField(fieldName);
		field.setAccessible(true);
		Map map = (Map) field.get(null);
		map.put(key, value);
	}

	public static void setPrivateField(Class<?> clazz, Object handle, String fieldName, Object value) throws Exception {
		Field field = clazz.getDeclaredField(fieldName);
		field.setAccessible(true);
		field.set(handle, value);
	}
	
	public static Object getPrivateField(Class<?> clazz, Object handle, String fieldName) throws Exception {
		Field field = clazz.getDeclaredField(fieldName);
		field.setAccessible(true);
		return field.get(handle);
	}

	public static boolean isClassLoaded(String className) {
		try {
			Class.forName(className);
			return true;
		} catch (Throwable t) {
			return false;
		}
	}

	public static boolean instanceOf(Object object, String classPath) {
		try {
			return Class.forName(classPath).isAssignableFrom(object.getClass());
		} catch (Throwable t) {
			return false;
		}
	}
}
