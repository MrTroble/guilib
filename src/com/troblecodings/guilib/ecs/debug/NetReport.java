package com.troblecodings.guilib.ecs.debug;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.minecraft.nbt.CompoundTag;

public class NetReport {

    public transient static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public CompoundTag compound;
    public long size;
    public String time;
    public int packetSize;
    public boolean serverPresent;
}
