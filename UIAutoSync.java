package eu.gir.girsignals.guis.guilib;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface UIAutoSync {
	
	public void write(final NBTTagCompound compound);
	
	public void read(final NBTTagCompound compound);
}
