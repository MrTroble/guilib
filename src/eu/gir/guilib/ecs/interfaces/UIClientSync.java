package eu.gir.guilib.ecs.interfaces;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

public interface UIClientSync {

    void readFromNBT(NBTTagCompound compound);

    EntityPlayerMP getPlayer();
}
