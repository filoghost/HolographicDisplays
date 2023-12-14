/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.v1_20_R3;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import me.filoghost.fcommons.logging.Log;
import me.filoghost.fcommons.reflection.ReflectField;
import me.filoghost.holographicdisplays.nms.common.NMSErrors;
import me.filoghost.holographicdisplays.nms.common.PacketListener;
import net.minecraft.network.protocol.game.PacketPlayInUseEntity;
import org.bukkit.entity.Player;

class InboundPacketHandler extends ChannelInboundHandlerAdapter {

    public static final String HANDLER_NAME = "holographic_displays_listener";
    private static final ReflectField<Integer> ENTITY_ID_FIELD = ReflectField.lookup(int.class, PacketPlayInUseEntity.class, "a");

    private final Player player;
    private final PacketListener packetListener;

    InboundPacketHandler(Player player, PacketListener packetListener) {
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
