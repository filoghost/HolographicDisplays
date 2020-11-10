/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
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
