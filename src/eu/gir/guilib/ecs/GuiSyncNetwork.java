package eu.gir.guilib.ecs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;

import com.google.common.collect.Lists;

import eu.gir.girsignals.items.Placementtool;
import eu.gir.guilib.ecs.interfaces.ISyncable;
import eu.gir.guilib.ecs.interfaces.UIClientSync;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientCustomPacketEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ServerCustomPacketEvent;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;

public class GuiSyncNetwork {

    public static final byte SEND_TO_ITEM = 0;
    public static final byte SEND_TO_POS = 1;
    public static final int MAX_PACKET = 32600;

    private static final Random RANDOM = new Random();

    private static final HashMap<Integer, ArrayList<PacketBuffer>> PACKET_QUEUE = new HashMap<>();

    @SubscribeEvent
    public void onCustomClientPacket(final ClientCustomPacketEvent event) {
        final Container container = Minecraft.getMinecraft().player.openContainer;
        if (container instanceof UIClientSync) {
            final UIClientSync sync = (UIClientSync) container;
            final FMLProxyPacket packet = event.getPacket();
            final PacketBuffer payBuf = new PacketBuffer(packet.payload());
            unpackNBT(payBuf, connectedBuf -> nbt -> sync.readFromNBT(nbt));
        }
    }

    @SubscribeEvent
    public void onCustomPacket(final ServerCustomPacketEvent event) {
        final FMLProxyPacket packet = event.getPacket();
        final PacketBuffer payBuf = new PacketBuffer(packet.payload());
        final EntityPlayerMP mp = ((NetHandlerPlayServer) event.getHandler()).player;
        final World world = mp.world;
        unpackNBT(payBuf, connectedBuf -> {
            final byte id = connectedBuf.readByte();
            switch (id) {
                case SEND_TO_ITEM:
                    return nbt -> readItemNBTSet(nbt, mp);
                case SEND_TO_POS:
                    final BlockPos pos = new BlockPos(connectedBuf.readInt(),
                            connectedBuf.readInt(), connectedBuf.readInt());
                    return nbt -> readFromPos(pos, nbt, world);
                default:
                    throw new IllegalArgumentException("Wrong packet ID in network recive!");
            }
        });
    }

    private static void readFromPos(final BlockPos pos, final NBTTagCompound nbt,
            final World world) {
        final TileEntity tile = world.getTileEntity(pos);
        if (tile != null && tile instanceof ISyncable) {
            final ISyncable syncable = (ISyncable) tile;
            syncable.updateTag(nbt);
        }
    }

    private static void readItemNBTSet(final NBTTagCompound tagCompound,
            final EntityPlayer player) {
        final ItemStack stack = player.getHeldItem(EnumHand.MAIN_HAND);
        if (stack.getItem() instanceof Placementtool) {
            stack.setTagCompound(tagCompound);
        }
    }

    private static void unpack(final Function<ByteBuf, Consumer<NBTTagCompound>> header,
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
        final Consumer<NBTTagCompound> consumer = header.apply(preBuffer);
        try {
            final NBTTagCompound nbt = CompressedStreamTools
                    .readCompressed(new ByteBufInputStream(preBuffer));
            if (nbt == null)
                return;
            consumer.accept(nbt);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    private static void unpackNBT(final PacketBuffer packet,
            final Function<ByteBuf, Consumer<NBTTagCompound>> header) {
        final int packetID = packet.readInt();
        final int maxCount = packet.readInt();
        if (maxCount == 1) {
            unpack(header, Lists.newArrayList(packet));
        } else {
            if (!PACKET_QUEUE.containsKey(packetID)) {
                PACKET_QUEUE.put(packetID, new ArrayList<>());
            }
            final ArrayList<PacketBuffer> alreadyPackets = PACKET_QUEUE.get(packetID);
            alreadyPackets.add(packet);
            if (alreadyPackets.size() == maxCount) {
                unpack(header, alreadyPackets);
                PACKET_QUEUE.remove(packetID);
            }
        }
    }

    private static void packNBT(final NBTTagCompound compound, final Consumer<ByteBuf> byteBuf,
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
    }

    public static void sendToItemServer(final NBTTagCompound compound) {
        packNBT(compound, buffer -> buffer.writeByte(SEND_TO_ITEM), packet -> {
            final CPacketCustomPayload payload = new CPacketCustomPayload(UIInit.CHANNELNAME,
                    packet);
            UIInit.getChannel().sendToServer(new FMLProxyPacket(payload));
        });
    }

    public static void sendToPosServer(final NBTTagCompound compound, final BlockPos pos) {
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

    public static void sendToClient(final NBTTagCompound compound, final EntityPlayerMP player) {
        if (player == null)
            return;
        packNBT(compound, _u -> {
        }, packet -> {
            UIInit.getChannel().sendTo(new FMLProxyPacket(packet, UIInit.CHANNELNAME), player);
        });
    }

}
