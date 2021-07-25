/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.v1_17_R1;

import me.filoghost.fcommons.logging.ErrorCollector;
import me.filoghost.fcommons.reflection.ReflectField;
import me.filoghost.holographicdisplays.common.nms.EntityID;
import me.filoghost.holographicdisplays.common.nms.FallbackEntityIDGenerator;
import me.filoghost.holographicdisplays.common.nms.NMSErrors;
import me.filoghost.holographicdisplays.common.nms.NMSManager;
import me.filoghost.holographicdisplays.common.nms.NMSPacketList;
import net.minecraft.world.entity.Entity;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

public class VersionNMSManager implements NMSManager {

    private static final ReflectField<AtomicInteger> ENTITY_ID_COUNTER_FIELD = ReflectField.lookup(AtomicInteger.class, Entity.class, "b");
    private final Supplier<Integer> entityIDGenerator;

    public VersionNMSManager(ErrorCollector errorCollector) {
        this.entityIDGenerator = getEntityIDGenerator(errorCollector);

        // Force initialization of class to eventually throw exceptions early
        DataWatcherKey.ENTITY_STATUS.getKeyIndex();
    }

    private Supplier<Integer> getEntityIDGenerator(ErrorCollector errorCollector) {
        try {
            AtomicInteger nmsEntityIDCounter = ENTITY_ID_COUNTER_FIELD.getStatic();
            return nmsEntityIDCounter::incrementAndGet;
        } catch (ReflectiveOperationException e) {
            errorCollector.add(e, NMSErrors.EXCEPTION_GETTING_ENTITY_ID_GENERATOR);
            return new FallbackEntityIDGenerator();
        }
    }

    @Override
    public EntityID newEntityID() {
        return new EntityID(entityIDGenerator);
    }

    @Override
    public NMSPacketList createPacketList() {
        return new VersionNMSPacketList();
    }

}
