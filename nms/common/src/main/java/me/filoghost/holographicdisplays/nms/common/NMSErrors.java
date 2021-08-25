/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.common;

public class NMSErrors {

    public static final String EXCEPTION_GETTING_ENTITY_ID_GENERATOR = "Could not get the NMS entity ID generator."
            + " There is a small chance of entity ID conflicts, causing client-side issues on single entities.";
    public static final String EXCEPTION_ON_PACKET_READ = "Unexpected error while inspecting inbound network packet.";
    public static final String EXCEPTION_MODIFYING_CHANNEL_PIPELINE = "Unexpected error while modifying the channel pipeline.";

}
