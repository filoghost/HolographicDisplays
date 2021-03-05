/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.core.nms.entity;

public interface NMSArmorStand extends NMSVehicle {

    // Sets a custom name as a String.
    void setCustomNameNMS(String name);

    // Returns the custom name as a String.
    String getCustomNameStringNMS();

    // Returns the custom name as version-dependent NMS object (String for MC 1.12 and below, ChatComponent for MC 1.13+ a ChatComponent).
    Object getCustomNameObjectNMS();

}
