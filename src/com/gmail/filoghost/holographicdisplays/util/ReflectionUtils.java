package com.gmail.filoghost.holographicdisplays.util;

import java.lang.reflect.Field;
import java.util.Map;

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
}
