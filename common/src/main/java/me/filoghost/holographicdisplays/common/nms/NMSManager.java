/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.common.nms;

public interface NMSManager {

    EntityID newEntityID();

    NMSPacketList createPacketList();

}
