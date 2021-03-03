/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.core.nms;

public enum StringCustomNameEditor implements CustomNameEditor {
    
    INSTANCE;

    @Override
    public String replaceCustomName(Object customNameNMSObject, String target, String replacement) {
        String customName = (String) customNameNMSObject;
        if (customName.contains(target)) {
            return customName.replace(target, replacement);
        } else {
            return customName;
        }
    }
    
}
