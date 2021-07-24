/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.test;

import me.filoghost.holographicdisplays.common.nms.EntityID;
import me.filoghost.holographicdisplays.common.nms.FallbackEntityIDGenerator;
import me.filoghost.holographicdisplays.common.nms.NMSManager;
import me.filoghost.holographicdisplays.common.nms.NMSPacketList;

import java.util.function.Supplier;

import static org.mockito.Mockito.*;

public class TestNMSManager implements NMSManager {

    private final Supplier<Integer> entityIDGenerator;
    private final NMSPacketList packetList;

    public TestNMSManager() {
        this.entityIDGenerator = new FallbackEntityIDGenerator();
        this.packetList = mock(NMSPacketList.class);
    }

    @Override
    public EntityID newEntityID() {
        return new EntityID(entityIDGenerator);
    }

    @Override
    public NMSPacketList createPacketList() {
        return packetList;
    }

}
