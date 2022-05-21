/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.core.tracking;

import me.filoghost.holographicdisplays.nms.common.PacketGroup;

import java.util.function.Consumer;

interface Viewers<T extends Viewer> {

    void forEach(Consumer<? super T> action);

    default void sendPackets(PacketGroup packetGroup) {
        forEach(viewer -> viewer.sendPackets(packetGroup));
    }

}
