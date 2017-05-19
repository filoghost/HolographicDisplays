package com.gmail.filoghost.holographicdisplays.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.logging.Level;

import com.gmail.filoghost.holographicdisplays.HolographicDisplays;

public class ReflectionUtils {

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
	
	private static Method getStackTraceElementMethod;
	private static Method getStackTraceDepthMethod;
	
	private static boolean stackTraceErrorPrinted;
	
	/**
	 * If you only need one stack trace element this is faster than Throwable.getStackTrace()[element],
	 * it doesn't generate the full stack trace.
	 */
	public static StackTraceElement getStackTraceElement(int index) {
		try {
			if (getStackTraceElementMethod == null) {
				getStackTraceElementMethod = Throwable.class.getDeclaredMethod("getStackTraceElement", int.class);
				getStackTraceElementMethod.setAccessible(true);
			}
			if (getStackTraceDepthMethod == null) {
				getStackTraceDepthMethod = Throwable.class.getDeclaredMethod("getStackTraceDepth");
				getStackTraceDepthMethod.setAccessible(true);
			}
			
			Throwable dummyThrowable = new Throwable();
			int depth = (Integer) getStackTraceDepthMethod.invoke(dummyThrowable);
			
			if (index < depth) {
				return (StackTraceElement) getStackTraceElementMethod.invoke(new Throwable(), index);
			} else {
				return null;
			}
		} catch (Throwable t) {
			if (!stackTraceErrorPrinted) {
				HolographicDisplays.getInstance().getLogger().log(Level.WARNING, "Unable to get a stacktrace element, please inform the developer. You will only see this error once to avoid spam.", t);
				stackTraceErrorPrinted = true;
			}
			return null;
		}
	}
}
