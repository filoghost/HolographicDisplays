package com.gmail.filoghost.holographicdisplays.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

public class ReflectionUtils {
	
	private static Method getStackTraceElementMethod;

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
			
			return (StackTraceElement) getStackTraceElementMethod.invoke(new Throwable(), index);
		} catch (Exception e) {
			// Fallback
			return new Throwable().getStackTrace()[index];
		}
	}
}
