package com.troblecodings.core.net;

import java.nio.ByteBuffer;

import org.apache.logging.log4j.Logger;

import com.troblecodings.core.interfaces.INetworkSync;

import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CCustomPayloadPacket;
import net.minecraft.network.play.server.SCustomPayloadPlayPacket;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.NetworkEvent.ClientCustomPayloadEvent;
import net.minecraftforge.fml.network.NetworkEvent.ServerCustomPayloadEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.event.EventNetworkChannel;

public class NetworkHandler {

    private final EventNetworkChannel channel;
    private final ResourceLocation channelName;

    public NetworkHandler(final String modid, final Logger logger) {
        super();
        this.channelName = new ResourceLocation(modid, "guilib");
        this.channel = NetworkRegistry.newEventChannel(this.channelName, () -> modid,
                modid::equalsIgnoreCase, modid::equalsIgnoreCase);
        this.channel.registerObject(this);
    }

    @SubscribeEvent
    public void clientEvent(final ClientCustomPayloadEvent event) {
        final Container menu = event.getSource().get().getSender().containerMenu;
        if (menu instanceof INetworkSync) {
            ((INetworkSync) menu).deserializeServer(event.getPayload().nioBuffer());
            event.getSource().get().setPacketHandled(true);
        }
    }

    @SubscribeEvent
    public void serverEvent(final ServerCustomPayloadEvent event) {
        final Minecraft mc = Minecraft.getInstance();
        final Container menu = mc.player.containerMenu;
        if (menu instanceof INetworkSync) {
            ((INetworkSync) menu).deserializeClient(event.getPayload().nioBuffer());
            event.getSource().get().setPacketHandled(true);
        }
    }

    public void sendTo(final PlayerEntity player, final ByteBuffer buf) {
        final PacketBuffer buffer = new PacketBuffer(
                Unpooled.copiedBuffer((ByteBuffer) buf.position(0)));
        if (player instanceof ServerPlayerEntity) {
            final ServerPlayerEntity server = (ServerPlayerEntity) player;
            server.connection.send(new SCustomPayloadPlayPacket(channelName, buffer));
        } else {
            final Minecraft mc = Minecraft.getInstance();
            mc.getConnection().send(new CCustomPayloadPacket(channelName, buffer));
        }
    }
}
