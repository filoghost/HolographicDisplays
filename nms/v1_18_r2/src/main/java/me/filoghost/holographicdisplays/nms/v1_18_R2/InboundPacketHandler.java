/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.v1_18_R2;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import me.filoghost.fcommons.logging.Log;
import me.filoghost.fcommons.reflection.ReflectField;
import me.filoghost.holographicdisplays.nms.common.NMSErrors;
import me.filoghost.holographicdisplays.nms.common.PacketListener;
import net.minecraft.network.protocol.game.PacketPlayInUseEntity;
import net.minecraft.world.EnumHand;
import net.minecraft.world.phys.Vec3D;
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
                Action action = new Action();
                packet.a(action);

                if (action.isRightClick != null) {
                    boolean cancel = packetListener.onAsyncEntityInteract(player, entityID, action.isRightClick);
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

    private static class Action implements PacketPlayInUseEntity.c {

        Boolean isRightClick = null;

        public void a(EnumHand var1) { // INTERACT
            if (var1 == EnumHand.b) return; // OFF_HAND

            this.isRightClick = true;
        }

        public void a(EnumHand var1, Vec3D var2) { // INTERACT_AT
        }

        public void a() { // ATTACK
            this.isRightClick = false;
        }
    }
}
