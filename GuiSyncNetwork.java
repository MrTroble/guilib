package eu.gir.girsignals.guis.guilib;

import java.io.IOException;

import eu.gir.girsignals.items.Placementtool;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ServerCustomPacketEvent;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;

public class GuiSyncNetwork {
	
	public static final byte SEND_TO_ITEM = 0;
	public static final byte SEND_TO_POS = 1;
	
	@SubscribeEvent
	public void onCustomPacket(ServerCustomPacketEvent event) {
		FMLProxyPacket packet = event.getPacket();
		PacketBuffer payBuf = new PacketBuffer(packet.payload());
		EntityPlayerMP mp = ((NetHandlerPlayServer) event.getHandler()).player;
		World world = mp.world;
		byte id = payBuf.readByte();
		switch (id) {
		case SEND_TO_ITEM:
			readItemNBTSET(payBuf, mp);
			break;
		case SEND_TO_POS:
			readFromPos(payBuf, world);
			break;
		default:
			throw new IllegalArgumentException("Wrong packet ID in network recive!");
		}
	}
	
	private static void readFromPos(final PacketBuffer payBuf, final World world) {
		final BlockPos pos = new BlockPos(payBuf.readInt(), payBuf.readInt(), payBuf.readInt());
		final TileEntity tile = world.getTileEntity(pos);
		if (tile != null && tile instanceof ISyncable) {
			final ISyncable syncable = (ISyncable) tile;
			try {
				syncable.updateTag(payBuf.readCompoundTag());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static void readItemNBTSET(PacketBuffer payBuf, EntityPlayer player) {
		try {
			final NBTTagCompound tagCompound = payBuf.readCompoundTag();
			final ItemStack stack = player.getHeldItem(EnumHand.MAIN_HAND);
			if (stack.getItem() instanceof Placementtool) {
				stack.setTagCompound(tagCompound);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void sendToItemServer(NBTTagCompound compound) {
		final ByteBuf buffer = Unpooled.buffer();
		buffer.writeByte(SEND_TO_ITEM);
		final PacketBuffer packet = new PacketBuffer(buffer);
		packet.writeCompoundTag(compound);
		final CPacketCustomPayload payload = new CPacketCustomPayload(UIInit.CHANNELNAME, packet);
		UIInit.getChannel().sendToServer(new FMLProxyPacket(payload));
	}
	
	public static void sendToPosServer(NBTTagCompound compound, BlockPos pos) {
		final ByteBuf buffer = Unpooled.buffer();
		buffer.writeByte(SEND_TO_POS);
		buffer.writeInt(pos.getX());
		buffer.writeInt(pos.getY());
		buffer.writeInt(pos.getZ());
		final PacketBuffer packet = new PacketBuffer(buffer);
		packet.writeCompoundTag(compound);
		final CPacketCustomPayload payload = new CPacketCustomPayload(UIInit.CHANNELNAME, packet);
		UIInit.getChannel().sendToServer(new FMLProxyPacket(payload));
	}
	
}
