/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.interfaces.entity;

public interface NMSArmorStand extends NMSNameable {
    
    // Sets the location through NMS and optionally broadcast an additional teleport packet containing the location.
    public void setLocationNMS(double x, double y, double z, boolean broadcastLocationPacket);

}
