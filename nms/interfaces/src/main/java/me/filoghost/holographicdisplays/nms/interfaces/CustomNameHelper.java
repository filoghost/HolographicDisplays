/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.interfaces;

import java.util.List;

public class CustomNameHelper {
    
    
    public static String replaceCustomNameString(Object customNameObject, String target, String replacement) {
        String customName = (String) customNameObject;
        if (customName.contains(target)) {
            return customName.replace(target, replacement);
        } else {
            return customName;
        }
    }
    
    public static <T> T replaceCustomNameChatComponent(ChatComponentAdapter<T> versionAdapter, Object customNameObject, String target, String replacement) {
        // Custom name is expected to be a ChatComponentText with empty text and child components (called "siblings") that do not contain more components.
        T rootComponent = versionAdapter.cast(customNameObject);
        if (!versionAdapter.getText(rootComponent).isEmpty()) {
            throw new IllegalArgumentException("Expected root component with empty text");
        }
        
        boolean[] childrenContainingTarget = null;
        List<T> children = versionAdapter.getSiblings(rootComponent);
        int childrenSize = children.size();
        
        for (int i = 0; i < childrenSize; i++) {
            T childComponent = versionAdapter.cast(children.get(i));

            if (!versionAdapter.getSiblings(childComponent).isEmpty()) {
                throw new IllegalArgumentException("Expected child component without sub-nodes");
            }
            
            if (versionAdapter.getText(childComponent).contains(target)) {
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
        T clonedRoot = versionAdapter.cloneComponent(rootComponent);
        for (int i = 0; i < childrenSize; i++) {
            T childComponent = children.get(i);
            
            String newText = versionAdapter.getText(childComponent);
            if (childrenContainingTarget[i]) {
                newText = newText.replace(target, replacement);
            }
            
            T clonedChild = versionAdapter.cloneComponent(childComponent, newText);
            versionAdapter.addSibling(clonedRoot, clonedChild);
        }
        
        return clonedRoot;
    }

}
