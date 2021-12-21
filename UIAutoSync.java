package eu.gir.girsignals.guis.guilib;

import net.minecraft.nbt.NBTTagCompound;

public interface UIAutoSync {

	public void write(final NBTTagCompound compound);

	public void read(final NBTTagCompound compound);
}
