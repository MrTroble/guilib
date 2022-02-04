package eu.gir.girsignals.guis.guilib;

import net.minecraft.nbt.NBTTagCompound;

public interface ISyncable {
		
	void updateTag(NBTTagCompound compound);
	
	NBTTagCompound getTag();
		
}
