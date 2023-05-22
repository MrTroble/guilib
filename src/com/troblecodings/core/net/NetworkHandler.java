package com.troblecodings.core.net;

import java.nio.ByteBuffer;

import org.apache.logging.log4j.Logger;

import com.troblecodings.core.interfaces.INetworkSync;

import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CCustomPayloadPacket;
import net.minecraft.network.play.server.SCustomPayloadPlayPacket;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientCustomPacketEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ServerCustomPacketEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.handshake.NetworkDispatcher;
import net.minecraftforge.fml.network.event.EventNetworkChannel;

public class NetworkHandler {

    private final NetworkDispatcher channel;
    private final ResourceLocation channelName;

    public NetworkHandler(final String modid, final Logger logger) {
        super();
        this.channelName = new ResourceLocation(modid, "guilib");
        this.channel = NetworkRegistry.newEventChannel(this.channelName, () -> modid,
                modid::equalsIgnoreCase, modid::equalsIgnoreCase);
        this.channel.registerObject(this);
    }

    @SubscribeEvent
    public void clientEvent(final ClientCustomPacketEvent event) {
        final Container menu = event.getSource().get().getSender().containerMenu;
        if (menu instanceof INetworkSync) {
            ((INetworkSync) menu).deserializeServer(event.getPayload().nioBuffer());
            event.getSource().get().setPacketHandled(true);
        }
    }

    @SubscribeEvent
    public void serverEvent(final ServerCustomPacketEvent event) {
        final Minecraft mc = Minecraft.getMinecraft();
        final Container menu = mc.player.openContainer;
        if (menu instanceof INetworkSync) {

            ((INetworkSync) menu).deserializeClient(event.getPacket().payload().nioBuffer());
            event.getSource().get().setPacketHandled(true);
        }
    }

    public void sendTo(final EntityPlayer player, final ByteBuffer buf) {
        final PacketBuffer buffer = new PacketBuffer(
                Unpooled.copiedBuffer((ByteBuffer) buf.position(0)));
        if (player instanceof EntityPlayerSP) {
            final EntityPlayerSP server = (EntityPlayerSP) player;
            server.connection.send(new SCustomPayloadPlayPacket(channelName, buffer));
        } else {
            final Minecraft mc = Minecraft.getMinecraft();
            mc.getConnection().send(new CCustomPayloadPacket(channelName, buffer));
        }
    }
}
