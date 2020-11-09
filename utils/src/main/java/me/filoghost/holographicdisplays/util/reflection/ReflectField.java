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
package me.filoghost.holographicdisplays.util.reflection;

import java.lang.reflect.Field;

@SuppressWarnings("unchecked")
public class ReflectField<T> {
	
	private final Class<?> clazz;
	private final String name;
	
	private Field field;
	
	public ReflectField(Class<?> clazz, String name) {
		this.clazz = clazz;
		this.name = name;
	}
	
	private void init() throws Exception {
		if (field == null) {
			field = clazz.getDeclaredField(name);
			field.setAccessible(true);
		}
	}
	
	public T get(Object instance) throws Exception {
		init();
		return (T) field.get(instance);
	}
	
	public T getStatic() throws Exception {
		init();
		return (T) field.get(null);
	}
	
	public void set(Object instance, T value) throws Exception {
		init();
		field.set(instance, value);
	}
	
	public void setStatic(T value) throws Exception {
		init();
		field.set(null, value);
	}

}
