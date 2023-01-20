package com.troblecodings.core.net;

import java.nio.ByteBuffer;

import org.apache.logging.log4j.Logger;

import com.troblecodings.core.interfaces.INetworkSync;

import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.game.ServerboundCustomPayloadPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.network.NetworkEvent.ClientCustomPayloadEvent;
import net.minecraftforge.network.NetworkEvent.ServerCustomPayloadEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.event.EventNetworkChannel;

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
        final AbstractContainerMenu menu = event.getSource().get().getSender().containerMenu;
        if (menu instanceof INetworkSync) {
            ((INetworkSync) menu).deserializeServer(event.getPayload().nioBuffer());
        }
    }

    @SubscribeEvent
    public void serverEvent(final ServerCustomPayloadEvent event) {
        final Minecraft mc = Minecraft.getInstance();
        final Screen screen = mc.screen;
        if (screen instanceof INetworkSync) {
            ((INetworkSync) screen).deserializeClient(event.getPayload().nioBuffer());
        }
    }

    public void sendTo(final Player player, final ByteBuffer buf) {
        final FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.copiedBuffer(buf.position(0)));
        if (player instanceof ServerPlayer) {
            final ServerPlayer server = (ServerPlayer) player;
            server.connection.send(new ClientboundCustomPayloadPacket(channelName, buffer));
        } else {
            final Minecraft mc = Minecraft.getInstance();
            mc.getConnection().send(new ServerboundCustomPayloadPacket(channelName, buffer));
        }
    }
}
