/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.v1_13_R2;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelPipeline;
import me.filoghost.fcommons.logging.ErrorCollector;
import me.filoghost.fcommons.logging.Log;
import me.filoghost.fcommons.reflection.ReflectField;
import me.filoghost.holographicdisplays.common.nms.EntityID;
import me.filoghost.holographicdisplays.common.nms.FallbackEntityIDGenerator;
import me.filoghost.holographicdisplays.common.nms.NMSErrors;
import me.filoghost.holographicdisplays.common.nms.NMSManager;
import me.filoghost.holographicdisplays.common.nms.PacketListener;
import me.filoghost.holographicdisplays.common.nms.entity.ClickableNMSPacketEntity;
import me.filoghost.holographicdisplays.common.nms.entity.ItemNMSPacketEntity;
import me.filoghost.holographicdisplays.common.nms.entity.TextNMSPacketEntity;
import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.NetworkManager;
import net.minecraft.server.v1_13_R2.PlayerConnection;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class VersionNMSManager implements NMSManager {

    private static final ReflectField<Integer> ENTITY_ID_COUNTER_FIELD = ReflectField.lookup(int.class, Entity.class, "entityCount");
    private final Supplier<Integer> fallbackEntityIDGenerator;
    private final Supplier<Integer> entityIDGenerator;

    public VersionNMSManager(ErrorCollector errorCollector) {
        this.fallbackEntityIDGenerator = new FallbackEntityIDGenerator();
        this.entityIDGenerator = getEntityIDGenerator(errorCollector);

        // Force initialization of class to eventually throw exceptions early
        DataWatcherKey.ENTITY_STATUS.getIndex();
    }

    private Supplier<Integer> getEntityIDGenerator(ErrorCollector errorCollector) {
        try {
            testStaticFieldReadWrite(ENTITY_ID_COUNTER_FIELD);

            return () -> {
                try {
                    int nmsEntityIDCounter = ENTITY_ID_COUNTER_FIELD.getStatic();
                    ENTITY_ID_COUNTER_FIELD.setStatic(nmsEntityIDCounter + 1);
                    return nmsEntityIDCounter;
                } catch (ReflectiveOperationException e) {
                    // Should not happen, access is tested beforehand
                    return fallbackEntityIDGenerator.get();
                }
            };
        } catch (ReflectiveOperationException e) {
            errorCollector.add(e, NMSErrors.EXCEPTION_GETTING_ENTITY_ID_GENERATOR);
            return fallbackEntityIDGenerator;
        }
    }

    private <T> void testStaticFieldReadWrite(ReflectField<T> field) throws ReflectiveOperationException {
        T value = field.getStatic();
        field.setStatic(value);
    }

    private EntityID newEntityID() {
        return new EntityID(entityIDGenerator);
    }

    @Override
    public TextNMSPacketEntity newTextPacketEntity() {
        return new VersionTextNMSPacketEntity(newEntityID());
    }

    @Override
    public ItemNMSPacketEntity newItemPacketEntity() {
        return new VersionItemNMSPacketEntity(newEntityID(), newEntityID());
    }

    @Override
    public ClickableNMSPacketEntity newClickablePacketEntity() {
        return new VersionClickableNMSPacketEntity(newEntityID(), newEntityID());
    }

    @Override
    public void injectPacketListener(Player player, PacketListener packetListener) {
        modifyPipeline(player, (ChannelPipeline pipeline) -> {
            ChannelHandler currentListener = pipeline.get(InboundPacketHandler.HANDLER_NAME);
            if (currentListener != null) {
                pipeline.remove(InboundPacketHandler.HANDLER_NAME);
            }
            pipeline.addBefore("packet_handler", InboundPacketHandler.HANDLER_NAME, new InboundPacketHandler(player, packetListener));
        });
    }

    @Override
    public void uninjectPacketListener(Player player) {
        modifyPipeline(player, (ChannelPipeline pipeline) -> {
            ChannelHandler currentListener = pipeline.get(InboundPacketHandler.HANDLER_NAME);
            if (currentListener != null) {
                pipeline.remove(InboundPacketHandler.HANDLER_NAME);
            }
        });
    }

    /*
     * Modifying the pipeline in the main thread can cause deadlocks, delays and other concurrency issues,
     * which can be avoided by using the event loop. Thanks to ProtocolLib for this insight.
     */
    private void modifyPipeline(Player player, Consumer<ChannelPipeline> pipelineModifierTask) {
        PlayerConnection playerConnection = ((CraftPlayer) player).getHandle().playerConnection;
        NetworkManager networkManager = playerConnection.a();
        Channel channel = networkManager.channel;

        channel.eventLoop().execute(() -> {
            try {
                pipelineModifierTask.accept(channel.pipeline());
            } catch (Exception e) {
                Log.warning(NMSErrors.EXCEPTION_MODIFYING_CHANNEL_PIPELINE, e);
            }
        });
    }

}
