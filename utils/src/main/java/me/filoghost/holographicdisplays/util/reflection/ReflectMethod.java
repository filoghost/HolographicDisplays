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

import java.lang.reflect.Method;

@SuppressWarnings("unchecked")
public class ReflectMethod<T> {
	
	private final Class<?> clazz;
	private final String name;
	private final Class<?>[] parameterTypes;
	
	private Method method;
	
	public ReflectMethod(Class<?> clazz, String name, Class<?>... parameterTypes) {
		this.clazz = clazz;
		this.name = name;
		this.parameterTypes = parameterTypes;
	}
	
	private void init() throws Exception {
		if (method == null) {
			method = clazz.getDeclaredMethod(name, parameterTypes);
			method.setAccessible(true);
		}
	}
	
	public T invoke(Object instance, Object... args) throws Exception {
		init();
		return (T) method.invoke(instance, args);
	}
	
	public T invokeStatic(Object... args) throws Exception {
		init();
		return (T) method.invoke(null, args);
	}

}
