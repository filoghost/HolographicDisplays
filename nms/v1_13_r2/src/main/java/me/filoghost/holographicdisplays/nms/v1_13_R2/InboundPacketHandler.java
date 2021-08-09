/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.v1_13_R2;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import me.filoghost.fcommons.logging.Log;
import me.filoghost.fcommons.reflection.ReflectField;
import me.filoghost.holographicdisplays.common.nms.NMSErrors;
import me.filoghost.holographicdisplays.common.nms.PacketListener;
import net.minecraft.server.v1_13_R2.PacketPlayInUseEntity;
import org.bukkit.entity.Player;

public class InboundPacketHandler extends ChannelInboundHandlerAdapter {

    public static final String HANDLER_NAME = "holographic_displays_listener";
    private static final ReflectField<Integer> ENTITY_ID_FIELD = ReflectField.lookup(int.class, PacketPlayInUseEntity.class, "a");

    private final Player player;
    private final PacketListener packetListener;

    public InboundPacketHandler(Player player, PacketListener packetListener) {
        this.player = player;
        this.packetListener = packetListener;
    }

    @Override
    public void channelRead(ChannelHandlerContext context, Object packet) throws Exception {
        try {
            if (packet instanceof PacketPlayInUseEntity) {
                int entityID = ENTITY_ID_FIELD.get(packet);
                boolean cancel = packetListener.onAsyncEntityInteract(player, entityID);
                if (cancel) {
                    return;
                }
            }
        } catch (Throwable t) {
            Log.warning(NMSErrors.EXCEPTION_ON_PACKET_READ, t);
        }
        super.channelRead(context, packet);
    }

}
