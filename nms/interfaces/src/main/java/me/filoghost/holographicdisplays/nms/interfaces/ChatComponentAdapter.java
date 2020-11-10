/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.interfaces;

import java.util.List;

public interface ChatComponentAdapter<T> {
    
    T cast(Object chatComponentObject);
    
    String getText(T chatComponent);
    
    List<T> getSiblings(T chatComponent);
    
    void addSibling(T chatComponent, T newSibling);
    
    default T cloneComponent(T chatComponent) {
        return cloneComponent(chatComponent, getText(chatComponent));
    }
    
    T cloneComponent(T chatComponent, String newText);
    
}
