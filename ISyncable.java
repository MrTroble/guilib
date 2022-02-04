package eu.gir.girsignals.guis.guilib;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ISyncable {
	
	void readFromNBT(NBTTagCompound compound);
	
	NBTTagCompound writeToNBT(NBTTagCompound compound);
	
	void updateTag(NBTTagCompound compound);
	
	NBTTagCompound getTag();
	
	BlockPos getPos();
	
	World getWorld();
	
	default SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(getPos(), 0, getUpdateTag());
	}
	
	default void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		this.readFromNBT(pkt.getNbtCompound());
	}
	
	default NBTTagCompound getUpdateTag() {
		return writeToNBT(new NBTTagCompound());
	}
	
	default void syncClient() {
		syncClient(getWorld(), getPos());
	}
	
	default void syncClient(World world, BlockPos pos) {
		final IBlockState state = world.getBlockState(pos);
		world.notifyBlockUpdate(pos, state, state, 3);
	}
	
}
