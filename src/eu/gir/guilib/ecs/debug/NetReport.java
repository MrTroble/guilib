package eu.gir.guilib.ecs.debug;

import com.google.gson.Gson;

import net.minecraft.nbt.NBTTagCompound;

public class NetReport {

    public transient static final Gson GSON = new Gson();

    public NBTTagCompound compound;
    public long size;
    public String time;
    public int packetSize;
}
