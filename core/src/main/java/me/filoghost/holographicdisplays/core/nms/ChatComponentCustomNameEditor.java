/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.core.nms;

import java.util.List;

public interface ChatComponentCustomNameEditor<T> extends CustomNameEditor {
    
    @Override
    default T replaceCustomName(Object customNameObject, String target, String replacement) {
        // Custom name is expected to be a ChatComponentText with empty text and child components (called "siblings") that do not contain more components.
        @SuppressWarnings("unchecked")
        T rootComponent = (T) customNameObject;
        
        if (!getText(rootComponent).isEmpty()) {
            throw new IllegalArgumentException("Expected root component with empty text");
        }
        
        boolean[] childrenContainingTarget = null;
        List<T> children = getSiblings(rootComponent);
        int childrenSize = children.size();
        
        for (int i = 0; i < childrenSize; i++) {
            T childComponent = children.get(i);

            if (!getSiblings(childComponent).isEmpty()) {
                throw new IllegalArgumentException("Expected child component without sub-nodes");
            }
            
            if (getText(childComponent).contains(target)) {
                // Lazy initialization for performance, since this method can be called frequently.
                if (childrenContainingTarget == null) {
                    childrenContainingTarget = new boolean[childrenSize];
                }
                childrenContainingTarget[i] = true;
            }
        }
        
        if (childrenContainingTarget == null) {
            // No match found, return original unmodified object.
            return rootComponent;
        }
        
        // Clone all the objects and apply replacements where needed.
        T clonedRoot = cloneComponent(rootComponent);
        for (int i = 0; i < childrenSize; i++) {
            T childComponent = children.get(i);
            
            String newText = getText(childComponent);
            if (childrenContainingTarget[i]) {
                newText = newText.replace(target, replacement);
            }
            
            T clonedChild = cloneComponent(childComponent, newText);
            addSibling(clonedRoot, clonedChild);
        }
        
        return clonedRoot;
    }

    String getText(T chatComponent);
    
    List<T> getSiblings(T chatComponent);
    
    void addSibling(T chatComponent, T newSibling);
    
    default T cloneComponent(T chatComponent) {
        return cloneComponent(chatComponent, getText(chatComponent));
    }
    
    T cloneComponent(T chatComponent, String newText);
    
}
