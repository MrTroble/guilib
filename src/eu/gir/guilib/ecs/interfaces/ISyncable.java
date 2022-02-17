package eu.gir.guilib.ecs.interfaces;

import net.minecraft.nbt.NBTTagCompound;

public interface ISyncable {
	
	void updateTag(NBTTagCompound compound);
	
	NBTTagCompound getTag();
	
}
