package com.troblecodings.guilib.ecs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Function;

import com.google.common.collect.Lists;
import com.troblecodings.guilib.ecs.interfaces.ISyncable;
import com.troblecodings.guilib.ecs.interfaces.ITagableItem;
import com.troblecodings.guilib.ecs.interfaces.UIClientSync;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.entity.player.PlayerMP;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.network.protocol.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientCustomPacketEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ServerCustomPacketEvent;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;

public class GuiSyncNetwork {

    public static final byte SEND_TO_ITEM = 0;
    public static final byte SEND_TO_POS = 1;
    public static final int MAX_PACKET = 32600;

    private static final Random RANDOM = new Random();
    public static final ExecutorService INPUT_GROUP = Executors.newCachedThreadPool();

    private static final HashMap<Integer, ArrayList<Packet>> PACKET_QUEUE = new HashMap<>();

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onCustomClientPacket(final ClientCustomPacketEvent event) {
        final Minecraft mc = Minecraft.getMinecraft();
        final Player player = mc.player;
        final Container container = player.openContainer;
        if (container instanceof UIClientSync) {
            final UIClientSync sync = (UIClientSync) container;
            final FMLProxyPacket packet = event.getPacket();
            final PacketBuffer payBuf = new PacketBuffer(packet.payload());
            unpackNBT(mc::addScheduledTask, payBuf, connectedBuf -> nbt -> sync.readFromNBT(nbt));
        }
    }

    @SubscribeEvent
    public void onCustomPacket(final ServerCustomPacketEvent event) {
        final FMLProxyPacket packet = event.getPacket();
        final PacketBuffer payBuf = new PacketBuffer(packet.payload());
        final NetHandlerPlayServer playerServer = (NetHandlerPlayServer) event.getHandler();
        final PlayerMP mp = playerServer.player;
        unpackNBT(mp.mcServer::addScheduledTask, payBuf, connectedBuf -> {
            final byte id = connectedBuf.readByte();
            switch (id) {
                case SEND_TO_ITEM:
                    return nbt -> readItemNBTSet(nbt, mp);
                case SEND_TO_POS:
                    final BlockPos pos = new BlockPos(connectedBuf.readInt(),
                            connectedBuf.readInt(), connectedBuf.readInt());
                    return nbt -> readFromPos(pos, nbt, mp);
                default:
                    throw new IllegalArgumentException("Wrong packet ID in network recive!");
            }
        });
    }

    private static void readFromPos(final BlockPos pos, final CompoundTag nbt,
            final Player player) {
        final TileEntity tile = player.world.getTileEntity(pos);
        if (tile != null && tile instanceof ISyncable) {
            final ISyncable syncable = (ISyncable) tile;
            if (syncable.isValid(player))
                syncable.updateTag(nbt);
        }
    }

    private static void readItemNBTSet(final CompoundTag tagCompound,
            final Player player) {
        final ItemStack stack = player.getHeldItem(EnumHand.MAIN_HAND);
        if (stack.getItem() instanceof ITagableItem) {
            stack.putCompound(tagCompound);
        }
    }

    private static void unpack(final Consumer<Runnable> server,
            final Function<ByteBuf, Consumer<CompoundTag>> header,
            final ArrayList<PacketBuffer> alreadyPackets) {
        final PacketBuffer[] packetBuffer = new PacketBuffer[alreadyPackets.size()];
        for (final PacketBuffer packet : alreadyPackets) {
            final int id = packet.readInt();
            packetBuffer[id] = packet;
        }
        final ByteBuf preBuffer = Unpooled.buffer();
        for (final PacketBuffer packet : packetBuffer) {
            preBuffer.writeBytes(packet);
        }
        final Consumer<CompoundTag> consumer = header.apply(preBuffer);
        try {
            final CompoundTag nbt = CompressedStreamTools
                    .readCompressed(new ByteBufInputStream(preBuffer));
            if (nbt == null)
                return;
            if (server == null)
                return;
            server.accept(() -> consumer.accept(nbt));
        } catch (final IOException e) {
            e.printStackTrace();
        } finally {
            preBuffer.release();
        }
    }

    private static void unpackNBT(final Consumer<Runnable> server, final PacketBuffer packet,
            final Function<ByteBuf, Consumer<CompoundTag>> header) {
        INPUT_GROUP.execute(() -> {
            final int packetID = packet.readInt();
            final int maxCount = packet.readInt();
            if (maxCount == 1) {
                unpack(server, header, Lists.newArrayList(packet));
            } else {
                if (!PACKET_QUEUE.containsKey(packetID)) {
                    PACKET_QUEUE.put(packetID, new ArrayList<>());
                }
                final ArrayList<PacketBuffer> alreadyPackets = PACKET_QUEUE.get(packetID);
                alreadyPackets.add(packet);
                if (alreadyPackets.size() == maxCount) {
                    unpack(server, header, alreadyPackets);
                    PACKET_QUEUE.remove(packetID);
                }
            }
        });
    }

    private static void packNBT(final CompoundTag compound, final Consumer<ByteBuf> byteBuf,
            final Consumer<PacketBuffer> consumer) {
        if (compound == null || compound.hasNoTags())
            return;
        final ByteBuf preBuffer = Unpooled.buffer();
        byteBuf.accept(preBuffer);
        try {
            CompressedStreamTools.writeCompressed(compound, new ByteBufOutputStream(preBuffer));
        } catch (final IOException e) {
            e.printStackTrace();
        }
        final int packetID = RANDOM.nextInt();
        final int maxCount = (int) Math.ceil(preBuffer.writerIndex() / (double) MAX_PACKET);
        final ByteBuf inputBuffer = preBuffer.asReadOnly();
        for (int i = 0; i < maxCount; i++) {
            final ByteBuf buffer = Unpooled.buffer();
            buffer.writeInt(packetID);
            buffer.writeInt(maxCount);
            buffer.writeInt(i);
            final int readable = inputBuffer.readableBytes();
            buffer.writeBytes(inputBuffer, readable < MAX_PACKET ? readable : MAX_PACKET);
            consumer.accept(new PacketBuffer(buffer));
        }
        inputBuffer.release();
    }

    public static void sendToItemServer(final CompoundTag compound) {
        packNBT(compound, buffer -> buffer.writeByte(SEND_TO_ITEM), packet -> {
            final CPacketCustomPayload payload = new CPacketCustomPayload(UIInit.CHANNELNAME,
                    packet);
            UIInit.getChannel().sendToServer(new FMLProxyPacket(payload));
        });
    }

    public static void sendToPosServer(final CompoundTag compound, final BlockPos pos) {
        packNBT(compound, buffer -> {
            buffer.writeByte(SEND_TO_POS);
            buffer.writeInt(pos.getX());
            buffer.writeInt(pos.getY());
            buffer.writeInt(pos.getZ());
        }, packet -> {
            final CPacketCustomPayload payload = new CPacketCustomPayload(UIInit.CHANNELNAME,
                    packet);
            UIInit.getChannel().sendToServer(new FMLProxyPacket(payload));
        });
    }

    public static void sendToClient(final CompoundTag compound, final PlayerMP player) {
        if (player == null)
            return;
        packNBT(compound, _u -> {
        }, packet -> {
            UIInit.getChannel().sendTo(new FMLProxyPacket(packet, UIInit.CHANNELNAME), player);
        });
    }

}