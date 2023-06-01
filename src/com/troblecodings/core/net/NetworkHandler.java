package com.troblecodings.core.net;

import java.nio.ByteBuffer;

import org.apache.logging.log4j.Logger;

import com.troblecodings.core.interfaces.INetworkSync;

import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLEventChannel;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientCustomPacketEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ServerCustomPacketEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;

public class NetworkHandler {

    private final FMLEventChannel channel;
    private final String channelName;

    public NetworkHandler(final String modid, final Logger logger) {
        super();
        this.channelName = new ResourceLocation(modid, "guilib").toString();
        this.channel = NetworkRegistry.INSTANCE.newEventDrivenChannel(this.channelName);
        this.channel.register(this);
    }

    @SubscribeEvent
    public void clientEvent(final ClientCustomPacketEvent event) {
        final Minecraft mc = Minecraft.getMinecraft();
        mc.addScheduledTask(() -> {
            final Container menu = mc.player.openContainer;
            if (menu instanceof INetworkSync) {
                ((INetworkSync) menu).deserializeClient(event.getPacket().payload().nioBuffer());
            }
        });
    }

    @SubscribeEvent
    public void serverEvent(final ServerCustomPacketEvent event) {
        final NetHandlerPlayServer handler = (NetHandlerPlayServer) event.getHandler();
        handler.player.mcServer.addScheduledTask(() -> {
            final Container menu = handler.player.openContainer;
            if (menu instanceof INetworkSync) {
                ((INetworkSync) menu).deserializeServer(event.getPacket().payload().nioBuffer());
            }
        });
    }

    public void sendTo(final EntityPlayer player, final ByteBuffer buf) {
        final PacketBuffer buffer = new PacketBuffer(
                Unpooled.copiedBuffer((ByteBuffer) buf.position(0)));
        if (player instanceof EntityPlayerMP) {
            final EntityPlayerMP server = (EntityPlayerMP) player;
            channel.sendTo(new FMLProxyPacket(buffer, channelName), server);
        } else {
            channel.sendToServer(new FMLProxyPacket(new CPacketCustomPayload(channelName, buffer)));
        }
    }
}
