package com.troblecodings.core.net;

import java.nio.ByteBuffer;

import org.apache.logging.log4j.Logger;

import com.troblecodings.core.WriteBuffer;
import com.troblecodings.core.interfaces.INetworkSync;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class NetworkHandler {

    private final CustomPacketPayload.Type<GuiPayload> payloadType;

    public NetworkHandler(final String modid, final Logger logger, final IEventBus eventBus) {
        this.payloadType = new CustomPacketPayload.Type<>(
                ResourceLocation.fromNamespaceAndPath(modid, "guilib"));
        eventBus.addListener(this::registerPayloads);
    }

    private void registerPayloads(final RegisterPayloadHandlersEvent event) {
        final StreamCodec<RegistryFriendlyByteBuf, GuiPayload> codec = new StreamCodec<>() {
            @Override
            public GuiPayload decode(final RegistryFriendlyByteBuf buffer) {
                return new GuiPayload(payloadType, buffer.readByteArray(1_048_576));
            }

            @Override
            public void encode(final RegistryFriendlyByteBuf buffer, final GuiPayload payload) {
                buffer.writeByteArray(payload.data());
            }
        };
        event.registrar("1").playBidirectional(payloadType, codec, this::handlePayload);
    }

    private void handlePayload(final GuiPayload payload, final IPayloadContext context) {
        final Player player = context.player();
        final AbstractContainerMenu menu = player.containerMenu;
        if (menu instanceof INetworkSync sync) {
            final ByteBuffer buffer = ByteBuffer.wrap(payload.data());
            if (player.level().isClientSide)
                sync.deserializeClient(buffer);
            else
                sync.deserializeServer(buffer);
        }
    }

    public void sendTo(final Player player, final WriteBuffer buffer) {
        sendTo(player, buffer.getBuildedBuffer());
    }

    public void sendTo(final Player player, final ByteBuffer buf) {
        final ByteBuffer copy = buf.duplicate();
        copy.rewind();
        final byte[] data = new byte[copy.remaining()];
        copy.get(data);
        final GuiPayload payload = new GuiPayload(payloadType, data);
        if (player instanceof ServerPlayer server)
            PacketDistributor.sendToPlayer(server, payload);
        else
            PacketDistributor.sendToServer(payload);
    }

    private record GuiPayload(CustomPacketPayload.Type<GuiPayload> type, byte[] data)
            implements CustomPacketPayload {
    }
}