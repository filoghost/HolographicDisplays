/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.common.nms.entity;

public interface NMSArmorStand extends NMSVehicle {

    void setCustomNameNMS(String customName);

    /**
     * Returns the last custom name set.
     */
    String getCustomNameStringNMS();

    /**
     * Returns the custom name NMS object, whose type is version-dependent (String for MC 1.12 and below, ChatComponent
     * for MC 1.13+). The returned value may differ from {@link #getCustomNameStringNMS()} even if it's a string, for
     * example if the custom name has been truncated before being applied.
     */
    Object getCustomNameObjectNMS();

}
