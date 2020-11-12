/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.interfaces.entity;

public interface NMSCanMount extends NMSEntityBase {
    
    // Sets the passenger of this entity through NMS.
    void setPassengerOfNMS(NMSEntityBase vehicleBase);
    
}
