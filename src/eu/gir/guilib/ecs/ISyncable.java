package eu.gir.guilib.ecs;

import net.minecraft.nbt.NBTTagCompound;

public interface ISyncable {
	
	void updateTag(NBTTagCompound compound);
	
	NBTTagCompound getTag();
	
}
