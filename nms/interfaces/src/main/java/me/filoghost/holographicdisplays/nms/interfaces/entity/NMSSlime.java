/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.interfaces.entity;

public interface NMSSlime extends NMSEntityBase, NMSCanMount {
    
    // Sets the location through NMS.
    public void setLocationNMS(double x, double y, double z);
    
}
