/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.common.reflection;

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
