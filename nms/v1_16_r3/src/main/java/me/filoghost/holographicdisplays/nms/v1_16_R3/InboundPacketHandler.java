/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.v1_16_R3;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import me.filoghost.fcommons.logging.Log;
import me.filoghost.fcommons.reflection.ReflectField;
import me.filoghost.holographicdisplays.nms.common.NMSErrors;
import me.filoghost.holographicdisplays.nms.common.PacketListener;
import net.minecraft.server.v1_16_R3.EnumHand;
import net.minecraft.server.v1_16_R3.PacketPlayInUseEntity;
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
    public void channelRead(ChannelHandlerContext context, Object packetObject) throws Exception {
        try {
            if (packetObject instanceof PacketPlayInUseEntity) {
                PacketPlayInUseEntity packet = (PacketPlayInUseEntity) packetObject;

                int entityID = ENTITY_ID_FIELD.get(packetObject);
                Boolean isRightClick = this.isRightClick(packet);

                if (isRightClick != null) {
                    boolean cancel = packetListener.onAsyncEntityInteract(player, entityID, isRightClick);
                    if (cancel) {
                        return;
                    }
                }
            }
        } catch (Throwable t) {
            Log.warning(NMSErrors.EXCEPTION_ON_PACKET_READ, t);
        }
        super.channelRead(context, packetObject);
    }

    private Boolean isRightClick(PacketPlayInUseEntity packet) {
        if (packet.b() == PacketPlayInUseEntity.EnumEntityUseAction.INTERACT_AT || packet.c() == EnumHand.OFF_HAND)
            return null;
        return packet.b() == PacketPlayInUseEntity.EnumEntityUseAction.INTERACT;
    }

}
